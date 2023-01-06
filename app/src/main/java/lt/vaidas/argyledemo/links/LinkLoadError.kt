package lt.vaidas.argyledemo.links

// Not data class so every instance would be distinct
class LinkLoadError(val message: String, val loadQuery: String?)
