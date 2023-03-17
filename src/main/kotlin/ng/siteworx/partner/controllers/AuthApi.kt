package ng.siteworx.partner.controllers

import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Constraint
import jakarta.validation.Valid
import ng.siteworx.partner.enums.Constants
import ng.siteworx.partner.models.Everyone
import ng.siteworx.partner.models.Role
import ng.siteworx.partner.payload.request.LoginRequest
import ng.siteworx.partner.payload.request.SignupRequest
import ng.siteworx.partner.payload.response.JwtResponse
import ng.siteworx.partner.payload.response.MessageResponse
import ng.siteworx.partner.repository.EveryoneRepository
import ng.siteworx.partner.security.jwt.JwtUtils
import ng.siteworx.partner.security.service.UserDetailsImpl
import ng.siteworx.partner.service.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import java.security.Security
import java.util.*
import java.util.stream.Collectors
import kotlin.collections.HashSet

@RestController
@RequestMapping("/api_v1/auth")
class AuthApi(
    private val authService: AuthService,
    private val authenticationManager: AuthenticationManager,
    private val everyoneRepository :  EveryoneRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtils: JwtUtils,
) {
    @PostMapping("/signin")
    fun authenticate(@Valid @RequestBody loginRequest: LoginRequest): ResponseEntity<*> {
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

    @PostMapping("/signup")
    fun register(@Valid @RequestBody signupRequest: SignupRequest): ResponseEntity<*> {
        if (everyoneRepository.existsByUsername(signupRequest.username)) {
            return ResponseEntity.badRequest()
                .body(MessageResponse("Error: Username is already taken!"))
        }

        if (everyoneRepository.existsByEmail(signupRequest.email)) {
            return ResponseEntity.badRequest()
                .body(MessageResponse("Error: Email is already taken!"))
        }
        val strRoles: Set<String> = signupRequest.roles
        val roles: MutableSet<Role> = HashSet()
        if (strRoles.isEmpty()) {
            roles.add(Role(Constants.USER_TYPE.CLIENT))
        } else {
            strRoles.forEach { role ->
                run {
                    when (role.trim().lowercase()) {
                        "admin" -> roles.add(Role(Constants.USER_TYPE.ADMIN))
                        "artisan" -> roles.add(Role(Constants.USER_TYPE.ARTISAN))
                        "merchant" -> roles.add(Role(Constants.USER_TYPE.MERCHANT))
                        "subcontractor" -> roles.add(Role(Constants.USER_TYPE.SUBCONTRACTOR))
                        "professional" -> roles.add(Role(Constants.USER_TYPE.PROFESSIONAL))

                    }
                }
            }
        }

        val everyone = Everyone()
        everyone.username = signupRequest.username
        everyone.email = signupRequest.email
        everyone.password = passwordEncoder.encode(signupRequest.password)
        everyone.roles = roles
        everyone.verificationToken = UUID.randomUUID().toString()
        val createdEveryone = everyoneRepository.save(everyone)

//        authService.sendMailWhenRegistered(createdEveryone.email, createdEveryone.verificationToken)
        return ResponseEntity.ok(MessageResponse("User registered successfully!"))
    }

    @GetMapping("/logout")
    fun logout(request: HttpServletRequest): ResponseEntity<*> {
        SecurityContextHolder.clearContext()
        return ResponseEntity.ok(MessageResponse("User logged out successfully!"))
    }

    @GetMapping("/verify")
    fun verifyAccount(@RequestParam token: String): ResponseEntity<String> = authService.verifyToken(token)

}