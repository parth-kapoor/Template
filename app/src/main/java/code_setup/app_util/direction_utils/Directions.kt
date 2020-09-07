package code_setup.app_util.direction_utils

import io.reactivex.Single

interface Directions {
    fun getSuggestions(
            origin: Position,
            destination: Position,
            options: TransitOptions = TransitOptions()): Single<GeocodedResponse>
}