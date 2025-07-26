package com.example.shinesale.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shinesale.MainActivity
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin

class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enable edge-to-edge display
        enableEdgeToEdge()

        setContent {
            ShineSaleTheme {
                SplashScreen {
                    // Navigate to MainActivity (or your main activity)
                    navigateToMain()
                }
            }
        }
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}

@Composable
fun SplashScreen(onNavigate: () -> Unit = {}) {
    val context = LocalContext.current

    // Animation states
    var startAnimation by remember { mutableStateOf(false) }

    // Logo scale animation
    val logoScale by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.3f,
        animationSpec = tween(
            durationMillis = 1000,
            easing = FastOutSlowInEasing
        )
    )

    // Logo alpha animation
    val logoAlpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 800,
            delayMillis = 200
        )
    )

    // Text alpha animation
    val textAlpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 800,
            delayMillis = 600
        )
    )

    // Rotating shine effect
    val infiniteTransition = rememberInfiniteTransition()
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    // Pulsing effect
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    // Start animation on composition
    LaunchedEffect(Unit) {
        startAnimation = true
        delay(3000) // 3 seconds delay
        onNavigate()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF6200EA),
                        Color(0xFF3700B3),
                        Color(0xFF1A0033)
                    ),
                    radius = 1000f
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // Animated background particles
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            drawAnimatedParticles(rotation)
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Main logo container with shine effect
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .scale(logoScale * pulseScale)
                    .alpha(logoAlpha),
                contentAlignment = Alignment.Center
            ) {
                // Shine effect background
                Canvas(
                    modifier = Modifier.fillMaxSize()
                ) {
                    rotate(rotation) {
                        drawShineEffect()
                    }
                }

                // Logo icon (using a shopping bag shape)
                Card(
                    modifier = Modifier.size(80.dp),
                    shape = CircleShape,
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 8.dp
                    )
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(
                            modifier = Modifier.size(40.dp)
                        ) {
                            drawShoppingBag()
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // App name
            Text(
                text = "ShineSale",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.alpha(textAlpha),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Tagline
            Text(
                text = "Shop Bright, Shop Right",
                fontSize = 16.sp,
                fontWeight = FontWeight.Light,
                color = Color.White.copy(alpha = 0.8f),
                modifier = Modifier.alpha(textAlpha),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(64.dp))

            // Loading indicator
            if (startAnimation) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(24.dp)
                        .alpha(textAlpha),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            }
        }
    }
}

fun DrawScope.drawAnimatedParticles(rotation: Float) {
    val particleCount = 20
    val centerX = size.width / 2
    val centerY = size.height / 2

    for (i in 0 until particleCount) {
        val angle = (i * 360f / particleCount) + rotation
        val radius = 200f + (i % 3) * 50f
        val x = centerX + cos(Math.toRadians(angle.toDouble())).toFloat() * radius
        val y = centerY + sin(Math.toRadians(angle.toDouble())).toFloat() * radius

        drawCircle(
            color = Color.White.copy(alpha = 0.1f),
            radius = 2f + (i % 3),
            center = Offset(x, y)
        )
    }
}

fun DrawScope.drawShineEffect() {
    val centerX = size.width / 2
    val centerY = size.height / 2
    val radius = size.minDimension / 2

    // Draw shine rays
    for (i in 0..7) {
        val angle = i * 45f
        val startRadius = radius * 0.6f
        val endRadius = radius * 1.2f

        val startX = centerX + cos(Math.toRadians(angle.toDouble())).toFloat() * startRadius
        val startY = centerY + sin(Math.toRadians(angle.toDouble())).toFloat() * startRadius
        val endX = centerX + cos(Math.toRadians(angle.toDouble())).toFloat() * endRadius
        val endY = centerY + sin(Math.toRadians(angle.toDouble())).toFloat() * endRadius

        drawLine(
            color = Color.Yellow.copy(alpha = 0.3f),
            start = Offset(startX, startY),
            end = Offset(endX, endY),
            strokeWidth = 3f
        )
    }
}

fun DrawScope.drawShoppingBag() {
    val paint = Paint().apply {
        style = PaintingStyle.Fill
    }

    val path = Path().apply {
        // Draw shopping bag shape
        moveTo(size.width * 0.2f, size.height * 0.3f)
        lineTo(size.width * 0.8f, size.height * 0.3f)
        lineTo(size.width * 0.75f, size.height * 0.9f)
        lineTo(size.width * 0.25f, size.height * 0.9f)
        close()

        // Handle
        moveTo(size.width * 0.35f, size.height * 0.3f)
        cubicTo(
            size.width * 0.35f, size.height * 0.15f,
            size.width * 0.65f, size.height * 0.15f,
            size.width * 0.65f, size.height * 0.3f
        )
    }

    drawPath(
        path = path,
        color = Color(0xFF6200EA),
        style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
    )
}

@Composable
fun ShineSaleTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Color(0xFF6200EA),
            secondary = Color(0xFF03DAC6)
        ),
        content = content
    )
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    ShineSaleTheme {
        SplashScreen()
    }
}