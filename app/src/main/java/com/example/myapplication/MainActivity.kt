package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    // Estado para controlar qual tela está ativa
    var currentScreen by remember { mutableStateOf("Feed") }
    var selectedChatName by remember { mutableStateOf("") }

    when {
        currentScreen == "Feed" -> FolioApp(onNavigate = { currentScreen = it })
        currentScreen == "Search" -> ExplorarScreen(onNavigate = { currentScreen = it })
        currentScreen == "Profile" -> UsuarioScreen(onNavigate = { currentScreen = it })
        currentScreen == "EditProfile" -> EditProfileScreen(
            onBack = { currentScreen = "Profile" },
            onSave = {
                // Lógica para salvar
                currentScreen = "Profile"
            },
            onNavigate = { currentScreen = it }
        )
        currentScreen == "Create" -> CreateStoryScreen(onNavigate = { currentScreen = it })

        currentScreen == "Chat" -> ChatListScreen(
            onNavigate = { currentScreen = it },
            onChatClick = { chat ->
                selectedChatName = chat.name
                currentScreen = "ChatDetail"
            }
        )
        currentScreen == "ChatDetail" -> ChatDetailScreen(
            chatName = selectedChatName,
            onBack = { currentScreen = "Chat" }
        )
        currentScreen == "PostDetail" -> {
             Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Detalhes do Post") },
                        navigationIcon = {
                            IconButton(onClick = { currentScreen = "Feed" }) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                            }
                        }
                    )
                }
            ) { padding ->
                Box(Modifier.padding(padding).fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Detalhes do Post")
                }
            }
        }
        currentScreen.startsWith("TagDetail/") -> {
            val tagName = currentScreen.substringAfter("TagDetail/")
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Tag: $tagName") },
                        navigationIcon = {
                            IconButton(onClick = { currentScreen = "Feed" }) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                            }
                        }
                    )
                }
            ) { padding ->
                Box(Modifier.padding(padding).fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Detalhes da Tag: $tagName")
                }
            }
        }
        else -> {
            // Placeholder para outras telas
            Scaffold(
                bottomBar = { SharedBottomNavigation(selectedItem = currentScreen, onNavigate = { currentScreen = it }) }
            ) { padding ->
                Box(Modifier.padding(padding).fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Tela $currentScreen em desenvolvimento")
                }
            }
        }
    }
}

data class Post(
    val id: Int,
    val authorName: String,
    val authorHandle: String,
    val title: String,
    val content: String,
    val likes: String,
    val comments: String
)

data class Tag(val id: Int, val name: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FolioApp(onNavigate: (String) -> Unit) {
    val posts = remember { 
        mutableStateListOf(
            Post(1, "Virginia Woolf", "@virginia_w", "To the Lighthouse", "\"The lighthouse stood out stark and clear; a magnificent sentinel against the gray and rolling tide of afternoon memories...\"", "1.2k", "340"),
            Post(2, "Fernando Pessoa", "@desassossego", "Livro do Desassossego", "\"Ah, as estradas de Portugal ao entardecer, quando a névoa beija os telhados amigos e a alma se dissolve in prosa silenciada...\"", "945", "188"),
            Post(3, "Clarice Lispector", "@clarice_l", "A Hora da Estrela", "\"Tudo no mundo começou com um sim. Uma molécula disse sim a outra molécula e nasceu a vida...\"", "2.5k", "560"),
            Post(4, "Machado de Assis", "@machado_assis", "Dom Casmurro", "\"Olhos de ressaca? Sim, olhos de ressaca. Eram assim, com aquela força de atrair e de prender...\"", "3.1k", "890")
        )
    }
    
    var newPostTitle by remember { mutableStateOf("") }

    Scaffold(
        containerColor = FolioBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Folio",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 26.sp,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Outlined.Notifications, contentDescription = "Notifications", tint = Color.Black)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = FolioBackground)
            )
        },
        bottomBar = {
            SharedBottomNavigation(selectedItem = "Feed", onNavigate = onNavigate)
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            
            items(posts, key = { it.id }) { post ->
                PostCard(post, onDoubleClick = { onNavigate("PostDetail") })
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PostCard(post: Post, onDoubleClick: () -> Unit) {
    var showSynopsis by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                interactionSource = interactionSource,
                indication = LocalIndication.current,
                onClick = { showSynopsis = !showSynopsis },
                onDoubleClick = onDoubleClick
            ),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE0E0E0))
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        post.authorName,
                        fontWeight = FontWeight.Bold,
                        color = FolioTextPrimary,
                        fontSize = 15.sp
                    )
                    Text(
                        post.authorHandle,
                        color = FolioTextSecondary,
                        fontSize = 13.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                post.title,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = FolioTextPrimary
            )
            Spacer(modifier = Modifier.height(12.dp))
            // Image Placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF2F2F2)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Add, contentDescription = null, tint = Color.LightGray, modifier = Modifier.size(48.dp))
            }

            AnimatedVisibility(visible = showSynopsis) {
                Text(
                    text = post.content,
                        fontStyle = FontStyle.Italic,
                        color = FolioTextSecondary,
                        fontSize = 14.sp,
                        lineHeight = 22.sp,
                        fontFamily = FontFamily.Serif
                    )

            }

            Spacer(modifier = Modifier.height(20.dp))
            HorizontalDivider(color = Color(0xFFF0F0F0), thickness = 1.dp)
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Outlined.FavoriteBorder,
                        contentDescription = "Like",
                        modifier = Modifier.size(20.dp),
                        tint = FolioTextSecondary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(post.likes, fontSize = 13.sp, color = FolioTextSecondary)
                    Spacer(modifier = Modifier.width(20.dp))
                    Icon(
                        Icons.Outlined.Email,
                        contentDescription = "Comment",
                        modifier = Modifier.size(20.dp),
                        tint = FolioTextSecondary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(post.comments, fontSize = 13.sp, color = FolioTextSecondary)
                }
                Icon(
                    Icons.Default.Star,
                    contentDescription = "Bookmark",
                    modifier = Modifier.size(20.dp),
                    tint = FolioTextSecondary
                )
            }
        }
    }
}

@Composable
fun SharedBottomNavigation(selectedItem: String, onNavigate: (String) -> Unit) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 0.dp,
        modifier = Modifier.height(80.dp)
    ) {
        NavigationBarItem(
            selected = selectedItem == "Feed",
            onClick = { onNavigate("Feed") },
            icon = { Icon(if (selectedItem == "Feed") Icons.Filled.Description else Icons.Outlined.Description, contentDescription = null) },
            label = { Text("Feed", fontSize = 10.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = FolioOrange,
                selectedTextColor = FolioOrange,
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            selected = selectedItem == "Chat" || selectedItem == "ChatDetail",
            onClick = { onNavigate("Chat") },
            icon = { Icon(if (selectedItem == "Chat" || selectedItem == "ChatDetail") Icons.Filled.ChatBubble else Icons.Outlined.ChatBubbleOutline, contentDescription = null) },
            label = { Text("Chat", fontSize = 10.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = FolioOrange,
                selectedTextColor = FolioOrange,
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            selected = selectedItem == "Create",
            onClick = { onNavigate("Create") },
            icon = { Icon(Icons.Outlined.Add, contentDescription = "Create") },
            label = { Text("Create", fontSize = 10.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = FolioOrange,
                selectedTextColor = FolioOrange,
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            selected = selectedItem == "Search",
            onClick = { onNavigate("Search") },
            icon = { Icon(if (selectedItem == "Search") Icons.Default.Search else Icons.Outlined.Search, contentDescription = "Search") },
            label = { Text("Search", fontSize = 10.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = FolioOrange,
                selectedTextColor = FolioOrange,
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            selected = selectedItem == "Profile",
            onClick = { onNavigate("Profile") },
            icon = { Icon(if (selectedItem == "Profile") Icons.Filled.Person else Icons.Outlined.Person, contentDescription = "Profile") },
            label = { Text("Profile", fontSize = 10.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = FolioOrange,
                selectedTextColor = FolioOrange,
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = Color.Transparent
            )
        )
    }
}
