package code_setup.net_

class NetworkConstant {

    companion object {

        // Api request code--------------------------------------
        val LOGIN_CODE: Int = 122
        val LIST_CODE: Int = 123


        // Api url--------------------------------------
        enum class Environment private constructor(val url: String) {
            GOOGLE("https://maps.googleapis.com/maps/"),
            PROD("https://prod.domain.com:1088/"),
            TEST(""),//socket
            LOCAL_DIRECT("http://3f6aca6a.ngrok.io/"),
            DEV("http://91.205.173.97:2005/"),//development
            LOCAL("http://192.168.1.21:3535/")//local
        }

        val BASE_URL: String = Environment.DEV.url
        val BASE_URL_SOCKET: String = Environment.DEV.url
        val TOKEN: String? = "token"


        // Api response code--------------------------------------
        val SUCCESS: Int = 1
        val FAIL: Int = 0


        // Api methods names----------------------------------------------
        const val API_LOGIN: String = "api/driver/login"
        const val API_PROFILE: String = "api/driver/profile"
        const val API_CHANGE_STATUS: String = "api/driver/change/status"
        const val API_CAPURE_INFO: String = "api/driver/captureinfo"
        const val API_DRIVER_STATUS: String = "api/driver/current/status"
        const val API_ACCEPT_BOOKINGS: String = "api/driver/orders/accept"
        const val API_REJECT_BOOKINGS: String = "api/driver/orders/reject"
        const val API_ORDER_DETAIL: String = "api/driver/orders/detail"
        const val API_LOGOUT_USER: String = "api/driver/logout"
        const val API_UPDATE_PROFILE: String = "api/driver/update/profile"

        const val API_UPDATE_DRIVER_STATUS: String = "api/driver/orders/onlocation"
        const val API_OUT_FOR_ON_WAY: String = "api/driver/orders/onway"
        const val API_REACHED_DROP_LOCATION: String = "api/driver/orders/arrived"
        const val API_ORDER_DELIVERED: String = "api/driver/orders/delivered"
        const val API_LOCATION_UPDATES: String = "api/driver/orders/update/location"
        const val API_REQUEST_SUPPORT: String = ""


        const val API_SOCIAL_LOGIN="api/auth/social/login"
        const val API_REQUEST_OTP: String = "api/auth/request/otp"
        const val API_REQUEST_VERIFY_OTP: String = "api/auth/verify/otp"
        const val API_STORE_USER: String = "api/auth/store/user"
        const val API_REQUEST_PRICE_LIST: String = "api/pricing"
        const val API_REQUEST_BOOK_CAB: String = "api/book/ride"



    }
}