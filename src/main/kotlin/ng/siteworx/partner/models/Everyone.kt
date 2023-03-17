package ng.siteworx.partner.models

import jakarta.persistence.*
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import ng.siteworx.partner.user.merchant.model.Merchant
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "everyone")
class Everyone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Email(message = "Please provide a valid email")
    @Column(name = "email", nullable = false, unique = true)
    var email: String = ""

    @NotBlank
    @Size(max = 30)
    @Size(min = 3, message = "Userame must be between 3 and 30 characters")
    @Column(name = "username", nullable = false, unique = true)
    var username: String = ""

    @Column(name = "password", nullable = false)
    var password: String = ""

    @Column(name = "verified", nullable = false)
    var isVerify: Boolean = false

    @Column(name = "verification", nullable=true)
    var verificationToken: String? = ""

    @Column(name="created_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    var created: LocalDateTime = LocalDateTime.now()


    @ManyToMany(fetch = FetchType.LAZY,cascade = [CascadeType.ALL])
    @JoinTable(
        name = "everyone_roles",
        joinColumns = [JoinColumn(name = "everyone_id")],
        inverseJoinColumns = [JoinColumn(name = "role_id")]
    )
    var roles: Set<Role> = HashSet()

    @OneToOne(mappedBy = "everyone", cascade = [CascadeType.ALL])
    var merchant: Merchant? = null

    @OneToOne(mappedBy = "bank", cascade = [CascadeType.ALL])
    var bankAccount: BankAccount? = null

//    @PrePersist
//    fun onCreate() {
//        if (merchant == null) {
//            merchant = Merchant()
//            merchant!!.everyone = this
//        }
//    }
}