package code_setup.app_util

import code_setup.app_models.other_.NotificationModel

class CommonValues {
    companion object {


        val APPLICATION_PLAYSTORE_URL_CUSTOMER =
            "https://play.google.com/store/apps/details?id=" + "com.electrovese.grocerydelivery"
        val NEW_JOB_MOTIFICATION_ID: Int = 111
        /*---------- NOTIFICATION MODEL --------*/
        val notificationModel = NotificationModel(
            "{\n" +
                    "  \"id\": \"5e57b8c7f1d16d2070abd8e0\",\n" +
                    "  \"type\": \"TOUR\",\n" +
                    "  \"user_id\": \"5e4118d74433d543983c63e0\",\n" +
                    "  \"driver_id\": \"5e394bb84d12413a4818c623\",\n" +
                    "  \"tour_id\": \"5e42897ab6900941dcede65b\",\n" +
                    "  \"created_at\": \"2020-02-11T11:01:14.663Z\",\n" +
                    "  \"pickup_loc\": \"Sector 8, Chandigarh, India\",\n" +
                    "  \"drop_off\": \"Industrial Area Phase I, Chandigarh, India\",\n" +
                    "  \"booking_date\": \"27-02-2020\",\n" +
                    "  \"destinations\": \"Sector 8, Chandigarh, India, Sector 26, Chandigarh, India, Industrial Area Phase I, Chandigarh, India\",\n" +
                    "  \"price\": 100,\n" +
                    "  \"user_name\": \"charanjeet singh`\",\n" +
                    "  \"rating\": 5,\n" +
                    "  \"expiration_date\": 1582807259121\n" +
                    "}",
            "",
            "1",
            "NEWTOURREQUEST",
            "NotificationModel",
            "",
            "NotificationModel"
        )


        val LONGITUDE: String = "elongitude"
        val LATITUDE: String = "elatitude"


        //Request Codes
        val REQUEST_CODE_PERMISSIONS_LOCATION: Int = 10001
        val REQUEST_CODE_PERMISSIONS_PHONE: Int = 10002
        val REQUEST_CODE_PERMISSIONS_CAMERA: Int = 10003
        val REQUEST_CODE_BACKGROUND_PERMISSIONS_LOCATION: Int = 10004
        val REQUEST_CODE_LOCATION_PICKER: Int=10005

        val CurrentMode: String = "currontMode"
        val APAPTER_BOTTOM_DIALOG_CLICK: Int = 436


        // Common Keys used
        val PHONE_NUMBER: String = "phoneNumber"
        val COUNTRY_CODE: String = "countryCode"
        val ACCESS_TOKEN: String = "accessToken"
        val USER_DATA: String = "userData"
        val IS_LOGEDIN: String = "isLogedin"
        val DAY_DETAIL: String = "dayDetail"
        val AVAIALBLE_SCANNED_VEHICLE_ID: String = "recentlyScannedVehicleId"
        val FCM_TOKEN: String = "fcmToken"
        val DEVICE_ID: String = "deviceId"
        val DEVICE_OS: String = "Android"
        val TOUR_ID: String = "tourID"
        val RIDER_PICKUP_DETAIL: String? = "riderPickup"
        val TOUR_DATA: String = "tourData"
        val RECENT_SUBSCRIBER_ID: String = "recentSubscriberId"
        val IS_FROM_NOTIFICATION: String = "isFromNotificationClick"
        val NOTIFICATION_ID: String = "notificationId"
        val IS_DRIVER_AVAILABLE: String = "isAvailable"
        val CURRENT_ORDER_ID: String = "currentOrderId"

        val SOCIAL_DATA_USER: String = "userSocialData"
        val IS_SOCIAL_LOGIN: String = "isSocialLogin"
        val SCHEDULED_DATE: String="scheduledDate"


        val SCREEN_TYPE: String = "screenType"
        val TERMS: String = "terms"
        val POLICY: String = "policy"
        val REFUND: String = "refund"

        val NORMAL_BOOKING_TYPE: String="normalBookingType"
        val SHARE_BOOKING_TYPE: String="sharedBookingType"


        //  adapter and dialog clicks-----------------
        val TOUR_PEOPLE_COUNT_CLICKED: Int = 1 * 10
        val TOUR_NAME_CLICKED: Int = 1 * 20
        val TOUR_DESTINATION_ARRIVED: Int = 1 * 30
        val TOUR_PICKUP_RIDER_CLICK: Int = 1 * 40
        val CALL_CLICK: Int = 1 * 41
        val MESSAGE_CLICK: Int = 1 * 42
        val REJECT: Int = 2 * 42

        ///*   driver ststus*/

        val RIDER_STATUS_ASSIGNED: String = "ASSIGNED"
        val RIDER_STATUS_ON_PICKUP_LOCATION: String = "ON_PICKUP_LOCATION"
        val RIDER_STATUS_OUT_FOR_DELIVERY: String = "OUT_FOR_DELIVERY"
        val RIDER_STATUS_ON_DELIVERY_LOCATION: String = "ON_DROP_LOCATION"
        val RIDER_STATUS_DELIVERED: String = "DELIVERED"

        val CURRENT_ADDRESS: String="currentAddress"


    }
}