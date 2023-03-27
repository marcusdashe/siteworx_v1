package ng.siteworx.partner.user.merchant.repository

import ng.siteworx.partner.user.merchant.model.Order
import ng.siteworx.partner.user.merchant.model.OrderItem
import ng.siteworx.partner.user.merchant.model.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
    interface OrderItemRepository : JpaRepository<OrderItem, Long> {
        // Define additional repository functions here, if needed
        fun findAllByOrder(order: Order): List<OrderItem>

        fun findAllByProduct(product: Product): List<OrderItem>


}
