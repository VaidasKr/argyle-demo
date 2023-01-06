package lt.vaidas.argyledemo.links

data class LinksState(
    val itemResult: ItemResult,
    val isLoading: Boolean,
    val loadError: LinkLoadError?
)
