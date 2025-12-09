package org.apps.jajanbarang.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.delay
import org.apps.jajanbarang.data.remote.SupabaseClientProvider
import org.apps.jajanbarang.ui.theme.BluePrimary
import org.apps.jajanbarang.ui.theme.poppinsFontFamily

@Composable
fun SplashScreen(navController: NavController){
    val auth = SupabaseClientProvider.client

    LaunchedEffect(Unit) {
        delay(1000)
        if (auth.auth.currentUserOrNull() != null) {
            navController.navigate("news") {
                popUpTo("splash") { inclusive = true }
            }
        } else {
            navController.navigate("login") {
                popUpTo("splash") { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize().background(Color.White),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "News App",
            fontSize = 32.sp,
            fontFamily = poppinsFontFamily,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = BluePrimary,
            modifier = Modifier.padding(top = 60.dp)
        )
    }
}
