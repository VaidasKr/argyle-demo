package lt.vaidas.argyledemo.links

import android.content.res.Resources
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifySequence
import lt.vaidas.argyledemo.R
import org.junit.Assert
import org.junit.Test

class EmptyResultMessageFactoryTest {
    private val resources = mockk<Resources>(relaxUnitFun = true) {
        every { getString(R.string.link_empty) } returns "Not found"
        every { getString(R.string.link_empty_with_query, any()) } answers {
            val formatArgs = invocation.args[1] as Array<Any>
            "Not found for ${formatArgs.first()}"
        }
    }
    private val factory = EmptyResultMessageFactory(resources)

    @Test
    fun `GIVEN query is null WHEN createFor THEN response has message with no query format`() {
        Assert.assertEquals("Not found", factory.createFor(null))

        verifySequence { resources.getString(R.string.link_empty) }
    }

    @Test
    fun `GIVEN query is passed WHEN createFor THEN response has message with query`() {
        Assert.assertEquals("Not found for query of test", factory.createFor("query of test"))

        verifySequence { resources.getString(R.string.link_empty_with_query, "query of test") }
    }
}
