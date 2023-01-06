package lt.vaidas.argyledemo.network

import org.junit.Assert
import org.junit.Test

class CredentialsUseCaseTest {
    @Test
    fun `WHEN credentials THEN username and password are joined base64 encoded`() {
        val actual = CredentialsUseCase("username", "password").credentials

        Assert.assertEquals("Basic dXNlcm5hbWU6cGFzc3dvcmQ=", actual)
    }
}
