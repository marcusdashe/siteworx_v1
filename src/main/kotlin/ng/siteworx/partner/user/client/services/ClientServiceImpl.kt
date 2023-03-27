package ng.siteworx.partner.user.client.services

import ng.siteworx.partner.user.client.model.Client
import ng.siteworx.partner.user.client.repository.ClientRepository
import org.springframework.stereotype.Service

@Service
class ClientServiceImpl(private val clientRepository: ClientRepository): ClientService {
    override fun findByFirstName(firstName: String?): List<Client?>? {
        return clientRepository.findByFirstName(firstName)
    }

    override fun findByProfession(profession: String?): List<Client?>? {
        return clientRepository.findByProfession(profession)
    }

}