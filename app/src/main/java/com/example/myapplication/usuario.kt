package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.*

data class Publication(val id: Int, val category: String, val title: String, val time: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsuarioScreen(onNavigate: (String) -> Unit) {
    val publications = remember {
        mutableStateListOf(
            Publication(1, "POESIA", "Sussurros do Crepúsculo", "Há 2 dias"),
            Publication(2, "CRÔNICA", "A Biblioteca Invisível", "Há 1 semana")
        )
    }
    
    var newPubTitle by remember { mutableStateOf("") }

    Scaffold(
        containerColor = FolioBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Autor",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 26.sp,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(
                            Icons.Outlined.Notifications,
                            contentDescription = "Notifications",
                            tint = Color.Black
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = FolioBackground)
            )
        },
        bottomBar = {
            SharedBottomNavigation(selectedItem = "Profile", onNavigate = onNavigate)
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(16.dp)
        ) {
            // Header do Perfil
            item {
                Spacer(modifier = Modifier.height(16.dp))
                // Avatar Placeholder
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE0E0E0)),
                    contentAlignment = Alignment.Center
                ) {
                    // Placeholder para imagem do autor
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Helena Andrade",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = FolioTextPrimary
                )

                Text(
                    text = "@helena_escreve",
                    color = FolioOrange,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "\"Escrevo para desenhar pontes secretas no silêncio do mundo cotidiano.\"",
                    fontStyle = FontStyle.Italic,
                    color = FolioTextSecondary,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 32.dp),
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Stats
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    ) {
                        Text("2.4k", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = FolioTextPrimary)
                        Text("Leitores", fontSize = 12.sp, color = FolioTextSecondary)
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    ) {
                        Text("340", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = FolioTextPrimary)
                        Text("Lendo", fontSize = 12.sp, color = FolioTextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Botão Editar Perfil com ícone de lápis
                Button(
                    onClick = { onNavigate("EditProfile") },
                    colors = ButtonDefaults.buttonColors(containerColor = FolioOrange),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                        .height(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar",
                        modifier = Modifier.size(18.dp),
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Editar Perfil",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))
            }

            // Tabs
            item {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Publicações",
                            color = FolioOrange,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(2.dp)
                                .background(FolioOrange)
                        )
                    }
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Favoritos",
                            color = FolioTextSecondary,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(Color(0xFFEEEEEE))
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
            
            // Add Publication Form


            // Lista de Publicações
            items(publications, key = { it.id }) { pub ->
                PublicationCard(
                    publication = pub,
                    onDelete = { publications.remove(pub) },
                    onClick = { onNavigate("PostDetail") } // Usando PostDetail como exemplo de Detalhes
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun PublicationCard(publication: Publication, onDelete: () -> Unit, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF5F5F5)),
        onClick = onClick
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = publication.category,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = publication.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = FolioTextPrimary,
                        fontFamily = FontFamily.Serif
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = publication.time,
                        fontSize = 11.sp,
                        color = Color.LightGray
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Remover", tint = Color.Gray)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UsuarioPreview() {
    MyApplicationTheme {
        UsuarioScreen(onNavigate = {})
    }
}
