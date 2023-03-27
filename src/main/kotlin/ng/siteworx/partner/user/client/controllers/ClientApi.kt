package ng.siteworx.partner.user.client.controllers

import ng.siteworx.partner.user.client.services.ClientService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api_v1/client")
class ClientApi(private val clientService: ClientService) {


    @PreAuthorize("hasRole('ADMIN') or hasRole('CLIENT')")
    @GetMapping("/search/firstname")
    fun findClientByFirstName(
        @RequestParam firstName: String
        ): ResponseEntity<*> {
        return ResponseEntity.ok().body(this.clientService.findByFirstName(firstName))
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('CLIENT')")
    @GetMapping("/search/profession")
    fun findClientByProfession(
        @RequestParam profession: String
    ): ResponseEntity<*>{
        return ResponseEntity.ok().body(this.clientService.findByProfession(profession))
    }
}