package code_setup.app_util

enum class VIEW_MODE constructor(val nCodes: Int) {
    ACTIVITY(0),
    FRAGMENT(1),
    BOTTOM_FRAGMENT(2),
    TOP_FRAGMENT(3),
    LIST_BOTTOM_FRAGMENT(4),
    LOADING(5),
}