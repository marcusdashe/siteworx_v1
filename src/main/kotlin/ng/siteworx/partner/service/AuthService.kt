package ng.siteworx.partner.service

import jakarta.mail.internet.InternetAddress
import jakarta.mail.internet.MimeMessage
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import ng.siteworx.partner.enums.Constants
import ng.siteworx.partner.models.Bank
import ng.siteworx.partner.models.Contact
import ng.siteworx.partner.models.Everyone
import ng.siteworx.partner.models.Role
import ng.siteworx.partner.payload.request.LoginRequest
import ng.siteworx.partner.payload.request.SignupRequest
import ng.siteworx.partner.payload.response.JwtResponse
import ng.siteworx.partner.payload.response.MessageResponse
import ng.siteworx.partner.repository.EveryoneRepository
import ng.siteworx.partner.security.jwt.JwtUtils
import ng.siteworx.partner.security.service.UserDetailsImpl
import ng.siteworx.partner.templates.SignupEmailTemplate.emailHtmlBody
import ng.siteworx.partner.user.artisan.models.Artisan
import ng.siteworx.partner.user.client.model.Client
import ng.siteworx.partner.user.merchant.model.Merchant
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*
import java.util.stream.Collectors
import kotlin.collections.HashSet



@Service
class AuthService(private val everyoneRepository: EveryoneRepository, private val authenticationManager: AuthenticationManager,
                  private val jwtUtils: JwtUtils, private var mailSender: JavaMailSender, private val passwordEncoder: PasswordEncoder,
                  private val entityManager: EntityManager
    ) {

    @Value("\${siteworx.ng.baseUrl}")
    private lateinit var baseUrl: String

    fun createEveryoneFromRequest(signupRequest: SignupRequest, rolesO: MutableSet<Role>): Everyone {
        val everyone = Everyone().apply {
            username = signupRequest.username
            email = signupRequest.email
            password = passwordEncoder.encode(signupRequest.password)
            roles = rolesO
            verificationToken = UUID.randomUUID().toString()
        }
        return everyone
    }

    fun getRolesFromString(strRoles: Set<String>): MutableSet<Role> {
        val rolesO = HashSet<Role>()
        if (strRoles.isEmpty()) {
            rolesO.add(Role(Constants.USER_TYPE.CLIENT))
        } else {
            strRoles.forEach { role ->
                when (role.trim().lowercase()) {
                    "admin" -> rolesO.add(Role(Constants.USER_TYPE.ADMIN))
                    "artisan" -> rolesO.add(Role(Constants.USER_TYPE.ARTISAN))
                    "merchant" -> rolesO.add(Role(Constants.USER_TYPE.MERCHANT))
                    "client" -> rolesO.add(Role(Constants.USER_TYPE.CLIENT))
                    "subcontractor" -> rolesO.add(Role(Constants.USER_TYPE.SUBCONTRACTOR))
                    "professional" -> rolesO.add(Role(Constants.USER_TYPE.PROFESSIONAL))
                }
            }
        }
        return rolesO
    }

    @Transactional
    fun createEveryone(signupRequest: SignupRequest): ResponseEntity<*> {
        if (everyoneRepository.existsByUsername(signupRequest.username)) {
            return ResponseEntity.badRequest()
                .body(MessageResponse("Error: Username is already taken!"))
        }

        if (everyoneRepository.existsByEmail(signupRequest.email)) {
            return ResponseEntity.badRequest()
                .body(MessageResponse("Error: Email is already taken!"))
        }
        val strRoles: Set<String> = signupRequest.roles
//        val rolesO: MutableSet<Role> = HashSet()

        val rolesO = getRolesFromString(strRoles)

        val everyone = createEveryoneFromRequest(signupRequest, rolesO)


        strRoles.forEach {role ->
            run {
                when (role.trim().lowercase()) {
                    "client" -> {
                        val client = Client()
                        val bank = Bank()
                        val contact = Contact()

                        client.everyone = everyone
                        bank.everyone = everyone
                        contact.everyone = everyone

                        everyone.client = client
                        everyone.bank = bank
                        everyone.contact = contact
                    }
                    "merchant" -> {
                        val merchant = Merchant()
                        val bank = Bank()
                        val contact = Contact()

                        merchant.everyone = everyone
                        merchant.everyone = everyone
                        merchant.everyone = everyone

                        everyone.merchant = merchant
                        everyone.bank = bank
                        everyone.contact = contact
                    }
                    "artisan" -> {

                        val artisan = Artisan()
                        val bank = Bank()
                        val contact = Contact()

                        artisan.everyone = everyone
                        bank.everyone = everyone
                        contact.everyone = everyone

                        everyone.artisan = artisan
                        everyone.bank = bank
                        everyone.contact = contact
                    }

                    "anonymous" -> {
                        println("anonymous")
                    }
                }
            }
        }

        val createdEveryone = everyoneRepository.save(everyone)

        sendMailWhenRegistered(createdEveryone.email, createdEveryone.verificationToken)
        return ResponseEntity.ok(MessageResponse("User registered successfully!"))
    }

    fun signIn(loginRequest: LoginRequest): ResponseEntity<*>{
        val authentication: Authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(loginRequest.username, loginRequest.password)
        )
        val userDetails: UserDetailsImpl = authentication.principal as UserDetailsImpl
        val roles: List<String> = userDetails.authorities.stream().map { item ->
            item.authority
        }.collect(Collectors.toList())

        val jwt: String = jwtUtils.generateJwtToken(authentication)
        return ResponseEntity.ok(
            JwtResponse(
                jwt,
                userDetails.getId(),
                userDetails.username,
                userDetails.getEmail(),
                roles
            )
        )
    }

    fun sendMailWhenRegistered(email: String, verificationToken: String?){
        // Send email
        val message = mailSender.createMimeMessage()
        val verificationLink = "${baseUrl}verify?token=${verificationToken}"

        message.setFrom(InternetAddress("no-reply@siteworx.ng"))
        message.setRecipients(MimeMessage.RecipientType.TO, email)
        message.subject = "Welcome to our Siteworx Marketplace"
        val allContent = "$emailHtmlBody \n\n $verificationLink"
        message.setContent(allContent , "text/html; charset=utf-8")
        mailSender.send(message)
    }

    fun verifyToken(token: String): ResponseEntity<String>{
        val everyone = everyoneRepository.findByVerificationToken(token)
            ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Verification token not found")

        if (everyone.isVerify) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Account already verified")
        }

        val now = LocalDateTime.now()
        val createdDate = everyone.created ?: now
        if (createdDate.plusDays(1) < now) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Verification token expired")
        }
        everyone.isVerify = true
        everyoneRepository.save(everyone)

        return ResponseEntity.ok("Account verified successfully")
    }

/*
* This service function uses Spring's @Scheduled annotation to run the removeExpiredVerificationTokens method once a day.
* It finds all the users in the database whose verificationToken is not null and whose createdDate is more than 24 hours ago,
*  removes the token, sets the verified flag to true, and saves the changes to the database using the everyoneRepository.
* */
    @Scheduled(fixedRate = 86400000) // runs once a day
    fun removeExpiredVerificationTokens() {
        val expiredUsers = everyoneRepository.findByVerificationTokenNotNullAndCreatedBefore(LocalDateTime.now().minusDays(1))
        expiredUsers.forEach {
            it.verificationToken = null
            everyoneRepository.save(it)
        }
    }


}


