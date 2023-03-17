package ng.siteworx.partner.user.merchant.services

import ng.siteworx.partner.exceptions.MerchantNotFoundException
import ng.siteworx.partner.repository.EveryoneRepository
import ng.siteworx.partner.user.merchant.repository.MerchantRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths


@Service
class MerchantService(private val merchantRepository: MerchantRepository, private val everyoneRepository: EveryoneRepository) {


    @Value("\${file.upload-dir}")
    private lateinit var uploadDir: String

    fun uploadAvatar(username: String, file: MultipartFile): String? {
//        val merchant = merchantRepository.findById(merchantId).orElse(null)
//            ?: return null // Merchant with given ID not found

        val everyone = everyoneRepository.findByUsername(username)
            ?: throw MerchantNotFoundException("Merchant with username $username not found") // Merchant with given ID not found

        val fileName = "${System.currentTimeMillis()}_${file.originalFilename}"
        val filePath: Path = Paths.get(uploadDir + fileName)

        return try {
            Files.copy(file.inputStream, filePath)
            merchant.avatarUrl = filePath.toString()
            merchantRepository.save(merchant)
            merchant.avatarUrl
        } catch (e: IOException) {
            e.printStackTrace()
            null // Error occurred during file upload
        }
    }

}