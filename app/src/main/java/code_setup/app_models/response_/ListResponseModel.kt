package code_setup.app_models.response_

import com.google.gson.annotations.SerializedName

data class ListResponseModel(@SerializedName("per_page")
                             val perPage: Int = 0,
                             @SerializedName("total")
                             val total: Int = 0,
                             @SerializedName("data")
                             val data: List<DataItem>?,
                             @SerializedName("page")
                             val page: Int = 0,
                             @SerializedName("total_pages")
                             val totalPages: Int = 0)