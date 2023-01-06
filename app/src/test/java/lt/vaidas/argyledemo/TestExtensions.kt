package lt.vaidas.argyledemo

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Assert

fun <T> List<T>.assertLast(state: T, size: Int) {
    Assert.assertEquals(size, this.size)
    Assert.assertEquals(state, last())
}

@OptIn(ExperimentalCoroutinesApi::class)
suspend fun <T> Flow<T>.testFlow(scope: TestScope, itemAssertBlock: suspend (List<T>) -> Unit) {
    val values = mutableListOf<T>()
    val job = scope.launch(UnconfinedTestDispatcher(scope.testScheduler)) { toList(values) }
    itemAssertBlock(values)
    job.cancel()
}
