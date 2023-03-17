package ng.siteworx.partner.exceptions

import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.time.LocalDateTime

/*
* @author Marcus Dashe <marcusdashe.developer@gmail.com>
* */
@ControllerAdvice
class BaseExceptionHandler {


    @ExceptionHandler(MerchantNotFoundException::class)
    fun handleMerchantNotFoundException(
        ex: MerchantNotFoundException,
        request: HttpServletRequest
    ): ResponseEntity<ApiError> {
        val error = ApiError(HttpStatus.NOT_FOUND.value(), ex.message, request.requestURI)
        return ResponseEntity(error, HttpStatus.NOT_FOUND)
    }
}

data class ApiError(
    val status: Int,
    val message: String?,
    val requestedUri: String,
    val timestamp: LocalDateTime = LocalDateTime.now(),
)
