package ng.siteworx.partner.enums

object Constants {

    enum class USER_TYPE {
        ARTISAN,
        MERCHANT,
        CLIENT,
        SUBCONTRACTOR,
        PROFESSIONAL,
        ADMIN,
        ANONYMOUS
    }

    enum class SUBSCRIPTION_LEVEL{
        FREE,
        TRIAL,
        PAID,
    }

    enum class PRODUCT_CATEGORY{
        BUILDING_MATERIALS,
        PLUMBING_SUPPLIES,
        ELECTRICAL_SUPPLIES,
        HVAC_SUPPLIES,
        TOOLS_EQUIPMENT,
        LANDSCAPING_SUPPLIES,
        HARDWARE_FASTENERS,
        PAINT_COATINGS,
        SAFETY_SECURITY,
        OTHERS,
    }

    enum class PURCHASE_LEASE{
        PURCHASE,
        LEASE
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