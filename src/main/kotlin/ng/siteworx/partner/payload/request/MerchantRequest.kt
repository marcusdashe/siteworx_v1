package ng.siteworx.partner.payload.request

import jakarta.persistence.Column
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.validation.constraints.Size
import ng.siteworx.partner.enums.Constants

data class MerchantRequest(
    var firstName: String? = null,

    var lastName: String? = null,

    var isAvailable: Boolean? = null,

    var hasSubscribe: Boolean? = null,

    var subscriptionLevel: String? = null,
)