package lt.vaidas.argyledemo.links

sealed interface ItemResult {
    data class Items(val items: List<LinkItem>) : ItemResult
    data class Placeholder(val size: Int) : ItemResult
    data class Empty(val message: String) : ItemResult
}
