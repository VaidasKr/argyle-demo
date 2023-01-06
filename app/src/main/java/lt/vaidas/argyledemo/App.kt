package lt.vaidas.argyledemo

import android.app.Application
import lt.vaidas.argyledemo.network.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)

            modules(networkModule)
        }
    }
}
