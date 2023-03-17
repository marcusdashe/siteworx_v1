package ng.siteworx.partner

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication
class PartnerApplication

fun main(args: Array<String>) {
	runApplication<PartnerApplication>(*args)
}
