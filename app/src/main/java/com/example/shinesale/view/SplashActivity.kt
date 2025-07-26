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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
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
import kotlinx.coroutines.delay
import kotlin.math.*
import kotlin.random.Random

class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShineSaleTheme {
                EnhancedSplashScreen()
            }
        }
    }
}

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
fun EnhancedSplashScreen() {
    val context = LocalContext.current
    var startAnimation by remember { mutableStateOf(false) }

    // Enhanced animation states
    val logoScale by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "logoScale"
    )

    val logoAlpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(1500, delayMillis = 200),
        label = "logoAlpha"
    )

    val textOffset by animateFloatAsState(
        targetValue = if (startAnimation) 0f else 150f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "textOffset"
    )

    val textAlpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(1200, delayMillis = 1000),
        label = "textAlpha"
    )

    // Infinite animations
    val infiniteTransition = rememberInfiniteTransition(label = "infiniteTransition")

    val primaryRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "primaryRotation"
    )

    val secondaryRotation by infiniteTransition.animateFloat(
        initialValue = 360f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "secondaryRotation"
    )

    val colorShift by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "colorShift"
    )

    val breathingScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breathingScale"
    )

    val waveEffect by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2 * PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "waveEffect"
    )

    LaunchedEffect(Unit) {
        startAnimation = true
        delay(4000) // Enhanced timing
        context.startActivity(Intent(context, MainActivity::class.java))
        (context as ComponentActivity).finish()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.sweepGradient(
                    colors = listOf(
                        Color(0xFF1e3c72), Color(0xFF2a5298), Color(0xFF667eea),
                        Color(0xFF764ba2), Color(0xFFf093fb), Color(0xFFf5576c),
                        Color(0xFFffa726), Color(0xFFffcc02), Color(0xFF4facfe),
                        Color(0xFF00f2fe), Color(0xFF43e97b), Color(0xFF38f9d7),
                        Color(0xFF667eea), Color(0xFF1e3c72)
                    ),
                    center = Offset.Infinite
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // Dynamic background overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.15f * colorShift),
                            Color.Black.copy(alpha = 0.4f)
                        ),
                        radius = 1200f + colorShift * 300f
                    )
                )
        )

        // Enhanced particle system
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawEnhancedParticleSystem(primaryRotation, secondaryRotation, colorShift, waveEffect)
            drawFloatingOrbs(primaryRotation * 0.6f, colorShift, breathingScale)
            drawEnergyRings(secondaryRotation, colorShift)
            drawDynamicLightBeams(primaryRotation * 0.3f, colorShift, waveEffect)
            drawConstellationPattern(primaryRotation * 0.2f, colorShift)
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Enhanced logo container
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .scale(logoScale * breathingScale)
                    .alpha(logoAlpha),
                contentAlignment = Alignment.Center
            ) {
                // Outer energy ring
                Canvas(
                    modifier = Modifier
                        .size(150.dp)
                        .rotate(primaryRotation * 0.8f)
                        .blur(12.dp)
                ) {
                    drawEnergyHalo(colorShift)
                }

                // Multiple rotating effects
                Canvas(modifier = Modifier.fillMaxSize()) {
                    rotate(primaryRotation) {
                        drawAdvancedShineEffect(colorShift, 1f)
                    }
                    rotate(secondaryRotation) {
                        drawAdvancedShineEffect(colorShift, 0.6f)
                    }
                    rotate(primaryRotation * -0.5f) {
                        drawAdvancedShineEffect(colorShift, 0.3f)
                    }
                }

                // Enhanced logo background
                Card(
                    modifier = Modifier.size(100.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                    elevation = CardDefaults.cardElevation(defaultElevation = 16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        Color.White,
                                        Color(0xFFF8F9FA).copy(alpha = 0.95f),
                                        Color(0xFFE9ECEF).copy(alpha = 0.9f)
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.size(60.dp)) {
                            drawPremiumShoppingBag(colorShift, waveEffect)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(50.dp))

            // Enhanced app name with multi-color gradient
            Text(
                text = "ShineSale",
                fontSize = 42.sp,
                fontWeight = FontWeight.Black,
                modifier = Modifier
                    .alpha(textAlpha)
                    .offset(y = textOffset.dp)
                    .scale(breathingScale * 0.98f),
                textAlign = TextAlign.Center,
                style = LocalTextStyle.current.copy(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFFFF6B9D), Color(0xFF4ECDC4), Color(0xFF45B7D1),
                            Color(0xFF96CEB4), Color(0xFFFEEA84), Color(0xFFD63384),
                            Color(0xFF6C5CE7), Color(0xFFA29BFE)
                        ),
                        start = Offset.Zero,
                        end = Offset.Infinite
                    ),
                    shadow = androidx.compose.ui.graphics.Shadow(
                        color = Color.Black.copy(alpha = 0.3f),
                        offset = Offset(3f, 3f),
                        blurRadius = 8f
                    )
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Enhanced tagline with shimmer effect
            Text(
                text = "✨ Shop Bright, Live Bright ✨",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                modifier = Modifier
                    .alpha(textAlpha * 0.9f)
                    .offset(y = (textOffset * 0.7f).dp)
                    .scale(breathingScale * 0.99f),
                textAlign = TextAlign.Center,
                style = LocalTextStyle.current.copy(
                    shadow = androidx.compose.ui.graphics.Shadow(
                        color = Color.Black.copy(alpha = 0.4f),
                        offset = Offset(2f, 2f),
                        blurRadius = 6f
                    )
                )
            )

            Spacer(modifier = Modifier.height(100.dp))

            // Enhanced loading indicator
            if (startAnimation) {
                Box(
                    modifier = Modifier
                        .alpha(textAlpha)
                        .scale(breathingScale * 0.9f),
                    contentAlignment = Alignment.Center
                ) {
                    // Multi-layered loading animation
                    repeat(3) { index ->
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size((40 + index * 8).dp)
                                .rotate(primaryRotation * (1f + index * 0.3f)),
                            color = when (index) {
                                0 -> Color(0xFFFF6B9D).copy(alpha = 0.8f)
                                1 -> Color(0xFF4ECDC4).copy(alpha = 0.6f)
                                else -> Color(0xFF45B7D1).copy(alpha = 0.4f)
                            },
                            strokeWidth = (2 + index).dp,
                            strokeCap = StrokeCap.Round
                        )
                    }
                }
            }
        }
    }
}

// Enhanced drawing functions with modern effects
fun DrawScope.drawEnhancedParticleSystem(
    rotation: Float, fastRotation: Float, colorShift: Float, waveEffect: Float
) {
    val particleCount = 50
    val centerX = size.width / 2
    val centerY = size.height / 2

    val vibrantColors = listOf(
        Color(0xFFFF6B9D), Color(0xFF4ECDC4), Color(0xFF45B7D1),
        Color(0xFF96CEB4), Color(0xFFFEEA84), Color(0xFFD63384),
        Color(0xFF6C5CE7), Color(0xFFA29BFE), Color(0xFFFD79A8),
        Color(0xFF00B894), Color(0xFFFFCB00), Color(0xFFE17055)
    )

    for (i in 0 until particleCount) {
        val angle = (i * 360f / particleCount) + rotation * (if (i % 3 == 0) 1f else -0.8f)
        val baseRadius = 180f + (i % 7) * 50f
        val waveOffset = sin(waveEffect + i * 0.5f).toFloat() * 30f
        val radius = baseRadius + waveOffset

        val x = centerX + cos((angle * PI / 180).toFloat()).toFloat() * radius
        val y = centerY + sin((angle * PI / 180).toFloat()).toFloat() * radius

        val colorIndex = (i + (colorShift * vibrantColors.size).toInt()) % vibrantColors.size
        val particleColor = vibrantColors[colorIndex]
        val dynamicAlpha = 0.7f + sin((fastRotation * PI / 180 + i).toFloat()).toFloat() * 0.3f

        // Enhanced particle with glow effect
        drawCircle(
            color = particleColor.copy(alpha = dynamicAlpha),
            radius = 4f + (i % 5) + sin((rotation * PI / 180 + i).toFloat()).toFloat() * 2f,
            center = Offset(x.toFloat(), y.toFloat())
        )

        // Inner glow
        drawCircle(
            color = Color.White.copy(alpha = dynamicAlpha * 0.6f),
            radius = 2f + sin((rotation * PI / 180 + i).toFloat()).toFloat(),
            center = Offset(x.toFloat(), y.toFloat())
        )
    }
}

fun DrawScope.drawFloatingOrbs(rotation: Float, colorShift: Float, scale: Float) {
    val orbCount = 20
    val centerX = size.width / 2
    val centerY = size.height / 2

    val orbColors = listOf(
        Color(0xFFFF6B9D), Color(0xFF4ECDC4), Color(0xFF6C5CE7),
        Color(0xFFFEEA84), Color(0xFF00B894), Color(0xFFE17055)
    )

    for (i in 0 until orbCount) {
        val angle = (i * 72f) + rotation * 0.4f
        val radius = 350f + (i % 4) * 100f
        val x = centerX + cos((angle * PI / 180).toFloat()).toFloat() * radius * scale
        val y = centerY + sin((angle * PI / 180).toFloat()).toFloat() * radius * scale

        val colorIndex = (i + (colorShift * orbColors.size).toInt()) % orbColors.size
        val orbSize = 12f + (i % 3) * 6f

        // Orb with gradient
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    orbColors[colorIndex].copy(alpha = 0.8f),
                    orbColors[colorIndex].copy(alpha = 0.2f),
                    Color.Transparent
                ),
                radius = orbSize * 2f
            ),
            radius = orbSize,
            center = Offset(x.toFloat(), y.toFloat())
        )
    }
}

fun DrawScope.drawEnergyRings(rotation: Float, colorShift: Float) {
    val centerX = size.width / 2
    val centerY = size.height / 2
    val ringCount = 8

    val energyColors = listOf(
        Color(0xFFFF6B9D), Color(0xFF4ECDC4), Color(0xFF45B7D1),
        Color(0xFFFEEA84), Color(0xFFD63384), Color(0xFF6C5CE7)
    )

    for (i in 0 until ringCount) {
        val radius = 100f + i * 40f
        val colorIndex = (i + (colorShift * energyColors.size).toInt()) % energyColors.size
        val rotationOffset = rotation * (1f + i * 0.1f)

        rotate(rotationOffset) {
            drawCircle(
                color = energyColors[colorIndex].copy(alpha = 0.3f - i * 0.03f),
                radius = radius,
                center = Offset(centerX, centerY),
                style = Stroke(width = 3f)
            )
        }
    }
}

fun DrawScope.drawDynamicLightBeams(rotation: Float, colorShift: Float, waveEffect: Float) {
    val centerX = size.width / 2
    val centerY = size.height / 2
    val beamCount = 16

    val beamColors = listOf(
        Color(0xFFFFD700), Color(0xFFFF69B4), Color(0xFF00CED1),
        Color(0xFF98FB98), Color(0xFFDDA0DD), Color(0xFFFFB347),
        Color(0xFF87CEEB), Color(0xFFDC143C)
    )

    for (i in 0 until beamCount) {
        val angle = i * (360f / beamCount) + rotation
        val startRadius = 80f
        val endRadius = 250f + sin((waveEffect + i).toFloat()).toFloat() * 50f

        val startX = centerX + cos((angle * PI / 180).toFloat()).toFloat() * startRadius
        val startY = centerY + sin((angle * PI / 180).toFloat()).toFloat() * startRadius
        val endX = centerX + cos((angle * PI / 180).toFloat()).toFloat() * endRadius
        val endY = centerY + sin((angle * PI / 180).toFloat()).toFloat() * endRadius

        val colorIndex = (i + (colorShift * beamColors.size).toInt()) % beamColors.size
        val beamAlpha = 0.6f + sin((waveEffect + i).toFloat()).toFloat() * 0.3f

        drawLine(
            color = beamColors[colorIndex].copy(alpha = beamAlpha),
            start = Offset(startX.toFloat(), startY.toFloat()),
            end = Offset(endX.toFloat(), endY.toFloat()),
            strokeWidth = 3f + sin((waveEffect + i).toFloat()).toFloat() * 2f,
            cap = StrokeCap.Round
        )
    }
}

fun DrawScope.drawConstellationPattern(rotation: Float, colorShift: Float) {
    val centerX = size.width / 2
    val centerY = size.height / 2
    val starCount = 30

    for (i in 0 until starCount) {
        val angle = Random.nextFloat() * 360f + rotation * 0.1f
        val radius = Random.nextFloat() * 400f + 200f
        val x = centerX + cos((angle * PI / 180).toFloat()).toFloat() * radius
        val y = centerY + sin((angle * PI / 180).toFloat()).toFloat() * radius

        val starAlpha = 0.4f + sin((rotation * PI / 180 + i).toFloat()).toFloat() * 0.3f
        val starSize = 1f + Random.nextFloat() * 2f

        drawCircle(
            color = Color.White.copy(alpha = starAlpha),
            radius = starSize,
            center = Offset(x.toFloat(), y.toFloat())
        )
    }
}

fun DrawScope.drawEnergyHalo(colorShift: Float) {
    val centerX = size.width / 2
    val centerY = size.height / 2
    val haloColors = listOf(
        Color(0xFFFF6B9D), Color(0xFF4ECDC4), Color(0xFF45B7D1),
        Color(0xFFFEEA84), Color(0xFFD63384), Color(0xFF6C5CE7)
    )

    for (i in haloColors.indices) {
        val radius = 60f + i * 15f
        val colorIndex = (i + (colorShift * haloColors.size).toInt()) % haloColors.size

        drawCircle(
            color = haloColors[colorIndex].copy(alpha = 0.4f - i * 0.05f),
            radius = radius,
            center = Offset(centerX, centerY),
            style = Stroke(width = 6f)
        )
    }
}

fun DrawScope.drawAdvancedShineEffect(colorShift: Float, intensity: Float) {
    val centerX = size.width / 2
    val centerY = size.height / 2
    val rayCount = 12

    val shineColors = listOf(
        Color(0xFFFFD700), Color(0xFFFFA500), Color(0xFFFF69B4),
        Color(0xFF00CED1), Color(0xFF98FB98), Color(0xFFDDA0DD),
        Color(0xFFFFB347), Color(0xFF87CEEB)
    )

    for (i in 0 until rayCount) {
        val angle = i * (360f / rayCount)
        val startRadius = 40f * intensity
        val endRadius = 70f * intensity

        val startX = centerX + cos((angle * PI / 180).toFloat()).toFloat() * startRadius
        val startY = centerY + sin((angle * PI / 180).toFloat()).toFloat() * startRadius
        val endX = centerX + cos((angle * PI / 180).toFloat()).toFloat() * endRadius
        val endY = centerY + sin((angle * PI / 180).toFloat()).toFloat() * endRadius

        val colorIndex = (i + (colorShift * shineColors.size).toInt()) % shineColors.size

        drawLine(
            color = shineColors[colorIndex].copy(alpha = 0.8f * intensity),
            start = Offset(startX.toFloat(), startY.toFloat()),
            end = Offset(endX.toFloat(), endY.toFloat()),
            strokeWidth = 4f * intensity,
            cap = StrokeCap.Round
        )
    }
}

fun DrawScope.drawPremiumShoppingBag(colorShift: Float, waveEffect: Float) {
    val dynamicColors = listOf(
        Color(0xFF667eea), Color(0xFF764ba2), Color(0xFFf093fb),
        Color(0xFFf5576c), Color(0xFF4facfe), Color(0xFF00f2fe),
        Color(0xFF43e97b), Color(0xFF38f9d7)
    )

    val colorIndex = (colorShift * dynamicColors.size).toInt() % dynamicColors.size
    val primaryColor = dynamicColors[colorIndex]
    val waveOffset = sin(waveEffect).toFloat() * 2f

    val bagPath = Path().apply {
        // Enhanced bag body with dynamic curves
        moveTo(size.width * 0.12f, size.height * (0.35f + waveOffset * 0.01f))
        quadraticBezierTo(
            size.width * 0.5f, size.height * (0.22f + waveOffset * 0.01f),
            size.width * 0.88f, size.height * (0.35f + waveOffset * 0.01f)
        )
        lineTo(size.width * 0.83f, size.height * 0.88f)
        quadraticBezierTo(
            size.width * 0.5f, size.height * 0.98f,
            size.width * 0.17f, size.height * 0.88f
        )
        close()
    }

    val handlePath = Path().apply {
        moveTo(size.width * 0.28f, size.height * 0.35f)
        quadraticBezierTo(
            size.width * 0.28f, size.height * 0.08f,
            size.width * 0.5f, size.height * 0.08f
        )
        quadraticBezierTo(
            size.width * 0.72f, size.height * 0.08f,
            size.width * 0.72f, size.height * 0.35f
        )
    }

    // Draw bag with enhanced gradient
    drawPath(
        path = bagPath,
        brush = Brush.linearGradient(
            colors = listOf(
                primaryColor,
                primaryColor.copy(alpha = 0.8f),
                primaryColor.copy(alpha = 0.6f)
            )
        ),
        style = Stroke(width = 5.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
    )

    // Draw handle
    drawPath(
        path = handlePath,
        color = primaryColor.copy(alpha = 0.9f),
        style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
    )

    // Add premium highlights
    drawPath(
        path = bagPath,
        color = Color.White.copy(alpha = 0.4f),
        style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
    )

    drawPath(
        path = handlePath,
        color = Color.White.copy(alpha = 0.3f),
        style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
    )

    // Add sparkle effect
    val sparklePositions = listOf(
        Offset(size.width * 0.3f, size.height * 0.5f),
        Offset(size.width * 0.7f, size.height * 0.6f),
        Offset(size.width * 0.5f, size.height * 0.7f)
    )

    sparklePositions.forEach { position ->
        drawCircle(
            color = Color.White.copy(alpha = 0.8f + sin(waveEffect).toFloat() * 0.2f),
            radius = 2f + sin((waveEffect + position.x).toFloat()).toFloat() * 1f,
            center = position
        )
    }
}

@Composable
fun ShineSaleTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Color(0xFF667eea),
            secondary = Color(0xFF764ba2),
            tertiary = Color(0xFFf093fb),
            surface = Color(0xFF1a1a2e),
            background = Color(0xFF16213e)
        ),
        content = content
    )
}

@Preview(showBackground = true)
@Composable
fun EnhancedSplashScreenPreview() {
    ShineSaleTheme {
        EnhancedSplashScreen()
    }
}