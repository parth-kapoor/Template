package code_setup.app_models.other_.event

/**
 * Created by harish on 31/1/19.
 */

class CustomEvent<T>(type: Int, oj: Any) {

    var type: Int = 0
        internal set

    var oj: Any
        internal set

    init {
        this.type = type
        this.oj = oj
    }
}