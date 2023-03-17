package ng.siteworx.partner.models

import jakarta.persistence.*
import ng.siteworx.partner.enums.Constants
import ng.siteworx.partner.user.artisan.models.Artisan
import ng.siteworx.partner.user.client.model.Client
import ng.siteworx.partner.user.merchant.model.Merchant


@Entity
@Table(name="bank_account_details")
class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(name = "bank_name")
    var bankName: String = ""

    @Column(name = "account_number", nullable = false, unique = true)
    var accountNumber: String = ""

    @Column(name = "account_name", nullable = false)
    var accountName: String = ""

    @Column(name = "account_type", nullable = false)
    @Enumerated(EnumType.STRING)
    var accountType: Constants.BANK_ACCOUNT_TYPE =  Constants.BANK_ACCOUNT_TYPE.SAVINGS

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "everyone_id", nullable = false)
    var everyone: Everyone? = null

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artisan_id")
    var artisan: Artisan? = null

    @OneToOne(fetch = FetchType.LAZY, optional= false)
    @JoinColumn(name = "client_id")
    private var client: Client? = null

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_id")
    var merchant: Merchant? = null
}