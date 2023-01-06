package lt.vaidas.argyledemo.links

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LinkItemResponse(
    @SerialName("name") val name: String?,
    @SerialName("logo_url") val logo: String?,
    @SerialName("kind") val kind: String?
)
