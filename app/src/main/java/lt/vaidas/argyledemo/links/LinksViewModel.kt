package lt.vaidas.argyledemo.links

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.update
import timber.log.Timber

@OptIn(ExperimentalCoroutinesApi::class)
class LinksViewModel(
    private val useCase: LoadLinksUseCase,
    private val errorFactory: LinkLoadErrorFactory,
    private val messageFactory: EmptyResultMessageFactory,
    private val searchThrottle: LinkSearchThrottle
) : ViewModel() {
    private val _state = MutableStateFlow(
        LinksState(itemResult = ItemResult.Placeholder(PLACEHOLDER_COUNT), isLoading = false, loadError = null)
    )
    val state: StateFlow<LinksState> = _state.asStateFlow()

    init {
        searchThrottle.requestFlow
            .mapLatest { load(it) }
            .buffer(0)
            .shareIn(viewModelScope, SharingStarted.Eagerly, 0)
        searchThrottle.postQuery(null)
    }

    private suspend fun load(query: String?) {
        _state.update { it.copy(isLoading = true, loadError = null) }
        runCatching { useCase.load(query) }
            .onFailure { cause ->
                if (cause !is CancellationException) {
                    Timber.e(cause)
                    val error = errorFactory.createFor(query)
                    _state.update { it.copy(isLoading = false, loadError = error) }
                }
            }.onSuccess { items ->
                if (items.isEmpty()) {
                    _state.value = LinksState(
                        itemResult = ItemResult.Empty(messageFactory.createFor(query)),
                        isLoading = false,
                        loadError = null
                    )
                } else {
                    _state.value = LinksState(itemResult = ItemResult.Items(items), isLoading = false, loadError = null)
                }
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
