package ng.siteworx.partner.security.service

import ng.siteworx.partner.models.Everyone
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.stream.Collectors

class UserDetailsImpl: UserDetails {

    private var id: Long = 0;
    private lateinit var username:String;
    private lateinit var email: String;
    private lateinit var password: String;
    private lateinit var authorities: List<GrantedAuthority>

    constructor(
        id: Long,
        username: String, email: String, password: String, authorities: List<GrantedAuthority>?
    ){
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        if (authorities != null) {
            this.authorities = authorities
        };
    }
    companion object {
        fun build(everyone: Everyone): UserDetailsImpl{
            val authorities: List<SimpleGrantedAuthority>? = everyone.roles.stream()
                .map {role -> SimpleGrantedAuthority(role.name.name) }
                .collect(Collectors.toList());
            return UserDetailsImpl(
                everyone.id!!,
                everyone.username,
                everyone.email,
                everyone.password,
                authorities
            )
        }
    }

    override fun getAuthorities(): List<GrantedAuthority> {
        return authorities;
    }

    override fun getPassword(): String {
        return password;
    }

    override fun getUsername(): String {
        return username;
    }

    fun getEmail(): String{
        return email;
    }
    fun getId():Long{
        return id
    }

    override fun isAccountNonExpired(): Boolean {
        return true;
    }

    override fun isAccountNonLocked(): Boolean {
        return true;
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true;
    }

    override fun isEnabled(): Boolean {
        return true;
    }
}