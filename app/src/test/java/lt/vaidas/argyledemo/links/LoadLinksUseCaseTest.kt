package lt.vaidas.argyledemo.links

import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import java.io.IOException

class LoadLinksUseCaseTest {
    private val service = mockk<LinksService>()
    private val mapper = mockk<LinkItemMapper>()
    private val useCase = LoadLinksUseCase(service, mapper)

    @Test
    fun `GIVEN service returns empty response WHEN load THEN empty list is returned`() = runBlocking {
        coEvery { service.fetchLinks(15, null) } returns LinksResponse(emptyList())

        val actual = useCase.load(null)

        Assert.assertEquals(emptyList<LinkItem>(), actual)
    }

    @Test
    fun `GIVEN service returns response with null results WHEN load THEN empty list is returned`() = runBlocking {
        coEvery { service.fetchLinks(15, null) } returns LinksResponse(null)

        val actual = useCase.load(null)

        Assert.assertEquals(emptyList<LinkItem>(), actual)
    }

    @Test
    fun `GIVEN service fails WHEN load THEN exception is passed`() = runBlocking {
        val expectedException = IOException()
        coEvery { service.fetchLinks(15, null) } throws expectedException

        val actualException = runCatching { useCase.load(null) }.exceptionOrNull()!!

        Assert.assertEquals(expectedException, actualException)
    }

    @Test
    fun `GIVEN service returns results WHEN load THEN mapped results are returned`() = runBlocking {
        val responseItem1 = mockk<LinkItemResponse>()
        val responseItem2 = mockk<LinkItemResponse>()
        coEvery { service.fetchLinks(15, null) } returns LinksResponse(listOf(responseItem1, responseItem2))
        val linkItem1 = mockk<LinkItem>()
        val linkItem2 = mockk<LinkItem>()
        every { mapper.responseToItem(responseItem1) } returns linkItem1
        every { mapper.responseToItem(responseItem2) } returns linkItem2

        val actual = useCase.load(null)

        Assert.assertEquals(listOf(linkItem1, linkItem2), actual)
    }

    @Test
    fun `GIVEN query is not null WHEN load THEN links are fetched with query`() = runBlocking {
        val responseItem = mockk<LinkItemResponse>()
        coEvery { service.fetchLinks(15, "some query") } returns LinksResponse(listOf(responseItem))
        val linkItem = mockk<LinkItem>()
        every { mapper.responseToItem(responseItem) } returns linkItem

        val actual = useCase.load("some query")

        Assert.assertEquals(listOf(linkItem), actual)
    }
}
