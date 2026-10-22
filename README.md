# Trabalho-3_rede_social_de_escritor

Você é um desenvolvedor Android sênior especialista em Kotlin + Room.

CONTEXTO DO PROJETO:
Estou fazendo um trabalho de faculdade: um app Android de REDE SOCIAL DE ESCRITORES.
O usuário pode criar textos/escritos e publicá-los, e esses textos devem aparecer:
  1) no FEED (lista de todos os textos publicados) que esta localizado no MainActivity.kt
  2) no PERFIL do autor (lista dos textos que ELE publicou) que esta localizado no usuario.kt

O banco de dados é 100% LOCAL (SQLite via Room). NÃO deve envolver login, servidor
remoto, API, Firebase ou sincronização em nuvem.

Não é exigido padrão rígido de arquitetura (MVVM, Clean Architecture, etc.) — o que
importa é o Room funcionando de verdade, bem integrado com as telas que já existem.

O QUE PRECISO QUE VOCÊ ENTREGUE:


2. NO MÍNIMO 2 @Entity com @PrimaryKey(autoGenerate = true). Sugestão:
   - Entity "Usuario" (id, nome, bio, email)
   - Entity "Texto" (id, titulo, conteudo, autorId, dataCriacao)
   - Se fizer sentido, um relacionamento via chave estrangeira (ForeignKey) entre Texto → Usuario.
     Se não fizer sentido para simplificar, pode deixar solto com autorId Int.

3. NO MÍNIMO 2 @Dao (interfaces), CADA UM com:
   - @Insert (suspend)
   - @Query (suspend) — pelo menos um buscarTodos()
   - @Update (suspend)
   - @Delete (suspend)
   Pelo menos UM DAO deve ter consulta filtrada por campo, ex.:
     @Query("SELECT * FROM texto WHERE id = :id") suspend fun buscarPorId(id: Int): Texto?
   E também:
     @Query("SELECT * FROM texto WHERE autorId = :autorId") suspend fun buscarPorAutor(autorId: Int): List<Texto>
   (para o perfil do autor)

4. CLASSE AppDatabase em PADRÃO SINGLETON:
   - @Database(entities = [...], version = 1, exportSchema = false)
   - abstract fun usuarioDao(): UsuarioDao
   - abstract fun textoDao(): TextoDao
   - companion object com @Volatile instance + getInstance(context) usando synchronized
   - Se quiser, deixe pronto um .fallbackToDestructiveMigration()

5. INTEGRAÇÃO COM AS TELAS:
   - Todas as operações de banco DEVEM rodar dentro de uma coroutine em Dispatchers.IO
     (use viewModelScope.launch(Dispatchers.IO) { ... } ou lifecycleScope.launch(Dispatchers.IO) { ... })
   - As LISTAS (feed e perfil) devem buscar os dados do BANCO ao abrir a tela — NÃO de
     uma lista fixa em memória.
   - A tela de DETALHES deve buscar o item DIRETO do banco pelo id:
        val texto = dao.buscarPorId(idRecebidoViaIntent)
   - Adicionar um texto pela UI (botão "Publicar") deve salvar de verdade no banco.
   - Editar um texto pela UI deve atualizar de verdade e persistir.
   - Remover um texto pela UI deve apagar de verdade do banco.

6. FLUXO ESPERADO (rede social de escritores):
   - Usuário preenche título + conteúdo de um texto e clica em "Publicar"
     → dao.insert(texto) com autorId do usuário logado (local)
     → texto aparece no Feed E no Perfil do autor automaticamente
   - O Feed usa dao.buscarTodos()
   - O Perfil usa dao.buscarPorAutor(autorId)

7. CHECKLIST que a entrega DEVE cumprir:
   [x] Room configurado e projeto compila
   [x] ≥2 @Entity com @PrimaryKey(autoGenerate = true)
   [x] ≥2 @Dao com @Insert, @Query, @Update, @Delete (todos suspend)
   [x] Pelo menos 1 consulta filtrada por campo (buscarPorId + buscarPorAutor)
   [x] Tela de Detalhes busca do banco pelo id
   [x] AppDatabase em Singleton
   [x] Listas buscam do banco ao abrir a tela
   [x] Adicionar item salva no banco
   [x] Editar item persiste
   [x] Remover item apaga do banco
   [x] Persistência sobrevive ao fechar/reabrir o app
   [x] Toda operação em coroutine com Dispatchers.IO

FORMATO DA RESPOSTA:
- Mostre o código de: build.gradle.kts (dependências), Entidades, DAOs, AppDatabase.
- Depois mostre o código Kotlin das telas (ViewModel/Fragment/Activity) mostrando
  a chamada das funções do DAO dentro de coroutines em Dispatchers.IO.
- Explique em passos objetivos o que mudar em cada tela existente (Feed, Perfil,
  Detalhes, Cadastro/Publicar, Editar).
- Se algo do projeto atual entrar em conflito (ex.: lista hardcoded), mostre antes/depois.
- NÃO invente API, servidor, login ou nuvem. Só Room local.
