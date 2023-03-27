package ng.siteworx.partner.service

import ng.siteworx.partner.exceptions.MerchantNotFoundException
import ng.siteworx.partner.repository.EveryoneRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths


@Service
class EveryoneService(private val everyoneRepository: EveryoneRepository) {
    @Value("\${file.upload-dir}")
    private lateinit var uploadDir: String

    fun uploadAvatar(username: String, file: MultipartFile): String? {

        val everyone = everyoneRepository.findByUsername(username)
            ?: throw MerchantNotFoundException("Merchant with username $username not found") // Merchant with given ID not found

        val fileName = "${System.currentTimeMillis()}_${file.originalFilename}"
        val filePath: Path = Paths.get(uploadDir + fileName)

        return try {
            Files.copy(file.inputStream, filePath)
            everyone.avatarUrl = filePath.toString()
            everyone.let{ everyoneRepository.save(it) }
            everyone.avatarUrl
        } catch (e: IOException) {
            e.printStackTrace()
            null // Error occurred during file upload
        }
    }
}