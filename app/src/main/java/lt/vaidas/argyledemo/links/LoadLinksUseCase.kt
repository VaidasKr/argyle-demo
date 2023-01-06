package lt.vaidas.argyledemo.links

class LoadLinksUseCase(private val service: LinksService, private val mapper: LinkItemMapper) {
    suspend fun load(query: String?): List<LinkItem> {
        val results = service.fetchLinks(DEFAULT_LIMIT, query).results
        if (results.isNullOrEmpty()) return emptyList()
        return results.map(mapper::responseToItem)
    }

    companion object {
        private const val DEFAULT_LIMIT = 15
    }
}
