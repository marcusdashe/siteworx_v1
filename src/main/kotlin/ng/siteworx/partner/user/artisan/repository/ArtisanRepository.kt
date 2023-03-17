package ng.siteworx.partner.user.artisan.repository

import ng.siteworx.partner.user.artisan.models.Artisan
import org.springframework.data.jpa.repository.JpaRepository

interface ArtisanRepository: JpaRepository<Artisan, Long> {
}