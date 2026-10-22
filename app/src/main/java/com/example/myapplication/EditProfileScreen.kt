package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(onBack: () -> Unit, onSave: () -> Unit, onNavigate: (String) -> Unit) {
    val context = LocalContext.current
    val db = AppDatabase.getInstance(context)
    val scope = rememberCoroutineScope()

    var usuario by remember { mutableStateOf<Usuario?>(null) }
    var name by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            val user = db.usuarioDao().buscarTodos().firstOrNull()
            usuario = user
            user?.let {
                name = it.nome
                bio = it.bio
                email = it.email
            }
        }
    }

    Scaffold(
        containerColor = FolioBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Editar perfil",
                        fontFamily = FontFamily.Serif,
                        fontStyle = FontStyle.Italic,
                        fontSize = 28.sp,
                        color = FolioTextPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = FolioTextPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = FolioBackground)
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))

                // Profile Picture Section
                Box(contentAlignment = Alignment.BottomEnd) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE0E0E0)),
                        contentAlignment = Alignment.Center
                    ) {
                        // Placeholder for actual image
                    }
                    Surface(
                        shape = CircleShape,
                        color = Color.White,
                        shadowElevation = 2.dp,
                        modifier = Modifier.size(32.dp).offset(x = 4.dp, y = 0.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                Icons.Default.CameraAlt,
                                contentDescription = "Change Photo",
                                tint = FolioOrange,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Alterar foto",
                    color = FolioOrange,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Form Fields
                EditField(
                    label = "Nome",
                    value = name,
                    onValueChange = { name = it }
                )

                Spacer(modifier = Modifier.height(20.dp))

                EditField(
                    label = "Email",
                    value = email,
                    onValueChange = { email = it },
                    helperText = "Seu endereço de e-mail privado."
                )

                Spacer(modifier = Modifier.height(20.dp))

                EditField(
                    label = "Biografia",
                    value = bio,
                    onValueChange = { if (it.length <= 160) bio = it },
                    isMultiline = true,
                    helperText = "Um pouco de você, em poucas palavras.",
                    counterText = "${bio.length} / 160",
                    textStyle = TextStyle(
                        fontFamily = FontFamily.Serif,
                        fontStyle = FontStyle.Italic,
                        fontSize = 16.sp,
                        color = FolioTextPrimary,
                        lineHeight = 24.sp
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                    Icon(
                        Icons.Outlined.Info,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Estas informações aparecem no seu perfil público.",
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.height(40.dp))

                // Buttons
                Button(
                    onClick = {
                        usuario?.let {
                            scope.launch(Dispatchers.IO) {
                                val updatedUser = it.copy(nome = name, bio = bio, email = email)
                                db.usuarioDao().update(updatedUser)
                                withContext(Dispatchers.Main) {
                                    onSave()
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = FolioOrange),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Salvar alterações", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }

                TextButton(
                    onClick = onBack,
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    Text("Cancelar", color = Color.Gray, fontSize = 16.sp)
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun EditField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
    helperText: String? = null,
    counterText: String? = null,
    isMultiline: Boolean = false,
    textStyle: TextStyle = TextStyle(
        fontSize = 16.sp,
        color = FolioTextPrimary
    )
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = FolioTextPrimary,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color(0xFFE0E0E0),
                focusedBorderColor = FolioOrange,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            ),
            textStyle = textStyle,
            leadingIcon = leadingIcon,
            minLines = if (isMultiline) 4 else 1,
            keyboardOptions = if (isMultiline) KeyboardOptions.Default else KeyboardOptions(keyboardType = KeyboardType.Text)
        )

        if (helperText != null || counterText != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (helperText != null) {
                    Text(helperText, color = Color.Gray, fontSize = 12.sp)
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }
                if (counterText != null) {
                    Text(counterText, color = Color.Gray, fontSize = 12.sp)
                }
            }
        }
    }
}
