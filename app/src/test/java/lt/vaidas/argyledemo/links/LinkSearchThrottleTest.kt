package lt.vaidas.argyledemo.links

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import lt.vaidas.argyledemo.assertLast
import lt.vaidas.argyledemo.testFlow
import org.junit.Assert
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LinkSearchThrottleTest {
    private val throttle = LinkSearchThrottle(100)

    @Test
    fun `GIVEN no query is running WHEN postQuery THEN query is posted`() = runTest {
        throttle.requestFlow.testFlow(this) { results ->
            Assert.assertTrue(results.isEmpty())

            throttle.postQuery(null)
            results.assertLast(null, 1)
        }
    }

    @Test
    fun `GIVEN query is posted after 50ms WHEN postQuery THEN query is posted after 100ms`() = runTest {
        throttle.requestFlow.testFlow(this) { results ->
            throttle.postQuery(null)

            advanceTimeBy(50)
            throttle.postQuery("query")
            results.assertLast(null, 1)

            advanceTimeBy(51)
            results.assertLast("query", 2)
        }
    }

    @Test
    fun `GIVEN multiple queries posted in 100ms window WHEN postQuery THEN latest query is posted after 100ms`() =
        runTest {
            throttle.requestFlow.testFlow(this) { results ->
                repeat(6) {
                    throttle.postQuery("query #$it")
                    advanceTimeBy(15)
                }
                results.assertLast("query #0", 1)

                advanceTimeBy(11)
                results.assertLast("query #5", 2)
            }
        }

    @Test
    fun `GIVEN query is posted after 100ms WHEN postQuery THEN query is posted instantly`() = runTest {
        throttle.requestFlow.testFlow(this) { results ->
            throttle.postQuery(null)

            advanceTimeBy(101)

            throttle.postQuery("query")
            results.assertLast("query", 2)
        }
    }
}
