package code_setup.app_util.socket_work.model_

/*

* {"message":{"action_code":"NEW_TOUR_JOB","id":"5e687ba096dea003704d69bd","tour_id":"5e394ddc4d12413a4818c62f","title":"NEW TOUR REQUEST",
* "body":"NEW TOUR REQUEST","data":"{\"id\":\"5e687ba096dea003704d69bd\",\"type\":\"TOUR\",\"user_id\":\"5e4118d74433d543983c63e0\",\"driver_id\":\"5e394bb84d12413a4818c623\",\"tour_id\":\"5e394ddc4d12413a4818c62f\",\"created_at\":\"2020-02-04T10:56:28.348Z\",\"pickup_loc\":\"Sector 8, Chandigarh, India\",\"drop_off\":\"Sector 26, Chandigarh, India\",\"booking_date\":\"11-03-2020\",\"destinations\":\"Sector 8, Chandigarh, India, Sector 26, Chandigarh, India\",\"price\":1000,\"user_name\":\"charanjeet singh`\",\"rating\":5,\"expiration_date\":1583905716984}"
* ,"channel":"NEW_TOUR_JOB","user_id":"5e394bb84d12413a4818c623"}}
*
* */
data class SocketUpdateModel(
    var message: Message
) {
    data class Message(
        var action_code: String,
        var body: String,
        var channel: String,
        var data: String,
        var id: String,
        var title: String,
        var tour_id: String,
        var user_id: String
    )
}