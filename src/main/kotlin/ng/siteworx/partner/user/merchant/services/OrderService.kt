package ng.siteworx.partner.user.merchant.services

import ng.siteworx.partner.user.client.repository.ClientRepository
import ng.siteworx.partner.user.merchant.model.Order
import ng.siteworx.partner.user.merchant.model.OrderItem
import ng.siteworx.partner.user.merchant.repository.MerchantRepository
import ng.siteworx.partner.user.merchant.repository.OrderRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime


@Service
class OrderService(
    private val orderRepository: OrderRepository,
    private val clientRepository: ClientRepository,
    private val merchantRepository: MerchantRepository
) {

    fun placeOrder(clientId: Long, merchantId: Long, orderItems: List<OrderItem>): Order {
        val client = clientRepository.findById(clientId)
            .orElseThrow { IllegalArgumentException("Invalid client ID") }
        val merchant = merchantRepository.findById(merchantId)
            .orElseThrow { IllegalArgumentException("Invalid merchant ID") }

        val order = Order()
        order.merchant = merchant
        order.client = client
        order.date = LocalDateTime.now()

        for (orderItem in orderItems) {
            orderItem.order = order
            order.orderItems.add(orderItem)
        }
        return orderRepository.save(order)
    }
}