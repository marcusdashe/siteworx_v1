package ng.siteworx.partner.security.service

import ng.siteworx.partner.repository.EveryoneRepository
import ng.siteworx.partner.models.Everyone
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.jvm.Throws

@Service
class UserDetailsServiceImpl(val everyoneRepository: EveryoneRepository): UserDetailsService {
    @Transactional
    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        val everyone: Everyone = (everyoneRepository.findByUsername(username)
            ?:throw UsernameNotFoundException("User not Found with username: $username ")) as Everyone
        return UserDetailsImpl.build(everyone)
    }
}