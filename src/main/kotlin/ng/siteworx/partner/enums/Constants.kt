package ng.siteworx.partner.enums

object Constants {


    enum class USER_TYPE {
        ARTISAN,
        MERCHANT,
        CLIENT,
        SUBCONTRACTOR,
        PROFESSIONAL,
        ADMIN
    }

    enum class SUBSCRIPTION_LEVEL{
        FREE,
        TRIAL,
        PAID,
    }

    enum class GENDER {
        MALE,
        FEMALE
    }

    enum class USER_STATUS {
        ACTIVE,
        INACTIVE
    }
    enum class ORDER_STATUS {
        NEW,
        PROCESSING,
        COMPLETED,
        CANCELLED,
        DELIVERED
    }
    enum class BANK_ACCOUNT_TYPE{
        SAVINGS,
        CURRENT,
        DOMINICIARY
    }
}