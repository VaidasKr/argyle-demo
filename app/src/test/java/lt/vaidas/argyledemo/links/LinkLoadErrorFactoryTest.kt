package lt.vaidas.argyledemo.links

import android.content.res.Resources
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifySequence
import lt.vaidas.argyledemo.R
import org.junit.Assert
import org.junit.Test

class LinkLoadErrorFactoryTest {
    private val resources = mockk<Resources>(relaxUnitFun = true) {
        every { getString(R.string.link_error) } returns "Failed"
        every { getString(R.string.link_error_with_query, any()) } answers {
            val formatArgs = invocation.args[1] as Array<Any>
            "Failed with ${formatArgs.first()}"
        }
    }
    private val factory = LinkLoadErrorFactory(resources)

    @Test
    fun `GIVEN query is null WHEN create THEN response has message with no query format`() {
        factory.createFor(null).assertEquals("Failed", null)

        verifySequence { resources.getString(R.string.link_error) }
    }

    @Test
    fun `GIVEN query is passed WHEN create THEn response has message with query`() {
        factory.createFor("test query").assertEquals("Failed with test query", "test query")

        verifySequence { resources.getString(R.string.link_error_with_query, "test query") }
    }

    private fun LinkLoadError.assertEquals(expectedMessage: String, expectedLoadQuery: String?) {
        Assert.assertEquals(expectedMessage, message)
        Assert.assertEquals(expectedLoadQuery, loadQuery)
    }
}
