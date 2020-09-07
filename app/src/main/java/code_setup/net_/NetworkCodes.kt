package code_setup.net_

enum class NetworkCodes constructor(val nCodes: Int) {
    SUCCEES(1),
    SUCCEES_REGISTER_USER_NOW(2),
    FAIL(0),
    NETWORK_ERROR(502),
    SESSION(401)
}