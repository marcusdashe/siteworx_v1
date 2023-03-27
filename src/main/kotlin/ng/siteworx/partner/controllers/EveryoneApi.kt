package ng.siteworx.partner.controllers

import ng.siteworx.partner.service.EveryoneService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api_v1/auth")
class EveryoneApi(private val everyoneService: EveryoneService) {
    @PostMapping("/avatar")
    @PreAuthorize("hasRole('ARTISAN') or hasRole('MERCHANT') or hasRole('CLIENT')")
    fun uploadAvatar(
        @RequestParam("file") file: MultipartFile,
    ): ResponseEntity<String> {

        val authentication = SecurityContextHolder.getContext().authentication
        val userDetails = authentication.principal as UserDetails

        val filePath = everyoneService.uploadAvatar(userDetails.username, file)
        return ResponseEntity.ok().body(filePath)
    }
}
