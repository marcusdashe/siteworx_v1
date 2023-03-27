package ng.siteworx.partner.payload.request

data class ProductRequest (
        var name: String? = null,

        var description: String? = null,

        var quantity: Double? = null,

        var price: Double? = null,

        var category: String? = null,

        var merchantIntention: String? = null,

        var photoPaths: List<String>? = null,

)