package com.familynotes.frame

import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.familynotes.frame.ui.theme.FamilyNotesTheme
import com.familynotes.frame.ui.webview.WebViewScreen
import com.familynotes.frame.ui.wifi.WifiPromptScreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainActivity : ComponentActivity() {
    private val connectivityManager by lazy {
        getSystemService(ConnectivityManager::class.java)
    }

    private val _isConnected = MutableStateFlow(false)
    val isConnected: StateFlow<Boolean> = _isConnected

    override fun onCreate(savedInstanceState: Bundle?) {
        // Install the splash screen - must be called before super.onCreate()
        installSplashScreen()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setupKioskMode()

        // Monitor connectivity
        registerNetworkCallback()
        checkCurrentConnection()

        setContent {
            FamilyNotesTheme {
                val connected by isConnected.collectAsState()

                if (connected) {
                    WebViewScreen(modifier = Modifier.fillMaxSize())
                } else {
                    WifiPromptScreen(
                        modifier = Modifier.fillMaxSize(),
                        onOpenWifiSettings = { openWifiSettings() }
                    )
                }
            }
        }
    }

    private fun registerNetworkCallback() {
        val request = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(
            request,
            object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    _isConnected.value = true
                }

                override fun onLost(network: Network) {
                    _isConnected.value = false
                }

                override fun onCapabilitiesChanged(
                    network: Network,
                    networkCapabilities: NetworkCapabilities
                ) {
                    val hasInternet = networkCapabilities.hasCapability(
                        NetworkCapabilities.NET_CAPABILITY_INTERNET
                    )
                    _isConnected.value = hasInternet
                }
            }
        )
    }

    private fun checkCurrentConnection() {
        val activeNetwork = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        val connected = capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true &&
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        _isConnected.value = connected
    }

    private fun openWifiSettings() {
        val intent =
            // Native floating WiFi panel (Android 10+)
            Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY)

        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

    /**
     * Locks the app in landscape, keeps the screen awake, draws edge‑to‑edge, and hides the system bars
     * in an immersive mode that users can reveal with a swipe.
     */
    private fun setupKioskMode() {
        window?.let { win ->
            // Keep the screen on for kiosk usage.
            win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

            // Tell the system we want to draw behind status & nav bars.
            WindowCompat.setDecorFitsSystemWindows(win, false)

            // Use the compatibility controller so this works back to API 21.
            val controller = WindowCompat.getInsetsController(win, win.decorView)
            controller.apply {
                hide(WindowInsetsCompat.Type.systemBars())
                systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }
    }
}
