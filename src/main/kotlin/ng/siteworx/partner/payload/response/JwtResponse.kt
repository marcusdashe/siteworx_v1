package ng.siteworx.partner.payload.response

class JwtResponse(
    val token:String,
    val id: Long,
    val username:String,
    val email:String,
    val roles:List<String>
)