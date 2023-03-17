package ng.siteworx.partner.service

import jakarta.mail.internet.InternetAddress
import jakarta.mail.internet.MimeMessage
import ng.siteworx.partner.repository.EveryoneRepository
import ng.siteworx.partner.templates.SignupEmailTemplate.emailHtmlBody
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.LocalDateTime


@Service
class AuthService(private val everyoneRepository: EveryoneRepository, private var mailSender: JavaMailSender) {


    @Value("\${siteworx.ng.baseUrl}")
    private lateinit var baseUrl: String

    fun sendMailWhenRegistered(email: String, verificationToken: String?){
        // Send email
        val message = mailSender.createMimeMessage()
        val helper = MimeMessageHelper(message, true)
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
//            it.isVerify = true
            everyoneRepository.save(it)
        }
    }


}


