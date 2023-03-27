package ng.siteworx.partner.controllers

import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import ng.siteworx.partner.payload.request.LoginRequest
import ng.siteworx.partner.payload.request.SignupRequest
import ng.siteworx.partner.payload.response.MessageResponse
import ng.siteworx.partner.service.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api_v1/auth")
class AuthApi(private val authService: AuthService) {
    @PostMapping("/signin")
    fun authenticate(@Valid @RequestBody loginRequest: LoginRequest): ResponseEntity<*> = authService.signIn(loginRequest)

    @PostMapping("/signup")
    fun register(@Valid @RequestBody signupRequest: SignupRequest): ResponseEntity<*> = authService.createEveryone(signupRequest)

    @GetMapping("/logout")
    fun logout(request: HttpServletRequest): ResponseEntity<*> {
        SecurityContextHolder.clearContext()
        return ResponseEntity.ok(MessageResponse("User logged out successfully!"))
    }

    @GetMapping("/verify")
    fun verifyAccount(@RequestParam token: String): ResponseEntity<String> = authService.verifyToken(token)

}