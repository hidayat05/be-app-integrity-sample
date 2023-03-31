package appintegrity.maskipli.id.model.request

data class ValidateNonceRequest(
    val deviceId: String,
    val nonce: String
)
