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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
    var currentScreen by remember { mutableStateOf("Feed") }
    var selectedChatName by remember { mutableStateOf("") }
    var selectedTextoId by remember { mutableStateOf(-1) }

    when {
        currentScreen == "Feed" -> FolioApp(onNavigate = { currentScreen = it }, onTextoClick = { id -> 
            selectedTextoId = id
            currentScreen = "PostDetail"
        })
        currentScreen == "Search" -> ExplorarScreen(onNavigate = { currentScreen = it })
        currentScreen == "Profile" -> UsuarioScreen(onNavigate = { currentScreen = it })
        currentScreen == "EditProfile" -> EditProfileScreen(
            onBack = { currentScreen = "Profile" },
            onSave = { currentScreen = "Profile" },
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
             PostDetailScreen(textoId = selectedTextoId, onBack = { currentScreen = "Feed" })
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostDetailScreen(textoId: Int, onBack: () -> Unit) {
    val context = LocalContext.current
    val db = AppDatabase.getInstance(context)
    var texto by remember { mutableStateOf<Texto?>(null) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(textoId) {
        texto = withContext(Dispatchers.IO) {
            db.textoDao().buscarPorId(textoId)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(texto?.titulo ?: "Carregando...") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { padding ->
        Column(Modifier.padding(padding).padding(16.dp).fillMaxSize()) {
            texto?.let {
                Text(it.titulo, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Text("Por Autor #${it.autorId}", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
                Spacer(Modifier.height(16.dp))
                Text(it.conteudo, style = MaterialTheme.typography.bodyLarge)
            } ?: Text("Post não encontrado")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FolioApp(onNavigate: (String) -> Unit, onTextoClick: (Int) -> Unit) {
    val context = LocalContext.current
    val db = AppDatabase.getInstance(context)
    val scope = rememberCoroutineScope()
    var posts by remember { mutableStateOf(listOf<Texto>()) }

    LaunchedEffect(Unit) {
        // Garantir que existe um usuário padrão para o app local
        withContext(Dispatchers.IO) {
            val usuarios = db.usuarioDao().buscarTodos()
            if (usuarios.isEmpty()) {
                db.usuarioDao().insert(Usuario(nome = "Helena Andrade", bio = "Escrevo para desenhar pontes secretas.", email = "helena@email.com"))
            }
            posts = db.textoDao().buscarTodos()
        }
    }

    Scaffold(
        containerColor = FolioBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text("Folio", fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold, fontSize = 26.sp, modifier = Modifier.padding(start = 8.dp))
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
            modifier = Modifier.padding(innerPadding).fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(posts, key = { it.id }) { post ->
                TextoCard(post, onClick = { onTextoClick(post.id) })
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TextoCard(texto: Texto, onClick: () -> Unit) {
    var showSynopsis by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier.fillMaxWidth().combinedClickable(
            onClick = { showSynopsis = !showSynopsis },
            onDoubleClick = onClick
        ),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(44.dp).clip(CircleShape).background(Color(0xFFE0E0E0)))
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text("Autor #${texto.autorId}", fontWeight = FontWeight.Bold, color = FolioTextPrimary, fontSize = 15.sp)
                    Text("@autor_${texto.autorId}", color = FolioTextSecondary, fontSize = 13.sp)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(texto.titulo, fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold, fontSize = 22.sp, color = FolioTextPrimary)
            
            AnimatedVisibility(visible = showSynopsis) {
                Text(text = texto.conteudo, fontStyle = FontStyle.Italic, color = FolioTextSecondary, fontSize = 14.sp, lineHeight = 22.sp, fontFamily = FontFamily.Serif)
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            HorizontalDivider(color = Color(0xFFF0F0F0))
            Row(modifier = Modifier.fillMaxWidth().padding(top = 12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.FavoriteBorder, contentDescription = null, modifier = Modifier.size(20.dp), tint = FolioTextSecondary)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("0", fontSize = 13.sp, color = FolioTextSecondary)
                }
                Icon(Icons.Default.Star, contentDescription = null, modifier = Modifier.size(20.dp), tint = FolioTextSecondary)
            }
        }
    }
}

@Composable
fun SharedBottomNavigation(selectedItem: String, onNavigate: (String) -> Unit) {
    NavigationBar(containerColor = Color.White, modifier = Modifier.height(80.dp)) {
        val items = listOf(
            Triple("Feed", Icons.Filled.Description, Icons.Outlined.Description),
            Triple("Chat", Icons.Filled.ChatBubble, Icons.Outlined.ChatBubbleOutline),
            Triple("Create", Icons.Filled.Add, Icons.Outlined.Add),
            Triple("Search", Icons.Default.Search, Icons.Outlined.Search),
            Triple("Profile", Icons.Filled.Person, Icons.Outlined.Person)
        )
        items.forEach { (name, filledIcon, outlinedIcon) ->
            NavigationBarItem(
                selected = selectedItem == name,
                onClick = { onNavigate(name) },
                icon = { Icon(if (selectedItem == name) filledIcon else outlinedIcon, contentDescription = name) },
                label = { Text(name, fontSize = 10.sp) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = FolioOrange,
                    selectedTextColor = FolioOrange,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}
