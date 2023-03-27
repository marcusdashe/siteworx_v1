package ng.siteworx.partner.user.client.repository

import ng.siteworx.partner.user.client.model.Client
import org.springframework.data.jpa.repository.JpaRepository

interface ClientRepository: JpaRepository<Client, Long> {
    fun findByFirstName(firstName: String?): List<Client?>?

    fun findByProfession(profession: String?): List<Client?>?
}