package ng.siteworx.partner.user.merchant.model

import jakarta.persistence.*
import jakarta.validation.constraints.Size
import ng.siteworx.partner.enums.Constants
import ng.siteworx.partner.models.Everyone
import java.time.LocalDateTime

@Entity
@Table(name = "merchant")
class Merchant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

//    @Size(min = 2, max = 30, message = "Name must be between 2 and 30 characters")
    @Column(name="first_name", nullable = false)
    var firstName: String? = ""

//    @Size(min = 2, max = 30, message = "Name must be between 2 and 30 characters")
    @Column(name="last_name", nullable = false)
    var lastName: String? = ""

    @Column(name = "availability", nullable = false)
    var isAvailable: Boolean? = false

    @Column(name = "subscribe", nullable = false)
    var hasSubscribe: Boolean? = false

    @Column(name = "subscription_level", nullable = false)
    @Enumerated(EnumType.STRING)
    var subscriptionLevel: Constants.SUBSCRIPTION_LEVEL = Constants.SUBSCRIPTION_LEVEL.FREE

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "everyone_id")
    var everyone: Everyone? = null

    @OneToMany(mappedBy = "merchant", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var products: MutableList<Product> = mutableListOf()

//    @OneToMany(mappedBy = "merchant", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
//    var orders: MutableList<Order> = mutableListOf()
}
