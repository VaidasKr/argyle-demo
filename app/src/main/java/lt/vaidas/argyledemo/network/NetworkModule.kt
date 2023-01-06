package lt.vaidas.argyledemo.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import lt.vaidas.argyledemo.links.LinksService
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.create
import timber.log.Timber

private val json = Json { ignoreUnknownKeys = true }

@OptIn(ExperimentalSerializationApi::class)
val networkModule = module {
    single {
        val authInterceptor =
            AuthInterceptor(CredentialsUseCase("9b40eed7b1d14f16ba3abfad216167e8", "kXatSEqBrGIaHeCp"))
        val loggingInterceptor = HttpLoggingInterceptor { Timber.d(it) }
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC
        val client = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
        Retrofit.Builder()
            .client(client)
            .baseUrl("https://api-sandbox.argyle.com/v1/")
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }
    factory { get<Retrofit>().create<LinksService>() }
}
