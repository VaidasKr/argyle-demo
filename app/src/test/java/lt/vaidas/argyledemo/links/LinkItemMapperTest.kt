package lt.vaidas.argyledemo.links

import android.content.res.Resources
import io.mockk.Called
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifySequence
import lt.vaidas.argyledemo.R
import org.junit.Assert
import org.junit.Test

class LinkItemMapperTest {
    private val resources = mockk<Resources>(relaxUnitFun = true) {
        every { getString(R.string.link_no_name) } returns "No name"
        every { getString(R.string.link_no_kind) } returns "None"
    }
    private val mapper = LinkItemMapper(resources)

    @Test
    fun `GIVEN response item has all fields WHEN responseToItem THEN item has same values`() {
        val response = LinkItemResponse(name = "Santa Inc", logo = "santa.png", kind = "Donation")
        val actual = mapper.responseToItem(response)

        val expected = LinkItem(name = "Santa Inc", logo = "santa.png", kind = "Donation")
        Assert.assertEquals(expected, actual)
        verify { resources wasNot Called }
    }

    @Test
    fun `GIVEN response name is null WHEN responseToItem THEN item name placeholder is taken from resources`() {
        val response = LinkItemResponse(name = null, logo = "incognito.png", kind = "Spy")
        val actual = mapper.responseToItem(response)

        val expected = LinkItem(name = "No name", logo = "incognito.png", kind = "Spy")
        Assert.assertEquals(expected, actual)
        verifySequence { resources.getString(R.string.link_no_name) }
    }

    @Test
    fun `GIVEN response logo is null WHEN responseToItem THEN item logo is empty`() {
        val response = LinkItemResponse(name = "Mafia", logo = null, kind = "Illegal activity")
        val actual = mapper.responseToItem(response)

        val expected = LinkItem(name = "Mafia", logo = "", kind = "Illegal activity")
        Assert.assertEquals(expected, actual)
        verify { resources wasNot Called }
    }

    @Test
    fun `GIVEN response kind is null WHEN responseToItem THEN item kind placeholder is taken from resources`() {
        val response = LinkItemResponse(name = "Lazy inc", logo = "Sloth.png", kind = null)
        val actual = mapper.responseToItem(response)

        val expected = LinkItem(name = "Lazy inc", logo = "Sloth.png", kind = "None")
        Assert.assertEquals(expected, actual)
        verifySequence { resources.getString(R.string.link_no_kind) }
    }
}
