package lt.vaidas.argyledemo.links

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LinksViewModel(
    private val useCase: LoadLinksUseCase,
    private val errorFactory: LinkLoadErrorFactory,
    private val searchThrottle: LinkSearchThrottle
) : ViewModel() {
    private val _state = MutableStateFlow(
        LinkState(displayItems = LinkDisplayItems.Placeholders(PLACEHOLDER_COUNT), isLoading = false, loadError = null)
    )
    val state: StateFlow<LinkState> = _state.asStateFlow()

    init {
        searchThrottle.postQuery(null)
        viewModelScope.launch {
            searchThrottle.requestFlow.collectLatest { query -> load(query) }
        }
    }

    private suspend fun load(query: String?) {
        _state.update { it.copy(isLoading = true, loadError = null) }
        runCatching { useCase.load(query) }
            .onFailure { cause ->
                // TODO log cause to timber
                if (cause !is CancellationException) {
                    val error = errorFactory.createFor(query)
                    _state.update { it.copy(isLoading = false, loadError = error) }
                }
            }.onSuccess { items ->
                _state.value = LinkState(LinkDisplayItems.Items(items), false, null)
            }
    }

    fun onQueryChange(query: String) {
        if (query.isEmpty()) {
            searchThrottle.postQuery(null)
            return
        }
        val trimmed = query.trim()
        if (trimmed.length >= 2) {
            searchThrottle.postQuery(trimmed)
        }
    }

    fun onRetry(loadError: LinkLoadError) {
        searchThrottle.postQuery(loadError.loadQuery)
    }

    companion object {
        private const val PLACEHOLDER_COUNT = 10
    }
}
