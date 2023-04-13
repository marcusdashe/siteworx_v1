package ng.siteworx.partner.user.merchant.services

import jakarta.persistence.EntityNotFoundException
import jakarta.transaction.Transactional
import ng.siteworx.partner.enums.Constants
import ng.siteworx.partner.payload.request.ProductRequest
import ng.siteworx.partner.payload.response.MessageResponse
import ng.siteworx.partner.user.merchant.model.Merchant
import ng.siteworx.partner.user.merchant.model.Product
import ng.siteworx.partner.user.merchant.repository.ProductRepository
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*

@Service
class ProductService(private val productRepository: ProductRepository) {

    fun createProduct(payload: ProductRequest?): ResponseEntity<*>{
        payload?.let{
            val product = Product().apply {
                name = it.name
                description = it.description
                quantity = it.quantity
                price = it.price
            }
            return ResponseEntity.ok().body(productRepository.save(product))
        }
        return badRequestResponseEntity()
    }

    fun updateProduct(productId: Long, payload: ProductRequest?): ResponseEntity<*>{
        payload?.let{
            val product = productRepository.findById(productId).get()
            product.name = it.name
            product.description = it.description
            product.price = it.price
            product.quantity= it.quantity
            return ResponseEntity.ok().body(productRepository.save(product))
        }
        return badRequestResponseEntity()
    }

    fun save(product: Product): Product? {
        return productRepository.save(product)
    }

    fun findById(id: Long): ResponseEntity<Optional<Product>> {
        return ResponseEntity.ok().body(productRepository.findById(id))
    }

    fun findAllProducts(): ResponseEntity<List<Product>> {
        return ResponseEntity.ok().body(productRepository.findAll())
    }

    fun deleteProductById(id: Long): ResponseEntity<*> {
        return ResponseEntity.ok().body(productRepository.deleteById(id))
    }

fun fetchProductByName(name: String?): ResponseEntity<*> {
    name?.let{
            return  ResponseEntity.ok().body(this.productRepository.findByName(name))
        }
        return badRequestResponseEntity()
    }

    fun fetchProductByMerchantId(merchantId: Long?): ResponseEntity<*>{
        merchantId?.let {
            return ResponseEntity.ok().body(this.productRepository.findByMerchantId(it))
        }
        return badRequestResponseEntity()
    }

    fun getProductsByMerchant(merchant: Merchant): List<Product> {
        return this.productRepository.findByMerchant(merchant)
    }

    fun fetchProductByPriceLessThan(priceLessThan: Double?): ResponseEntity<*>{
        priceLessThan?.let {
            return ResponseEntity.ok().body(this.productRepository.findByPriceLessThan(it))
        }
        return badRequestResponseEntity()
    }

    fun fetchProductByDescriptionContaining(keyword: String?): ResponseEntity<*>{
        keyword?.let {
            return ResponseEntity.ok().body(this.productRepository.findByDescriptionContaining(it))
        }
        return badRequestResponseEntity()
    }

    fun fetchProductByCategory(category: String?): ResponseEntity<*>{
        category?.let {
            return ResponseEntity.ok().body(this.productRepository.findByCategory(it))
        }
        return badRequestResponseEntity()
    }

    fun fetchByMerchantIntentions(intention: Constants.PURCHASE_LEASE): ResponseEntity<*>{
        intention.let {
            return ResponseEntity.ok().body(this.productRepository.findByMerchantIntentions(it))
        }
    }

    fun fetchAllByMerchantId(merchantId: Long?): ResponseEntity<*>{
        merchantId?.let {
            return ResponseEntity.ok().body(this.productRepository.findAllByMerchantId(it))
        }
        return badRequestResponseEntity()
    }

    fun fetchAllProductsByName(name: String?): ResponseEntity<*>{
        name?.let {
            return ResponseEntity.ok().body(this.productRepository.findAllByName(it))
        }
        return badRequestResponseEntity()
    }

    fun fetchAllProductsByDescription(description: String?): ResponseEntity<*>{
        description?.let {
            return ResponseEntity.ok().body(this.productRepository.findAllByDescription(it))
        }
        return badRequestResponseEntity()
    }

    fun fetchAllProductsByPriceBetween(minPrice: Double, maxPrice: Double): ResponseEntity<*>{
        if(minPrice != null || minPrice < maxPrice){
            return ResponseEntity.ok().body(this.productRepository.findAllByPriceBetween(minPrice, maxPrice))
        }
        return badRequestResponseEntity()
    }

    @Transactional
    fun addProductPhotos(productId: Long, photoPaths: List<String>): ResponseEntity<Product> {
        val product = productRepository.findById(productId)
            .orElseThrow {EntityNotFoundException("Product with ID $productId not found") }
        product.photoPaths = product.photoPaths.plus(photoPaths)
        return ResponseEntity.ok().body(productRepository.save(product))
    }
    fun saveProductPhoto(file: MultipartFile): String {
        val fileName = UUID.randomUUID().toString() + "-" + file.originalFilename
        val uploadDir = "uploads"
        val uploadPath: Path = Paths.get(uploadDir)

        if (!Files.exists(uploadPath)) {
            try {
                Files.createDirectories(uploadPath)
            } catch (e: IOException) {
                throw RuntimeException("Failed to create upload directory", e)
            }
        }

        val filePath = uploadPath.resolve(fileName)
        try {
            Files.copy(file.inputStream, filePath)
        } catch (e: IOException) {
            throw RuntimeException("Failed to store file $fileName", e)
        }

        return filePath.toAbsolutePath().toString()
    }

    fun badRequestResponseEntity() =  ResponseEntity.badRequest().body(MessageResponse("Bad Request"))

}
