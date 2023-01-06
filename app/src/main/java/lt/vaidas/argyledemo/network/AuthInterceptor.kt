package lt.vaidas.argyledemo.network

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val useCase: CredentialsUseCase) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response =
        chain.proceed(chain.request().newBuilder().addHeader("Authorization", useCase.credentials).build())
}
