package ng.siteworx.partner.repository

import ng.siteworx.partner.models.Everyone
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface EveryoneRepository : JpaRepository<Everyone, Long> {
    fun findByUsername(username: String): Everyone?
    fun existsByUsername(username:String): Boolean;
    fun existsByEmail(username: String): Boolean;
//    fun findByVerificationTokenNotNullAndCreatedDateBefore(date: LocalDateTime): List<Everyone>
//    fun findByVerificationToken(token: String): Everyone?

    fun findByVerificationTokenNotNullAndCreatedBefore(date: LocalDateTime): List<Everyone>
    fun findByVerificationToken(token: String): Everyone?
}