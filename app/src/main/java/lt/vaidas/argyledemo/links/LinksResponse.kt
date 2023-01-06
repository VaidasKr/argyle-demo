package lt.vaidas.argyledemo.links

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LinksResponse(@SerialName("results") val results: List<LinkItemResponse>)
