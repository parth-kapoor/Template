package code_setup.app_models.request_

data class GetPriceListRequestModel(
    var end_loc: List<Double>,
    var start_loc: List<Double>
)