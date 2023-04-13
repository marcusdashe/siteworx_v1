package ng.siteworx.partner.user.merchant.repository

import ng.siteworx.partner.enums.Constants
import ng.siteworx.partner.user.merchant.model.Merchant
import ng.siteworx.partner.user.merchant.model.Product
import org.springframework.data.jpa.repository.JpaRepository

interface ProductRepository : JpaRepository<Product, Long> {

    // Find product by ID
    fun findByName(name: String): List<Product?>
    fun findByMerchantId(merchantId: Long): List<Product?>
    fun findByPriceLessThan(price: Double): List<Product?>
    fun findByDescriptionContaining(keyword: String): List<Product?>

    fun findByCategory(category: String): List<Product?>

    fun findByMerchantIntentions(intent: Constants.PURCHASE_LEASE): List<Product?>
    // Find all products by merchant ID
    fun findAllByMerchantId(merchantId: Long): List<Product?>

    // Find all products by name
    fun findAllByName(name: String): List<Product>

    // Find all products by description
    fun findAllByDescription(description: String): List<Product>

    // Find all products by price range
    fun findAllByPriceBetween(minPrice: Double, maxPrice: Double): List<Product>

    // Save or update a product
    fun save(product: Product): Product

    fun findByMerchant(merchant: Merchant): List<Product>
}