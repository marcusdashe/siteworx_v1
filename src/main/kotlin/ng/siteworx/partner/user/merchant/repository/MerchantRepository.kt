package ng.siteworx.partner.user.merchant.repository

import ng.siteworx.partner.enums.Constants
import ng.siteworx.partner.user.merchant.model.Merchant
import org.springframework.data.jpa.repository.JpaRepository

interface MerchantRepository: JpaRepository<Merchant, Long> {
    fun findByFirstNameAndLastName(firstName: String, lastName: String): Merchant?
    fun findByIsAvailable(isAvailable: Boolean): List<Merchant>
    fun findByHasSubscribe(hasSubscribe: Boolean): List<Merchant>
    fun findBySubscriptionLevel(subscriptionLevel: Constants.SUBSCRIPTION_LEVEL): List<Merchant>
    fun findByAvatarUrl(avatarUrl: String): List<Merchant>
}