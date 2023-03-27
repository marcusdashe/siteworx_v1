package ng.siteworx.partner.user.merchant.controllers

import ng.siteworx.partner.enums.Constants
import ng.siteworx.partner.payload.request.MerchantRequest
import ng.siteworx.partner.payload.response.MessageResponse
import ng.siteworx.partner.security.jwt.JwtUtils
import ng.siteworx.partner.user.merchant.model.Merchant
import ng.siteworx.partner.user.merchant.services.MerchantServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile


@RestController
@RequestMapping("/api_v1/merchant")
class MerchantApi (private val merchantService: MerchantServiceImpl) {

    @PreAuthorize("hasRole('ADMIN') or hasRole('MERCHANT') or hasRole('CLIENT') or hasRole('ARTISAN')")
    @GetMapping("/")
    fun getAllMerchants(): ResponseEntity<MutableList<Merchant>> = this.merchantService.getAllMerchants()

    @PreAuthorize("hasRole('ADMIN') or hasRole('MERCHANT')")
    @PostMapping("/register")
    fun createMerchant(@RequestBody payload: MerchantRequest): ResponseEntity<*> = this.merchantService.onboardMerchant(payload)

    @PreAuthorize("hasRole('ADMIN') or hasRole('MERCHANT')")
    @PutMapping("/{id}")
    fun updateMerchant(
        @PathVariable id: Long,
        @RequestBody payload: MerchantRequest
    ): ResponseEntity<MessageResponse> = this.merchantService.updateMerchant(id, payload)

//    @PreAuthorize("hasRole('ADMIN') or hasRole('MERCHANT')")
//    @PutMapping("/update")
//    fun merchantSelfUpdate(
//        @RequestBody payload: MerchantRequest
//    ): ResponseEntity<MessageResponse> {
//        val authentication = SecurityContextHolder.getContext().authentication
//        val userDetails = authentication.principal as UserDetails
//
//        return this.merchantService.updateMerchant(userDetails., payload)
//    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MERCHANT')")
    @DeleteMapping("/{id}")
    fun deleteMerchantById(@PathVariable id: Long): ResponseEntity<MessageResponse> = this.merchantService.deleteMerchantById(id)

    @PreAuthorize("hasRole('ADMIN') or hasRole('MERCHANT')")
    @GetMapping("/search/{id}")
    fun searchMerchantById(
        @PathVariable id: Long,
    ): ResponseEntity<Merchant?> = this.merchantService.getMerchantById(id)

    @PreAuthorize("hasRole('ADMIN') or hasRole('MERCHANT')")
    @GetMapping("/search/firstlastname")
    fun findByFirstNameLastName(
        @RequestParam firstName: String,
        @RequestParam lastName: String
    ): ResponseEntity<*> {
        val merchant = merchantService.findByFirstNameLastName(firstName, lastName)
        return ResponseEntity.ok().body(merchant)
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MERCHANT')")
    @GetMapping("/search/firstname")
    fun findByFirstName(
        @RequestParam firstName: String,

    ): ResponseEntity<*> {
        val merchant = merchantService.getMerchantByFirstName(firstName)
        return ResponseEntity.ok().body(merchant)
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MERCHANT')")
    @GetMapping("/search/lastname")
    fun findByLastName(
        @RequestParam lastName: String,
        ): ResponseEntity<*> {
        val merchant = merchantService.getMerchantByLastName(lastName)
        return ResponseEntity.ok().body(merchant)
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MERCHANT')")
    @GetMapping("/search/subscription")
    fun getMerchantsBySubscriptionLevel(@RequestParam subscriptionLevel: String): ResponseEntity<List<Merchant>> {
        val level = Constants.SUBSCRIPTION_LEVEL.valueOf(subscriptionLevel.uppercase())
        return merchantService.getMerchantsBySubscriptionLevel(level)
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MERCHANT')")
    @GetMapping("/search/available")
    fun getAvailableMerchants(@RequestParam isAvail: Boolean): ResponseEntity<List<Merchant>> = this.merchantService.getAvailableMerchants(isAvail)

    @PreAuthorize("hasRole('ADMIN') or hasRole('MERCHANT')")
    @PutMapping("/{id}/subscribe")
    fun subscribeMerchant(@PathVariable id: Long, @RequestParam subscriptionLevel: String): ResponseEntity<MessageResponse> {
        val level = Constants.SUBSCRIPTION_LEVEL.valueOf(subscriptionLevel.uppercase())
       return  merchantService.subscribeMerchant(id, level)
    }
}