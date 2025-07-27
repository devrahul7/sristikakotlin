package com.example.shinesales.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shinesales.R
import com.example.shinesales.model.UserModel
import com.example.shinesales.repository.UserRepositoryImpl
import com.example.shinesales.ui.theme.ShineSalesTheme
import com.example.shinesales.viewmodel.UserViewModel

class DashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShineSalesTheme {
                DashboardScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen() {
    val context = LocalContext.current
    val repo = remember { UserRepositoryImpl() }
    val userViewModel = remember { UserViewModel(repo) }

    // ✅ BASIC STATE MANAGEMENT - No LiveData observation needed
    var selectedTab by remember { mutableStateOf(0) }
    var userName by remember { mutableStateOf("User") }
    var userEmail by remember { mutableStateOf("") }
    var userPhone by remember { mutableStateOf("") }
    var userAddress by remember { mutableStateOf("") }
    var isUserDataLoaded by remember { mutableStateOf(false) }

    val primaryColor = Color(0xFF6A1B9A)
    val goldColor = Color(0xFFFFD700)
    val backgroundColor = Color(0xFFF8F6FF)

    val featuredProducts = listOf(
        "Gold Ring" to "₹45,000",
        "Diamond Necklace" to "₹85,000",
        "Pearl Earrings" to "₹25,000",
        "Silver Bracelet" to "₹15,000"
    )

    // ✅ BASIC DATA LOADING - Manual state updates
    LaunchedEffect(Unit) {
        val currentUser = userViewModel.getCurrentUser()
        if (currentUser != null) {
            // Manual callback to update states
            userViewModel.getUserByID(currentUser.uid)

            // Get user data with callback
            repo.getUserByID(currentUser.uid) { user, success, message ->
                if (success && user != null) {
                    userName = user.fullName.ifEmpty { "Jewelry Lover" }
                    userEmail = user.email
                    userPhone = user.phoneNumber
                    userAddress = user.address
                    isUserDataLoaded = true
                }
            }
        }
    }

    fun navigateToAddProduct() {
        val intent = Intent(context, AddProductActivity::class.java)
        context.startActivity(intent)
    }

    fun navigateToViewAddedProduct() {
        val intent = Intent(context, ViewProductActivity::class.java)
        context.startActivity(intent)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        when (selectedTab) {
                            0 -> "✨ Shine Sales"
                            1 -> "🔍 Search"
                            2 -> "👤 Profile"
                            else -> "✨ Shine Sales"
                        },
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                actions = {
                    if (selectedTab == 0) { // Only show on home screen
                        IconButton(onClick = { /* Handle notifications */ }) {
                            Icon(
                                Icons.Default.Notifications,
                                contentDescription = "Notifications",
                                tint = Color.White
                            )
                        }
                        IconButton(onClick = { /* Handle cart */ }) {
                            Icon(
                                Icons.Default.ShoppingCart,
                                contentDescription = "Cart",
                                tint = Color.White
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = primaryColor
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                contentColor = primaryColor
            ) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = primaryColor,
                        selectedTextColor = primaryColor,
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray
                    )
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                    label = { Text("Search") },
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = primaryColor,
                        selectedTextColor = primaryColor,
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray
                    )
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                    label = { Text("Profile") },
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = primaryColor,
                        selectedTextColor = primaryColor,
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray
                    )
                )
            }
        }
    ) { paddingValues ->
        when (selectedTab) {
            0 -> HomeScreen(
                paddingValues = paddingValues,
                primaryColor = primaryColor,
                goldColor = goldColor,
                backgroundColor = backgroundColor,
                featuredProducts = featuredProducts,
                navigateToAddProduct = ::navigateToAddProduct,
                navigateToViewAddedProduct = ::navigateToViewAddedProduct,
                userName = userName
            )
            1 -> SearchScreen(paddingValues = paddingValues, primaryColor = primaryColor)
            2 -> ProfileScreen(
                paddingValues = paddingValues,
                primaryColor = primaryColor,
                userName = userName,
                userEmail = userEmail,
                userPhone = userPhone,
                userAddress = userAddress,
                isUserDataLoaded = isUserDataLoaded,
                userViewModel = userViewModel
            )
        }
    }
}

@Composable
fun HomeScreen(
    paddingValues: PaddingValues,
    primaryColor: Color,
    goldColor: Color,
    backgroundColor: Color,
    featuredProducts: List<Pair<String, String>>,
    navigateToAddProduct: () -> Unit,
    navigateToViewAddedProduct: () -> Unit,
    userName: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(backgroundColor)
    ) {
        // Welcome Section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = primaryColor),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Welcome Back, $userName! 💎",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "Discover premium jewelry collection",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .background(Color.White.copy(alpha = 0.2f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.img),
                        contentDescription = "Profile",
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                    )
                }
            }
        }

        // View Added Product Button
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clickable { navigateToViewAddedProduct() },
            colors = CardDefaults.cardColors(
                containerColor = goldColor.copy(alpha = 0.1f)
            ),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .background(goldColor.copy(alpha = 0.2f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.List,
                            contentDescription = "View Products",
                            tint = primaryColor,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            "View Added Products",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2D1B3D)
                        )
                        Text(
                            "Check your added jewelry items",
                            fontSize = 12.sp,
                            color = Color(0xFF2D1B3D).copy(alpha = 0.7f)
                        )
                    }
                }
                Icon(
                    Icons.Default.KeyboardArrowRight,
                    contentDescription = "Navigate",
                    tint = primaryColor,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Featured Products Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Featured Products",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2D1B3D)
            )
            TextButton(onClick = { /* Handle view all */ }) {
                Text(
                    "View All",
                    color = primaryColor,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Featured Products Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(featuredProducts.size) { index ->
                val (name, price) = featuredProducts[index]

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { /* Handle product click */ },
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(130.dp)
                                .background(Color(0xFFF8F6FF), RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(R.drawable.img),
                                contentDescription = name,
                                modifier = Modifier.size(90.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            name,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF2D1B3D)
                        )

                        Text(
                            price,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = primaryColor
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = { navigateToAddProduct() },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                            shape = RoundedCornerShape(12.dp),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                        ) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Add to Cart",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SearchScreen(paddingValues: PaddingValues, primaryColor: Color) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.Search,
            contentDescription = "Search",
            tint = primaryColor,
            modifier = Modifier.size(80.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "Search Feature",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = primaryColor
        )
        Text(
            "Coming Soon!",
            fontSize = 16.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun ProfileScreen(
    paddingValues: PaddingValues,
    primaryColor: Color,
    userName: String,
    userEmail: String,
    userPhone: String,
    userAddress: String,
    isUserDataLoaded: Boolean,
    userViewModel: UserViewModel
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp)
    ) {
        // Profile Header
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = primaryColor
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(R.drawable.img),
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    userName,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    "✨ Premium Jewelry Member ✨",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // User Details Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    "Account Information",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2D1B3D),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                if (isUserDataLoaded) {
                    // Email
                    UserInfoRow(
                        icon = Icons.Default.Email,
                        label = "Email",
                        value = userEmail.ifEmpty { "Not provided" },
                        primaryColor = primaryColor
                    )

                    // Phone
                    UserInfoRow(
                        icon = Icons.Default.Phone,
                        label = "Phone",
                        value = userPhone.ifEmpty { "Not added" },
                        primaryColor = primaryColor
                    )

                    // Address
                    UserInfoRow(
                        icon = Icons.Default.LocationOn,
                        label = "Address",
                        value = userAddress.ifEmpty { "Not added" },
                        primaryColor = primaryColor
                    )
                } else {
                    // Loading state
                    repeat(3) {
                        UserInfoRow(
                            icon = Icons.Default.Person,
                            label = "Loading...",
                            value = "Please wait...",
                            primaryColor = primaryColor
                        )
                        if (it < 2) Divider(modifier = Modifier.padding(vertical = 8.dp))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Profile Options
        ProfileOption(
            icon = Icons.Default.Edit,
            title = "Edit Profile",
            subtitle = "Update your personal information",
            onClick = {
//                val intent = Intent(context, EditProfileActivity::class.java)
//                context.startActivity(intent)
            }
        )

        ProfileOption(
            icon = Icons.Default.ExitToApp,
            title = "Logout",
            subtitle = "Sign out of your account",
            onClick = {
                userViewModel.logout { success, message ->
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    if (success) {
                        val intent = Intent(context, LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        context.startActivity(intent)
                    }
                }
            }
        )
    }
}

@Composable
fun UserInfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    primaryColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = label,
            tint = primaryColor,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                label,
                fontSize = 12.sp,
                color = Color.Gray
            )
            Text(
                value,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF2D1B3D)
            )
        }
    }
    if (label != "Address") {
        Divider(modifier = Modifier.padding(vertical = 8.dp))
    }
}

@Composable
fun ProfileOption(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = Color(0xFF6A1B9A),
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF2D1B3D)
                )
                Text(
                    subtitle,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
            Icon(
                Icons.Default.CheckCircle,
                contentDescription = null,
                tint = Color.Gray
            )
        }
    }
}
