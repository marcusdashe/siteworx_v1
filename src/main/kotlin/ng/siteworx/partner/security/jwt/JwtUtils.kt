package ng.siteworx.partner.security.jwt

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.SignatureException
import ng.siteworx.partner.security.service.UserDetailsImpl
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.lang.IllegalArgumentException
import java.util.*


@Component
class JwtUtils {

    companion object {
        private const val EXPIRE_DURATION: Long = 24 * 60 * 1000
        private val LOGGER = LoggerFactory.getLogger(JwtUtils::class.java)
    }

    @Value("\${app.jwt.secret}")
    private lateinit var secretKey: String

    fun generateJwtToken(authentication: Authentication): String {
        val userPrincipal: UserDetailsImpl = authentication.principal as UserDetailsImpl
        return Jwts.builder()
            .setSubject((userPrincipal.username))
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + EXPIRE_DURATION))
            .signWith(SignatureAlgorithm.HS512, secretKey)
            .compact();

    }

    fun getUserNameFromJwtToken(token: String): String{
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).body.subject
    }

    fun validateJwtToken(authToken: String): Boolean{
        try{
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(authToken);
            return true;
        }catch (e: SignatureException){
            val msg = e.message
            println("Invalid JWT signature: $msg" )
        } catch (e: MalformedJwtException){
            val msg = e.message
            println("Invalid JWT token: $msg")

        }catch (e: IllegalArgumentException){
            val msg = e.message
            println("JWT claims string is empty: $msg")
        }
        return false;
    }

}