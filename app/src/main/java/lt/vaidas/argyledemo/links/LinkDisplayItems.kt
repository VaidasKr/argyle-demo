package lt.vaidas.argyledemo.links

sealed interface LinkDisplayItems {
    fun onType(onItems: (List<LinkItem>) -> Unit, onPlaceholder: (Int) -> Unit)

    data class Placeholders(val size: Int) : LinkDisplayItems {
        override fun onType(onItems: (List<LinkItem>) -> Unit, onPlaceholder: (Int) -> Unit) {
            onPlaceholder(size)
        }
    }

    data class Items(val items: List<LinkItem>) : LinkDisplayItems {
        override fun onType(onItems: (List<LinkItem>) -> Unit, onPlaceholder: (Int) -> Unit) {
            onItems(items)
        }
    }
}
