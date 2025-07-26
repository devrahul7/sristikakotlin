package com.shinesale.app

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.PI
import kotlin.random.Random

class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShineSaleTheme {
                SplashScreen()
            }
        }
    }
}

// Create a dummy MainActivity class for navigation
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShineSaleTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Text("Main Activity - Replace with your actual content")
                }
            }
        }
    }
}

@Composable
fun SplashScreen() {
    val context = LocalContext.current

    // Animation states
    var startAnimation by remember { mutableStateOf(false) }

    // Logo scale animation with bounce effect
    val logoScale by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "logoScale"
    )

    // Logo alpha animation
    val logoAlpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 1200,
            delayMillis = 100,
            easing = FastOutSlowInEasing
        ),
        label = "logoAlpha"
    )

    // Text slide animation
    val textOffset by animateFloatAsState(
        targetValue = if (startAnimation) 0f else 100f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "textOffset"
    )

    // Text alpha animation
    val textAlpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 1000,
            delayMillis = 800
        ),
        label = "textAlpha"
    )

    // Multiple rotating elements
    val infiniteTransition = rememberInfiniteTransition(label = "infiniteTransition")

    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    val fastRotation by infiniteTransition.animateFloat(
        initialValue = 360f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "fastRotation"
    )

    // Color animation for dynamic background
    val colorProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(5000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "colorProgress"
    )

    // Pulsing effect with multiple layers
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    val secondaryPulse by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "secondaryPulse"
    )

    // Start animation on composition
    LaunchedEffect(Unit) {
        startAnimation = true
        delay(3500) // 3.5 seconds delay for better experience
        context.startActivity(Intent(context, MainActivity::class.java))
        (context as ComponentActivity).finish()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.sweepGradient(
                    colors = listOf(
                        Color(0xFF667eea),
                        Color(0xFF764ba2),
                        Color(0xFFf093fb),
                        Color(0xFFf5576c),
                        Color(0xFF4facfe),
                        Color(0xFF00f2fe),
                        Color(0xFF43e97b),
                        Color(0xFF38f9d7),
                        Color(0xFF667eea)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // Animated gradient overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.3f * colorProgress),
                            Color.Black.copy(alpha = 0.6f)
                        ),
                        radius = 800f + colorProgress * 200f
                    )
                )
        )

        // Multiple particle layers
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            drawColorfulParticles(rotation, fastRotation, colorProgress)
            drawFloatingBubbles(rotation * 0.5f, colorProgress)
            drawLightRays(fastRotation, colorProgress)
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Main logo container with multiple effects
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .scale(logoScale * pulseScale)
                    .alpha(logoAlpha),
                contentAlignment = Alignment.Center
            ) {
                // Outer glow ring
                Canvas(
                    modifier = Modifier
                        .size(130.dp)
                        .scale(secondaryPulse)
                        .blur(8.dp)
                ) {
                    rotate(rotation * 0.7f) {
                        drawGlowRing(colorProgress)
                    }
                }

                // Multiple rotating shine effects
                Canvas(
                    modifier = Modifier.fillMaxSize()
                ) {
                    rotate(rotation) {
                        drawDynamicShineEffect(colorProgress)
                    }
                    rotate(fastRotation) {
                        drawSecondaryShineEffect(colorProgress)
                    }
                }

                // Logo background with gradient
                Card(
                    modifier = Modifier.size(90.dp),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Transparent
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 12.dp
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        Color.White,
                                        Color(0xFFF8F9FA),
                                        Color(0xFFE9ECEF)
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(
                            modifier = Modifier.size(50.dp)
                        ) {
                            drawEnhancedShoppingBag(colorProgress)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // App name with rainbow effect
            Text(
                text = "ShineSale",
                fontSize = 38.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier
                    .alpha(textAlpha)
                    .offset(y = textOffset.dp),
                textAlign = TextAlign.Center,
                style = LocalTextStyle.current.copy(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFFFF6B6B),
                            Color(0xFF4ECDC4),
                            Color(0xFF45B7D1),
                            Color(0xFF96CEB4),
                            Color(0xFFFEEA84),
                            Color(0xFFD63384)
                        )
                    )
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Enhanced tagline
            Text(
                text = "Shop Bright, Shop Right ✨",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White,
                modifier = Modifier
                    .alpha(textAlpha * 0.9f)
                    .offset(y = (textOffset * 0.7f).dp),
                textAlign = TextAlign.Center,
                style = LocalTextStyle.current.copy(
                    shadow = Shadow(
                        color = Color.Black.copy(alpha = 0.3f),
                        offset = Offset(2f, 2f),
                        blurRadius = 4f
                    )
                )
            )

            Spacer(modifier = Modifier.height(80.dp))

            // Enhanced loading indicator with color animation
            if (startAnimation) {
                Box(
                    modifier = Modifier.alpha(textAlpha),
                    contentAlignment = Alignment.Center
                ) {
                    // Background circle
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(32.dp)
                            .scale(pulseScale),
                        color = Color.White.copy(alpha = 0.3f),
                        strokeWidth = 3.dp,
                        progress = 1f
                    )

                    // Animated progress
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(32.dp)
                            .scale(pulseScale),
                        strokeWidth = 3.dp,
                        color = Color.White,
                        strokeCap = StrokeCap.Round
                    )
                }
            }
        }
    }
}

fun DrawScope.drawColorfulParticles(rotation: Float, fastRotation: Float, colorProgress: Float) {
    val particleCount = 30
    val centerX = size.width / 2
    val centerY = size.height / 2

    val colors = listOf(
        Color(0xFFFF6B6B), Color(0xFF4ECDC4), Color(0xFF45B7D1),
        Color(0xFF96CEB4), Color(0xFFFEEA84), Color(0xFFD63384),
        Color(0xFF6C5CE7), Color(0xFFA29BFE), Color(0xFFFD79A8)
    )

    for (i in 0 until particleCount) {
        val angle = (i * 360f / particleCount) + rotation * (if (i % 2 == 0) 1f else -0.7f)
        val radius = 150f + (i % 5) * 40f + sin((fastRotation * PI / 180 + i).toFloat()).toFloat() * 20f
        val x = centerX + cos((angle * PI / 180).toFloat()) * radius
        val y = centerY + sin((angle * PI / 180).toFloat()) * radius

        val colorIndex = (i + (colorProgress * colors.size).toInt()) % colors.size
        val particleColor = colors[colorIndex]

        drawCircle(
            color = particleColor.copy(alpha = 0.6f + sin((fastRotation * PI / 180 + i).toFloat()) * 0.3f),
            radius = 3f + (i % 4) + sin((rotation * PI / 180 + i).toFloat()) * 2f,
            center = Offset(x, y)
        )
    }
}

fun DrawScope.drawFloatingBubbles(rotation: Float, colorProgress: Float) {
    val bubbleCount = 15
    val centerX = size.width / 2
    val centerY = size.height / 2

    for (i in 0 until bubbleCount) {
        val angle = (i * 60f) + rotation * 0.3f
        val radius = 300f + (i % 3) * 80f
        val x = centerX + cos((angle * PI / 180).toFloat()) * radius
        val y = centerY + sin((angle * PI / 180).toFloat()) * radius

        drawCircle(
            color = Color.White.copy(alpha = 0.1f + colorProgress * 0.1f),
            radius = 8f + (i % 3) * 4f,
            center = Offset(x, y),
            style = Stroke(width = 2f)
        )
    }
}

fun DrawScope.drawLightRays(rotation: Float, colorProgress: Float) {
    val centerX = size.width / 2
    val centerY = size.height / 2
    val maxRadius = size.minDimension

    val rayColors = listOf(
        Color(0xFFFFD700), Color(0xFFFF69B4), Color(0xFF00CED1),
        Color(0xFF98FB98), Color(0xFFDDA0DD), Color(0xFFFFB347)
    )

    for (i in 0..11) {
        val angle = i * 30f + rotation * 0.5f
        val startRadius = maxRadius * 0.15f
        val endRadius = maxRadius * 0.4f

        val startX = centerX + cos((angle * PI / 180).toFloat()) * startRadius
        val startY = centerY + sin((angle * PI / 180).toFloat()) * startRadius
        val endX = centerX + cos((angle * PI / 180).toFloat()) * endRadius
        val endY = centerY + sin((angle * PI / 180).toFloat()) * endRadius

        val colorIndex = (i + (colorProgress * rayColors.size).toInt()) % rayColors.size

        drawLine(
            color = rayColors[colorIndex].copy(alpha = 0.4f),
            start = Offset(startX, startY),
            end = Offset(endX, endY),
            strokeWidth = 2f + sin((rotation * PI / 180).toFloat()) * 1f,
            cap = StrokeCap.Round
        )
    }
}

fun DrawScope.drawGlowRing(colorProgress: Float) {
    val centerX = size.width / 2
    val centerY = size.height / 2
    val radius = size.minDimension / 3

    val glowColors = listOf(
        Color(0xFFFF6B6B), Color(0xFF4ECDC4), Color(0xFF45B7D1),
        Color(0xFFFEEA84), Color(0xFFD63384), Color(0xFF6C5CE7)
    )

    for (i in glowColors.indices) {
        val angle = i * 60f
        val glowRadius = radius + i * 5f
        val colorIndex = (i + (colorProgress * glowColors.size).toInt()) % glowColors.size

        drawCircle(
            color = glowColors[colorIndex].copy(alpha = 0.3f),
            radius = glowRadius,
            center = Offset(centerX, centerY),
            style = Stroke(width = 4f)
        )
    }
}

fun DrawScope.drawDynamicShineEffect(colorProgress: Float) {
    val centerX = size.width / 2
    val centerY = size.height / 2
    val radius = size.minDimension / 2

    val shineColors = listOf(
        Color(0xFFFFD700), Color(0xFFFFA500), Color(0xFFFF69B4),
        Color(0xFF00CED1), Color(0xFF98FB98), Color(0xFFDDA0DD)
    )

    for (i in 0..7) {
        val angle = i * 45f
        val startRadius = radius * 0.4f
        val endRadius = radius * 0.8f

        val startX = centerX + cos((angle * PI / 180).toFloat()) * startRadius
        val startY = centerY + sin((angle * PI / 180).toFloat()) * startRadius
        val endX = centerX + cos((angle * PI / 180).toFloat()) * endRadius
        val endY = centerY + sin((angle * PI / 180).toFloat()) * endRadius

        val colorIndex = (i + (colorProgress * shineColors.size).toInt()) % shineColors.size

        drawLine(
            color = shineColors[colorIndex].copy(alpha = 0.7f),
            start = Offset(startX, startY),
            end = Offset(endX, endY),
            strokeWidth = 4f,
            cap = StrokeCap.Round
        )
    }
}

fun DrawScope.drawSecondaryShineEffect(colorProgress: Float) {
    val centerX = size.width / 2
    val centerY = size.height / 2
    val radius = size.minDimension / 2.5f

    for (i in 0..11) {
        val angle = i * 30f
        val startRadius = radius * 0.6f
        val endRadius = radius * 0.9f

        val startX = centerX + cos((angle * PI / 180).toFloat()) * startRadius
        val startY = centerY + sin((angle * PI / 180).toFloat()) * startRadius
        val endX = centerX + cos((angle * PI / 180).toFloat()) * endRadius
        val endY = centerY + sin((angle * PI / 180).toFloat()) * endRadius

        drawLine(
            color = Color.White.copy(alpha = 0.5f + colorProgress * 0.3f),
            start = Offset(startX, startY),
            end = Offset(endX, endY),
            strokeWidth = 2f,
            cap = StrokeCap.Round
        )
    }
}

fun DrawScope.drawEnhancedShoppingBag(colorProgress: Float) {
    val bagColors = listOf(
        Color(0xFF667eea), Color(0xFF764ba2), Color(0xFFf093fb),
        Color(0xFFf5576c), Color(0xFF4facfe), Color(0xFF00f2fe)
    )

    val colorIndex = (colorProgress * bagColors.size).toInt() % bagColors.size
    val currentColor = bagColors[colorIndex]

    val path = Path().apply {
        // Enhanced shopping bag shape with curves
        moveTo(size.width * 0.15f, size.height * 0.35f)
        quadraticBezierTo(
            size.width * 0.5f, size.height * 0.25f,
            size.width * 0.85f, size.height * 0.35f
        )
        lineTo(size.width * 0.8f, size.height * 0.85f)
        quadraticBezierTo(
            size.width * 0.5f, size.height * 0.95f,
            size.width * 0.2f, size.height * 0.85f
        )
        close()

        // Curved handle
        moveTo(size.width * 0.3f, size.height * 0.35f)
        quadraticBezierTo(
            size.width * 0.3f, size.height * 0.1f,
            size.width * 0.5f, size.height * 0.1f
        )
        quadraticBezierTo(
            size.width * 0.7f, size.height * 0.1f,
            size.width * 0.7f, size.height * 0.35f
        )
    }

    // Draw bag with gradient effect
    drawPath(
        path = path,
        brush = Brush.linearGradient(
            colors = listOf(
                currentColor,
                currentColor.copy(alpha = 0.7f)
            )
        ),
        style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
    )

    // Add highlight
    drawPath(
        path = path,
        color = Color.White.copy(alpha = 0.3f),
        style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
    )
}

@Composable
fun ShineSaleTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Color(0xFF667eea),
            secondary = Color(0xFF764ba2),
            tertiary = Color(0xFFf093fb)
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