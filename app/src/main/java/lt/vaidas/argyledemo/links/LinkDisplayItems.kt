package lt.vaidas.argyledemo.links

sealed interface LinkDisplayItems {
    data class Placeholders(val size: Int) : LinkDisplayItems

    data class Items(val items: List<LinkItem>) : LinkDisplayItems

    data class Empty(val message: String) : LinkDisplayItems
}
