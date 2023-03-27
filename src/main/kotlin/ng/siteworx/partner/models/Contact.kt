package ng.siteworx.partner.models

import jakarta.persistence.*
import ng.siteworx.partner.user.artisan.models.Artisan
import ng.siteworx.partner.user.client.model.Client
import ng.siteworx.partner.user.merchant.model.Merchant


@Entity
@Table(name="contact_info")
class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(name="phone_number1", unique = true)
    var phoneNumber1: String? = null

    @Column(name="phone_number2", unique = true)
    var phoneNumber2: String? = null

    @Column(name="street_number")
    var streetNumber: String? = null

    @Column(name="zip_code")
    var zipCode: String? = null

    @Column(name="street_name")
    var streetName: String? = null

    @Column(name="city")
    var cityName: String? = null

    @Column(name="state")
    var stateName: String? = null

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "everyone_id")
    var everyone: Everyone? = null

//    @OneToOne(fetch = FetchType.LAZY, optional= false)
//    @JoinColumn(name = "artisan_id")
//    private var artisan: Artisan? = null
//
//    @OneToOne(fetch = FetchType.LAZY, optional= false)
//    @JoinColumn(name = "merchant_id")
//    private var merchant: Merchant? = null
//
//    @OneToOne(fetch = FetchType.LAZY, optional= false)
//    @JoinColumn(name = "client_id")
//    private var client: Client? = null

}