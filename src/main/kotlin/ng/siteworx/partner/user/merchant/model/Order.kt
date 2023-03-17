package ng.siteworx.partner.user.merchant.model

import jakarta.persistence.*
import ng.siteworx.partner.user.client.model.Client
import java.time.LocalDate
import java.time.LocalDateTime


@Entity
@Table(name = "order")
class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(name="created_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    var date: LocalDateTime? =  LocalDateTime.now()

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_id", nullable = false)
    var merchant: Merchant? = null

    @OneToMany(mappedBy = "order", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var orderItems: MutableList<OrderItem> = mutableListOf()


}