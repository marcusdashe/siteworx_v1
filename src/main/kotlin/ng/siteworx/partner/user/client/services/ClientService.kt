package ng.siteworx.partner.user.client.services

import ng.siteworx.partner.user.client.model.Client

interface ClientService {
    fun findByFirstName(firstName: String?): List<Client?>?

    fun findByProfession(profession: String?): List<Client?>?
}