package lt.vaidas.argyledemo.network

import okhttp3.Credentials

class CredentialsUseCase(username: String, password: String) {
    val credentials: String = Credentials.basic(username, password)
}
