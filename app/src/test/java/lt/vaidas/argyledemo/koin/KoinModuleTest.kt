package lt.vaidas.argyledemo.koin

import android.content.Context
import io.mockk.every
import io.mockk.mockk
import lt.vaidas.argyledemo.MainDispatcherRule
import lt.vaidas.argyledemo.links.linkModule
import lt.vaidas.argyledemo.network.networkModule
import org.junit.Rule
import org.junit.Test
import org.koin.android.ext.koin.androidContext
import org.koin.test.KoinTest
import org.koin.test.check.checkKoinModules

class KoinModuleTest : KoinTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `verify koin modules`() {
        checkKoinModules(listOf(networkModule, linkModule), appDeclaration = {
            val androidContext = mockk<Context>(relaxUnitFun = true) {
                every { resources } returns mockk()
            }
            androidContext(androidContext)
        })
    }
}
