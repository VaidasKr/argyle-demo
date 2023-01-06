package lt.vaidas.argyledemo.network

import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.junit.Assert
import org.junit.Test

class AuthInterceptorTest {
    @Test
    fun `WHEN intercept THEN authorization header with credentials are added to request`() {
        val useCase = mockk<CredentialsUseCase>()
        every { useCase.credentials } returns "auth header"
        val chain = mockk<Interceptor.Chain>()
        every { chain.request() } returns Request.Builder().url("https://some-url.com").build()
        val response = mockk<Response>()
        val slot = slot<Request>()
        every { chain.proceed(capture(slot)) } returns response

        AuthInterceptor(useCase).intercept(chain)

        Assert.assertEquals(slot.captured.headers, Headers.Builder().add("Authorization", "auth header").build())
    }
}
