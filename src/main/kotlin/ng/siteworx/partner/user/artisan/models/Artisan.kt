package ng.siteworx.partner.user.artisan.models

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import ng.siteworx.partner.enums.Constants
import ng.siteworx.partner.models.BankAccount
import ng.siteworx.partner.models.Contact
import ng.siteworx.partner.models.Role
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.util.*
import kotlin.collections.HashSet


@Entity
@Table(name = "artisan")
class Artisan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Size(max = 30)
    @Size(min = 3, message = "Name must be between 3 and 30 characters")
    @Column(name="first_name", nullable = false)
    var firstName: String = ""

    @Size(max = 30)
    @Size(min = 3, message = "Name must be between 3 and 30 characters")
    @Column(name="last_name", nullable = false)
    var lastName: String = ""

    @Column(name = "availability", nullable = false)
    var isAvailable: Boolean = false

    @Column(name = "subscribe", nullable = false)
    var hasSubscribe: Boolean = false

    @Column(name = "subscription_level", nullable = false)
    @Enumerated(EnumType.STRING)
    var subscriptionLevel: Constants.SUBSCRIPTION_LEVEL = Constants.SUBSCRIPTION_LEVEL.FREE

    @OneToOne(mappedBy = "artisan", fetch = FetchType.LAZY, cascade = arrayOf(CascadeType.ALL))
    private var contact: Contact? = null

    @OneToOne(mappedBy = "artisan", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    var account: BankAccount? = null

}