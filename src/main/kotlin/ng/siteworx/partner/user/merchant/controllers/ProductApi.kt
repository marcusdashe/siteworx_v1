package ng.siteworx.partner.user.merchant.controllers

import ng.siteworx.partner.enums.Constants
import ng.siteworx.partner.payload.request.ProductRequest
import ng.siteworx.partner.payload.response.MessageResponse
import ng.siteworx.partner.user.merchant.model.Merchant
import ng.siteworx.partner.user.merchant.model.Product
import ng.siteworx.partner.user.merchant.services.ProductService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.net.URI
import java.util.*

@RestController
@RequestMapping("/api_v1/product")
class ProductApi(private val productService: ProductService) {

    @PreAuthorize("hasRole('ADMIN') or hasRole('MERCHANT')")
    @PostMapping("/register")
    fun createProduct(@RequestBody payload: ProductRequest): ResponseEntity<*> =
        this.productService.createProduct(payload)

    @PreAuthorize("hasRole('ADMIN') or hasRole('MERCHANT') or hasRole('CLIENT') or hasRole('ARTISAN')")
    @GetMapping("/")
    fun getAllProducts(): ResponseEntity<*> = this.productService.findAllProducts()

    @PreAuthorize("hasRole('ADMIN') or hasRole('MERCHANT') or hasRole('CLIENT') or hasRole('ARTISAN')")
    @GetMapping("/search/{id}")
    fun searchProductById(
        @PathVariable id: Long,
    ): ResponseEntity<Optional<Product>> = this.productService.findById(id)

    @PreAuthorize("hasRole('ADMIN') or hasRole('MERCHANT')")
    @DeleteMapping("/{id}")
    fun deleteProductById(@PathVariable id: Long): ResponseEntity<*> = this.productService.deleteProductById(id)

    @PreAuthorize("hasRole('ADMIN') or hasRole('MERCHANT') or hasRole('CLIENT') or hasRole('ARTISAN')")
    @GetMapping("/search")
    fun findByFirstName(@RequestParam productName: String): ResponseEntity<*> =
        this.productService.fetchProductByName(productName)

    @PreAuthorize("hasRole('ADMIN') or hasRole('MERCHANT') or hasRole('CLIENT') or hasRole('ARTISAN')")
    @GetMapping("/merchant/{id}")
    fun searchProductByMerchantId(
        @PathVariable id: Long?,
    ): ResponseEntity<*> = this.productService.fetchProductByMerchantId(id)

    @PreAuthorize("hasRole('ADMIN') or hasRole('MERCHANT') or hasRole('CLIENT') or hasRole('ARTISAN')")
    @GetMapping("/search/price")
    fun searchProductByPriceLessThan(@RequestParam priceLessThan: Double): ResponseEntity<*> =
        this.productService.fetchProductByPriceLessThan(priceLessThan)

    @PreAuthorize("hasRole('ADMIN') or hasRole('MERCHANT') or hasRole('CLIENT') or hasRole('ARTISAN')")
    @GetMapping("/search/desc")
    fun searchProductByDescriptionKeyword(@RequestParam desc: String): ResponseEntity<*> =
        this.productService.fetchProductByDescriptionContaining(desc)

    @PreAuthorize("hasRole('ADMIN') or hasRole('MERCHANT') or hasRole('CLIENT') or hasRole('ARTISAN')")
    @GetMapping("/search/category")
    fun searchProductByCategory(@RequestParam category: String): ResponseEntity<*> =
        this.productService.fetchProductByCategory(category)

    @PreAuthorize("hasRole('ADMIN') or hasRole('MERCHANT') or hasRole('CLIENT') or hasRole('ARTISAN')")
    @GetMapping("/search/intention")
    fun searchProductByIntention(@RequestParam intention: String): ResponseEntity<*> {
        val intent = Constants.PURCHASE_LEASE.valueOf(intention.uppercase())
        return this.productService.fetchByMerchantIntentions(intent)
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MERCHANT') or hasRole('CLIENT') or hasRole('ARTISAN')")
    @GetMapping("/search/minmaxprice")
    fun searchProductsByMinMaxPrice(
        @RequestParam min: Double,
        @RequestParam max: Double
    ): ResponseEntity<*> = this.productService.fetchAllProductsByPriceBetween(min, max)

    @PreAuthorize("hasRole('ADMIN') or hasRole('MERCHANT')")
    @PostMapping("/upload")
    fun uploadPhoto(@RequestParam("file") file: MultipartFile): ResponseEntity<*> {
        val filePath = this.productService.saveProductPhoto(file)
        val photoPathsList = listOf(filePath)
        return this.productService.addProductPhotos(2, photoPathsList)
//        return ResponseEntity.created(URI.create(filePath)).body(filePath)
    }

}