package ng.siteworx.partner.models

import jakarta.persistence.*
import ng.siteworx.partner.enums.Constants

@Entity
@Table(name="roles")
class Role(
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    var name: Constants.USER_TYPE
){
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}