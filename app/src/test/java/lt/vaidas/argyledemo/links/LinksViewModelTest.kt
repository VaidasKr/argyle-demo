package lt.vaidas.argyledemo.links

import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifySequence
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import lt.vaidas.argyledemo.assertLast
import lt.vaidas.argyledemo.testFlow
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class LinksViewModelTest {
    private val useCase = mockk<LoadLinksUseCase>()
    private val errorFactory = mockk<LinkLoadErrorFactory>()
    private val throttleFlow = MutableSharedFlow<String?>()
    private val searchThrottle = mockk<LinkSearchThrottle>(relaxUnitFun = true) {
        every { requestFlow } returns throttleFlow
    }
    private val viewModel: LinksViewModel get() = LinksViewModel(useCase, errorFactory, searchThrottle)

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `WHEN init THEN state has placeholder items`() = runTest {
        viewModel.state.testFlow(this) { states ->
            states.assertLast(LinkState(LinkDisplayItems.Placeholders(10), false, null), 1)
        }
    }

    @Test
    fun `WHEN init THEN null query is posted to search throttle`() {
        viewModel

        verifySequence {
            searchThrottle.postQuery(null)
            searchThrottle.requestFlow
        }
    }

    @Test
    fun `GIVEN queries posted in requestFlow AND use case successful WHEN init THEN items are loaded in use case`() =
        runTest {
            val deferred = CompletableDeferred<List<LinkItem>>()
            coEvery { useCase.load("query") } coAnswers { deferred.await() }
            viewModel.state.testFlow(this) { states ->
                throttleFlow.emit("query")

                states.assertLast(LinkState(LinkDisplayItems.Placeholders(10), isLoading = true, loadError = null), 2)
                val value = mockk<List<LinkItem>>()
                deferred.complete(value)

                states.assertLast(LinkState(LinkDisplayItems.Items(value), isLoading = false, loadError = null), 3)
            }
        }

    @Test
    fun `GIVEN queries posted in requestFlow AND use case fails WHEN init THEN loadError is added to state`() =
        runTest {
            val deferred = CompletableDeferred<List<LinkItem>>()
            coEvery { useCase.load("query") } coAnswers { deferred.await() }
            val loadError = LinkLoadError("message", "query")
            every { errorFactory.createFor("query") } returns loadError
            viewModel.state.testFlow(this) { states ->
                throttleFlow.emit("query")

                states.assertLast(LinkState(LinkDisplayItems.Placeholders(10), isLoading = true, loadError = null), 2)
                val exception = IOException()
                deferred.completeExceptionally(exception)

                states.assertLast(
                    LinkState(LinkDisplayItems.Placeholders(10), isLoading = false, loadError = loadError),
                    3
                )
            }
        }

    @Test
    fun `GIVEN query is posted while previous query did not finish WHEN init THEN first loading is cancelled`() {
        runTest {
            val deferredFirst = CompletableDeferred<List<LinkItem>>()
            val deferredSecond = CompletableDeferred<List<LinkItem>>()
            coEvery { useCase.load("query1") } coAnswers { deferredFirst.await() }
            coEvery { useCase.load("query2") } coAnswers { deferredSecond.await() }
            viewModel.state.testFlow(this) { states ->
                throttleFlow.emit("query1")
                states.assertLast(LinkState(LinkDisplayItems.Placeholders(10), isLoading = true, loadError = null), 2)

                throttleFlow.emit("query2")
                states.assertLast(LinkState(LinkDisplayItems.Placeholders(10), isLoading = true, loadError = null), 2)

                println("complete first")
                val itemsForQuery1 = mockk<List<LinkItem>>()
                deferredFirst.complete(itemsForQuery1)
                states.assertLast(LinkState(LinkDisplayItems.Placeholders(10), isLoading = true, loadError = null), 2)

                println("complete second")
                val itemsForQuery2 = mockk<List<LinkItem>>()
                deferredSecond.complete(itemsForQuery2)

                states.assertLast(
                    LinkState(LinkDisplayItems.Items(itemsForQuery2), isLoading = false, loadError = null),
                    3
                )
            }
        }
    }

    @Test
    fun `GIVEN query is empty WHEN onQueryChange THEN null query is posted`() {
        viewModel.onQueryChange("")

        verifySequenceAfterInit {
            searchThrottle.postQuery(null)
        }
    }

    @Test
    fun `GIVEN query is white space only WHEN onQueryChanged THEN no query is posted`() {
        viewModel.onQueryChange("    ")

        verifySequence {
            searchThrottle.postQuery(null)
            searchThrottle.requestFlow
        }
    }

    @Test
    fun `GIVEN query is single char WHEN onQueryChanged THEN no query is posted`() {
        viewModel.onQueryChange("1")

        verifySequence {
            searchThrottle.postQuery(null)
            searchThrottle.requestFlow
        }
    }

    @Test
    fun `GIVEN query has single non space char WHEN onQueryChanged THEN no query is posted`() {
        viewModel.onQueryChange(" 1 ")

        verifySequence {
            searchThrottle.postQuery(null)
            searchThrottle.requestFlow
        }
    }

    @Test
    fun `GIVEN query is 2 chars WHEN onQueryChanged THEN query is posted`() {
        viewModel.onQueryChange("aa")

        verifySequenceAfterInit {
            searchThrottle.postQuery("aa")
        }
    }

    @Test
    fun `GIVEN query has space padding WHEN onQueryChanged THEN trimmed query is posted`() {
        viewModel.onQueryChange("  queryyy ")

        verifySequenceAfterInit {
            searchThrottle.postQuery("queryyy")
        }
    }

    @Test
    fun `WHEN onRetry THEN query from loadError is posted`() {
        viewModel.onRetry(LinkLoadError("some message", "failed query"))

        verifySequenceAfterInit {
            searchThrottle.postQuery("failed query")
        }
    }

    private fun verifySequenceAfterInit(block: () -> Unit) {
        verifySequence {
            searchThrottle.postQuery(null)
            searchThrottle.requestFlow
            block()
        }
    }
}
