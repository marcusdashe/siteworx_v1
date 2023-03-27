package ng.siteworx.partner.user.merchant.services

import ng.siteworx.partner.enums.Constants
import ng.siteworx.partner.exceptions.MerchantCouldNotBeDeletedException
import ng.siteworx.partner.payload.request.MerchantRequest
import ng.siteworx.partner.payload.response.MessageResponse
import ng.siteworx.partner.repository.EveryoneRepository
import ng.siteworx.partner.user.merchant.model.Merchant
import ng.siteworx.partner.user.merchant.model.Product
import ng.siteworx.partner.user.merchant.repository.MerchantRepository
import ng.siteworx.partner.user.merchant.repository.ProductRepository
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.stereotype.Service


@Service
class MerchantServiceImpl(private val merchantRepository: MerchantRepository,
                          private val everyoneRepository: EveryoneRepository,
                          private val authenticationManager: AuthenticationManager,
                          private val productRepository: ProductRepository
    ):
    MerchantService {

    override fun onboardMerchant(payload: MerchantRequest): ResponseEntity<*>{
        if(payload != null){
            val merchant = Merchant().apply {
                firstName = payload.firstName
                lastName = payload.lastName
                isAvailable = false
                hasSubscribe = false
                subscriptionLevel =  Constants.SUBSCRIPTION_LEVEL.FREE
            }
            return ResponseEntity.ok().body(merchantRepository.save(merchant))
        }
        else{
            return ResponseEntity.badRequest().body(MessageResponse("Bad request"))
        }
    }

//    Return Merchant by First Name & Last Name
    override fun findByFirstNameLastName(firstName: String, lastName: String): ResponseEntity<Merchant?>{
        return ResponseEntity.ok().body(merchantRepository.findByFirstNameAndLastName(firstName, lastName))
    }

    fun getMerchantByFirstName(firstName: String): Merchant?{
        return merchantRepository.findByFirstName(firstName)
    }

    fun getMerchantByLastName(lastName: String): Merchant?{
        return merchantRepository.findByLastName(lastName)
    }

    override fun updateMerchant(id: Long, payload: MerchantRequest): ResponseEntity<MessageResponse>{
        // Update the merchant properties with the new values
        val merchant: Merchant = merchantRepository.getReferenceById(id)
        merchant.firstName = payload.firstName?.let{it}
        merchant.lastName = payload.lastName?.let{it}
        merchant.isAvailable = payload.isAvailable?.let{it}
        merchant.hasSubscribe = payload.hasSubscribe?.let{it}
        merchant.subscriptionLevel = payload.subscriptionLevel?.let{
            when(it){
                "free" -> Constants.SUBSCRIPTION_LEVEL.FREE
                "trial" -> Constants.SUBSCRIPTION_LEVEL.TRIAL
                "paid" -> Constants.SUBSCRIPTION_LEVEL.PAID
                else -> Constants.SUBSCRIPTION_LEVEL.FREE
            }
        }!!
        merchantRepository.save(merchant)
        return ResponseEntity.ok().body(MessageResponse("Successfully updated merchant"))
    }
    override fun getMerchantById(id: Long): ResponseEntity<Merchant?> {
        return ResponseEntity.ok().body(merchantRepository.getReferenceById(id))
    }

    override fun deleteMerchantById(id: Long): ResponseEntity<MessageResponse> {
        try{
            merchantRepository.deleteById(id)
        } catch(e: Exception) {
            throw MerchantCouldNotBeDeletedException("Merchant could not be deleted")
        }
        return ResponseEntity.ok().body(MessageResponse("Merchant deleted successfully"))
    }

    override fun getAllMerchants(): ResponseEntity<MutableList<Merchant>>{
        return ResponseEntity.ok().body(merchantRepository.findAll())
    }

    override fun getMerchantsBySubscriptionLevel(subscriptionLevel: Constants.SUBSCRIPTION_LEVEL): ResponseEntity<List<Merchant>>{
        return ResponseEntity.ok().body(merchantRepository.findBySubscriptionLevel(subscriptionLevel))
    }

    override fun getAvailableMerchants(isAvail: Boolean): ResponseEntity<List<Merchant>>{
        return ResponseEntity.ok().body(merchantRepository.findByIsAvailable(isAvail))
    }

    override fun getUnavailableMerchants(): ResponseEntity<List<Merchant>>{
        return ResponseEntity.ok().body(merchantRepository.findByIsAvailable(false))
    }

    override fun subscribeMerchant(id: Long, subscriptionLevel: Constants.SUBSCRIPTION_LEVEL): ResponseEntity<MessageResponse>{
        val merchant =  merchantRepository.getReferenceById(id)
        merchant.subscriptionLevel = subscriptionLevel
        return ResponseEntity.ok().body(MessageResponse("Subscribed successfully"))
    }

    fun addProductToMerchant(merchant: Merchant, product: Product) {
        product.merchant = merchant // Set the Merchant object on the Product object
        merchant.products.add(product) // Add the Product object to the Merchant's products list
        // Save both the Merchant and Product objects using the repositories
        merchantRepository.save(merchant)
        productRepository.save(product)
    }

    fun addProductsToMerchant(merchant: Merchant, products: List<Product>) {
        for (product in products) {
            addProductToMerchant(merchant, product) // Add each product to the merchant using the addProductToMerchant function
        }
    }

}