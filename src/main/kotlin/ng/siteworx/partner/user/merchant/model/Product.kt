package ng.siteworx.partner.user.merchant.model

import jakarta.persistence.*
import ng.siteworx.partner.enums.Constants
import java.time.LocalDateTime

@Entity
@Table(name = "product")
class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(name= "product_name", nullable = false)
    var name: String? = null

    @Column(name= "product_description", nullable = false)
    var description: String? = null

    @Column(name= "product_quantity",nullable = false)
    var quantity: Double? = null

    @Column(nullable = false)
    var price: Double? = null

    @Column(name="product_category")
    var category: Constants.PRODUCT_CATEGORY = Constants.PRODUCT_CATEGORY.OTHERS

    @Column(name="merchant_intentions")
    var merchantIntentions: Constants.PURCHASE_LEASE = Constants.PURCHASE_LEASE.PURCHASE

    @Column(name="created_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    var date: LocalDateTime? =  LocalDateTime.now()

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "product_photo", joinColumns = [JoinColumn(name = "product_id")])
    @Column(name = "photo_path", nullable = false)
    var photoPaths: List<String> = emptyList()

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_id", nullable = false)
    var merchant: Merchant? = null

    @OneToMany(mappedBy = "product", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var orderItems: MutableList<OrderItem> = mutableListOf()
}