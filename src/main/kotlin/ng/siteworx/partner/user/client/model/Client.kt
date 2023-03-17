package ng.siteworx.partner.user.client.model

import jakarta.persistence.*
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Size


@Entity
@Table(name = "client")
class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Size(max = 30)
    @Size(min = 3, message = "Name must be between 3 and 30 characters")
    @Column(name="first_name", nullable = false)
    var firstName: String? = null

    @Size(max = 30)
    @Size(min = 3, message = "Name must be between 3 and 30 characters")
    @Column(name="last_name", nullable = false)
    var lastName: String? = null

    @Column(name="profession")
    var profession: String? = null
}