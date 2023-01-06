package lt.vaidas.argyledemo.links

import android.content.res.Resources
import lt.vaidas.argyledemo.R

class LinkLoadErrorFactory(private val resources: Resources) {
    fun createFor(query: String?): LinkLoadError {
        val message = query?.formatMessage() ?: resources.getString(R.string.link_error)
        return LinkLoadError(message = message, loadQuery = query)
    }

    private fun String.formatMessage(): String = resources.getString(R.string.link_error_with_query, this)
}
