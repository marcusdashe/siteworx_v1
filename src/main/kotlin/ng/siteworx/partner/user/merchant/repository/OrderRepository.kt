package ng.siteworx.partner.user.merchant.repository

import ng.siteworx.partner.user.merchant.model.Order
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
    interface OrderRepository : JpaRepository<Order, Long> {

        fun findByMerchantId(merchantId: Long): List<Order>

        fun findByClientId(clientId: Long): List<Order>

        fun findByDateBetween(startDate: LocalDateTime, endDate: LocalDateTime): List<Order>

        fun deleteByMerchantId(merchantId: Long)

    }
