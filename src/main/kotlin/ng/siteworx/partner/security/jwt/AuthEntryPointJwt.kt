package ng.siteworx.partner.security.jwt

import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import java.io.IOException
import kotlin.jvm.Throws

@Component
class AuthEntryPointJwt: AuthenticationEntryPoint {
    @Throws(IOException::class, ServletException::class)
    override fun commence(request: HttpServletRequest?, response: HttpServletResponse?, authException: AuthenticationException?) {
//        if (request?.requestURI == "/api_v1/auth/signup" || request?.requestURI == "/api_v1/auth/signin") {
//            // Allow access to signup and signin routes
//            return
//        }
        //SC_UNAUTHORIZED is 401 status code
        println("Unauthorized error: ${authException?.message}")
        response?.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Error: Unauthorized")
    }

}