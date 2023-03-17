package ng.siteworx.partner.user.merchant.model

import jakarta.persistence.*
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

    @Column(name="created_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    var date: LocalDateTime? =  LocalDateTime.now()

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_id", nullable = false)
    var merchant: Merchant? = null

    @OneToMany(mappedBy = "product", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var orderItems: MutableList<OrderItem> = mutableListOf()
}