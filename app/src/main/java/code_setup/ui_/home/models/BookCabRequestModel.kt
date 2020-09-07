package code_setup.ui_.home.models

data class BookCabRequestModel(
    var end_address: String,
    var end_loc: List<Double>,
    var start_address: String,
    var start_loc: List<Double>,
    var vehicle_category: String
)