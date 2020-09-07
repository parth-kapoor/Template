package code_setup.app_models.response_

import com.google.gson.annotations.SerializedName

data class DataItem(@SerializedName("color")
                    val color: String = "",
                    @SerializedName("year")
                    val year: Int = 0,
                    @SerializedName("name")
                    val name: String = "",
                    @SerializedName("id")
                    val id: Int = 0,
                    @SerializedName("pantone_value")
                    val pantoneValue: String = "")