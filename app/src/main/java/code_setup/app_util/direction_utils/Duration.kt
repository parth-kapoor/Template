package code_setup.app_util.direction_utils

import com.google.android.gms.maps.model.LatLng


/**
 * Created by Harish on 4/3/2019.
 */
class Route {
    var distance: Distance? = null
    var duration: Duration? = null
    var endAddress: String? = null
    var endLocation: LatLng? = null
    var startAddress: String? = null
    var startLocation: LatLng? = null

    var points: List<LatLng>? = null
}