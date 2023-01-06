package lt.vaidas.argyledemo.links

import android.content.res.Resources
import lt.vaidas.argyledemo.R

class EmptyResultMessageFactory(private val resources: Resources) {
    fun createFor(query: String?): String = query?.formatMessage() ?: resources.getString(R.string.link_empty)

    private fun String.formatMessage(): String = resources.getString(R.string.link_empty_with_query, this)
}
