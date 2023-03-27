package ng.siteworx.partner.user.merchant.services

import ng.siteworx.partner.enums.Constants
import ng.siteworx.partner.payload.request.MerchantRequest
import ng.siteworx.partner.payload.response.MessageResponse
import ng.siteworx.partner.user.merchant.model.Merchant
import org.springframework.http.ResponseEntity

interface MerchantService {
    fun findByFirstNameLastName(firstName: String, lastName: String): ResponseEntity<Merchant?>
    fun onboardMerchant(payload: MerchantRequest): ResponseEntity<*>
    fun getMerchantById(id: Long): ResponseEntity<Merchant?>
    fun updateMerchant(id: Long, payload: MerchantRequest): ResponseEntity<MessageResponse>
    fun deleteMerchantById(id: Long): ResponseEntity<MessageResponse>
    fun getAllMerchants():  ResponseEntity<MutableList<Merchant>>
    fun getMerchantsBySubscriptionLevel(subscriptionLevel: Constants.SUBSCRIPTION_LEVEL): ResponseEntity<List<Merchant>>
    fun getAvailableMerchants(isAvail: Boolean): ResponseEntity<List<Merchant>>
    fun getUnavailableMerchants(): ResponseEntity<List<Merchant>>
    fun subscribeMerchant(id: Long, subscriptionLevel: Constants.SUBSCRIPTION_LEVEL): ResponseEntity<MessageResponse>

}