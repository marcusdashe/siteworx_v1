package ng.siteworx.partner.user.merchant.controllers

import ng.siteworx.partner.security.jwt.JwtUtils
import ng.siteworx.partner.user.merchant.services.MerchantService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile


@RestController
@RequestMapping("/api_v1/merchant")
class MerchantApi (private val merchantService: MerchantService){

    @Autowired
    lateinit var jwtUtils: JwtUtils


//    Upload merchant Avatar image
    @PostMapping("/avatar")
    @PreAuthorize("hasRole('ARTISAN') or hasRole('MERCHANT') or hasRole('CLIENT')")
    fun uploadAvatar(
        @RequestParam("file") file: MultipartFile,
        @RequestHeader("Authorization") authHeader: String
    ): ResponseEntity<String> {
        val jwt = authHeader.replace("Bearer ", "")
        val username = jwtUtils.getUserNameFromJwtToken(jwt)
        val filePath = merchantService.uploadAvatar(username, file)
        return ResponseEntity.ok().body(filePath)
    }
}