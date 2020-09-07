package code_setup.app_util.direction_utils


import com.google.gson.annotations.SerializedName

data class Step(
        @SerializedName("start_location")
        val start: Position,
        @SerializedName("end_location")
        val end: Position,
        val polyline: Poly)