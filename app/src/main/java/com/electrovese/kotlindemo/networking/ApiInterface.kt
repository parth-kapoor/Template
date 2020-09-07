package com.electrovese.kotlindemo.networking

import code_setup.app_models.request_.*
import code_setup.app_models.response_.*
import code_setup.net_.NetworkConstant
import code_setup.ui_.auth.models.request_model.RequestOTPModel
import code_setup.ui_.auth.models.request_model.RequestSocialLogin
import code_setup.ui_.auth.models.request_model.RequestVerifyOtp
import code_setup.ui_.auth.models.response_model.RequestOTPResponse
import code_setup.ui_.auth.models.response_model.StoreUserResponseModel
import code_setup.ui_.auth.models.response_model.VerifyOTPResponse
import code_setup.ui_.home.models.BookCabRequestModel
import code_setup.ui_.home.models.GooglePlaceDetailResponseModel
import code_setup.ui_.home.models.GooglePlaceResponseModel
import code_setup.ui_.home.models.PriceListResponseModel
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*


/**
 * Created by arischoice on 20/1/2019.
 */
interface ApiInterface {
    fun search(searchKey: String): Observable<BaseResponseModel>

    @POST(NetworkConstant.API_LOGIN)
    fun loginRequest(@Body loginRequestModel: LoginRequestModel): Observable<LoginResponseModel>

    @GET(NetworkConstant.API_PROFILE)
    fun getProfileDetail(@HeaderMap headreToken: HashMap<String, String>): Observable<ProfileResponseModel>

    @POST(NetworkConstant.API_CHANGE_STATUS)
    fun changeDriverStatus(@HeaderMap headreToken: HashMap<String, String>): Observable<UpdateStatusResponseModel>

    @GET(NetworkConstant.API_DRIVER_STATUS)
    abstract fun getStatusRequest(@HeaderMap commonHeaders: HashMap<String, String>): Observable<CurrentStatusResponseModel>

    @POST(NetworkConstant.API_CAPURE_INFO)
    fun captureInfo(@HeaderMap headreToken: HashMap<String, String>, @Body captureInfoModel: CaptureInfoModel): Observable<BaseResponseModel>

    @POST(NetworkConstant.API_ACCEPT_BOOKINGS)
    fun acceptTourRequest(
        @HeaderMap commonHeaders: HashMap<String, String>,
        @Body updateBookingRequestModel1: OrderDetailRequestModel
    ): Observable<BaseResponseModel>

    @POST(NetworkConstant.API_REJECT_BOOKINGS)
    fun rejectTourRequest(
        @HeaderMap commonHeaders: HashMap<String, String>,
        @Body updateBookingRequestModel1: OrderDetailRequestModel
    ): Observable<BaseResponseModel>


    @POST(NetworkConstant.API_ORDER_DETAIL)
    fun getOrderDetail(
        @HeaderMap commonHeaders: HashMap<String, String>,
        @Body updateBookingRequestModel: OrderDetailRequestModel
    ): Observable<OrderDetailResponseModel>


    @POST(NetworkConstant.API_LOGOUT_USER)
    fun logoutUser(@HeaderMap commonHeaders: HashMap<String, String>): Observable<BaseResponseModel>


    @Multipart
    @POST(NetworkConstant.API_UPDATE_PROFILE)
    fun updateProfileImage(
        @HeaderMap commonHeaders: HashMap<String, String>,
        @Part requiredData: MultipartBody.Part
    ): Observable<ProfileUpdateResponseModel>

    @POST(NetworkConstant.API_UPDATE_DRIVER_STATUS)
    fun requestUpdateDriverStatus(
        @HeaderMap commonHeaders: HashMap<String, String>,
        @Body updateOrderRequestModel: UpdateOrderRequestModel
    ): Observable<BaseResponseModel>

    @POST(NetworkConstant.API_OUT_FOR_ON_WAY)
    fun requestOutForandOnWay(
        @HeaderMap commonHeaders: HashMap<String, String>,
        @Body updateOrderRequestModel: UpdateOrderRequestModel
    ): Observable<BaseResponseModel>

    @POST(NetworkConstant.API_REACHED_DROP_LOCATION)
    fun requestReachedDropLocation(
        @HeaderMap commonHeaders: HashMap<String, String>,
        @Body updateOrderRequestModel: UpdateOrderRequestModel
    ): Observable<BaseResponseModel>


    @POST(NetworkConstant.API_ORDER_DELIVERED)
    fun requestOrderDelivered(
        @HeaderMap commonHeaders: HashMap<String, String>,
        @Body updateOrderRequestModel: UpdateOrderRequestModel
    ): Observable<BaseResponseModel>

    @POST(NetworkConstant.API_LOCATION_UPDATES)
    fun updateLocationRequest(
        @HeaderMap commonHeaders: HashMap<String, String>,
        @Body updateOrderRequestModel: UpdateOrderRequestModel
    ): Observable<BaseResponseModel>

    @POST(NetworkConstant.API_REQUEST_SUPPORT)
    fun requestSupport(
        @HeaderMap commonHeaders: HashMap<String, String>,
        @Body requiredData: RequestSupportModel
    ): Observable<BaseResponseModel>


    @POST(NetworkConstant.API_SOCIAL_LOGIN)
    fun requestSocialLogin(
        @HeaderMap commonHeaders: HashMap<String, String>,
        @Body requestSocialLogin: RequestSocialLogin
    ): Observable<StoreUserResponseModel>


    @POST(NetworkConstant.API_REQUEST_OTP)
    fun requestOTP(@Body requestOTPModel: RequestOTPModel): Observable<RequestOTPResponse>

    @POST(NetworkConstant.API_REQUEST_VERIFY_OTP)
    fun requestVerifyOTP(@Body requestVerifyOtp: RequestVerifyOtp): Observable<VerifyOTPResponse>

    @Multipart
    @POST(NetworkConstant.API_STORE_USER)
    fun requestStoreUser(
        @HeaderMap commonHeaders: HashMap<String, String>,
        @Part("email") emailBody: RequestBody,
        @Part("name") nameBody: RequestBody
    ): Observable<StoreUserResponseModel>


    @GET("api/place/autocomplete/json?")
    fun getplaceAutocompleteEmbbed(
        @Query("sessiontoken") sessiontoken: String,
        @Query("input") address: String,
        @Query("location") location: String,
        @Query("radius") radius: String,
        @Query("key") key: String

    ): Observable<GooglePlaceResponseModel>

    @GET("api/place/details/json?")
    fun getplaceDetail(
        @Query("sessiontoken") sessiontoken: String,
        @Query("placeid") placeId: String,
        @Query("key") s: String
    ): Observable<GooglePlaceDetailResponseModel>


    @POST(NetworkConstant.API_REQUEST_PRICE_LIST)
    fun requestPriceList(
        @HeaderMap commonHeaders: HashMap<String, String>,
        @Body priceListRequestModel: GetPriceListRequestModel
    ): Observable<PriceListResponseModel>

    @POST(NetworkConstant.API_REQUEST_BOOK_CAB)
    fun requestBookCab(
        @HeaderMap commonHeaders: HashMap<String, String>,
        @Body bookingRequestData: BookCabRequestModel
    ): Observable<BaseResponseModel>

}


