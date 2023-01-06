package lt.vaidas.argyledemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import lt.vaidas.argyledemo.links.compose.LinksScreen
import lt.vaidas.argyledemo.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                rememberSystemUiController().setStatusBarColor(MaterialTheme.colors.background, !isSystemInDarkTheme())
                Surface {
                    LinksScreen()
                }
            }
        }
    }
}
