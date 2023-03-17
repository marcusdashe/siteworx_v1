package ng.siteworx.partner.payload.request

class SignupRequest(
    val username:String,
    val email:String,
    val password:String,
    val roles: Set<String>
)