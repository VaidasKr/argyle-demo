package lt.vaidas.argyledemo.links

import android.content.res.Resources
import lt.vaidas.argyledemo.R

class LinkItemMapper(private val resources: Resources) {
    fun responseToItem(itemResponse: LinkItemResponse): LinkItem =
        LinkItem(
            name = itemResponse.name ?: resources.getString(R.string.link_no_name),
            logo = itemResponse.logo.orEmpty(),
            kind = itemResponse.kind ?: resources.getString(R.string.link_no_kind)
        )
}
