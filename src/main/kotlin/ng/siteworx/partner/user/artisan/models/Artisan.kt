package ng.siteworx.partner.user.artisan.models

import jakarta.persistence.*
import jakarta.validation.constraints.Size
import ng.siteworx.partner.enums.Constants
import ng.siteworx.partner.models.Everyone
import java.util.*


@Entity
@Table(name = "artisan")
class Artisan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(name="first_name", nullable = false)
    var firstName: String? = ""

    @Column(name="last_name", nullable = false)
    var lastName: String? = ""

    @Column(name = "availability", nullable = false)
    var isAvailable: Boolean = false

    @Column(name = "subscribe", nullable = false)
    var hasSubscribe: Boolean = false

    @Column(name = "subscription_level", nullable = false)
    @Enumerated(EnumType.STRING)
    var subscriptionLevel: Constants.SUBSCRIPTION_LEVEL = Constants.SUBSCRIPTION_LEVEL.FREE

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "everyone_id")
    var everyone: Everyone? = null

}