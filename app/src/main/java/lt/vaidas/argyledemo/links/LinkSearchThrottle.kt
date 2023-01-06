package lt.vaidas.argyledemo.links

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.transform

class LinkSearchThrottle(private val skipDuration: Long) {
    private val _innerQueryState =
        MutableSharedFlow<String?>(replay = 0, extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    val requestFlow: Flow<String?> = _innerQueryState
        .conflate()
        .transform {
            emit(it)
            delay(skipDuration)
        }

    fun postQuery(query: String?) {
        _innerQueryState.tryEmit(query)
    }
}
