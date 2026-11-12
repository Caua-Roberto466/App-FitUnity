package com.example.fitunity.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.fitunity.data.dao.CandidaturaDao
import com.example.fitunity.data.dao.DietaDao
import com.example.fitunity.data.dao.HistoricoDao
import com.example.fitunity.data.dao.MensagemDao
import com.example.fitunity.data.dao.NotificacaoDao
import com.example.fitunity.data.dao.PersonalAlunoDao
import com.example.fitunity.data.dao.PersonalEscolhidoDao
import com.example.fitunity.data.dao.TreinoDao
import com.example.fitunity.data.dao.UsuarioDao

class FitUnityDbHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onConfigure(db: SQLiteDatabase) {
        super.onConfigure(db)
        db.setForeignKeyConstraintsEnabled(true)
    }

    override fun onCreate(db: SQLiteDatabase) {
        DatabaseSchema.criar(db)
        DatabaseSeeder.garantirExercicios(db)
        DatabaseSeeder.garantirAlunosDemonstracao(db)
        DatabaseSeeder.garantirPersonaisDemonstracao(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        DatabaseSchema.migrar(db, oldVersion, newVersion)
        DatabaseSeeder.garantirExercicios(db)
        DatabaseSeeder.garantirAlunosDemonstracao(db)
        DatabaseSeeder.garantirPersonaisDemonstracao(db)
    }

    fun cadastrarUsuario(
        nome: String,
        email: String,
        senha: String,
        dataNascimento: String = "",
        genero: String = ""
    ): Long = usuarioDao().cadastrarUsuario(nome, email, senha, dataNascimento, genero)

    fun autenticarUsuario(email: String, senha: String): PerfilCliente? =
        usuarioDao().autenticarUsuario(email, senha)

    fun buscarPerfilPorId(id: Long): PerfilCliente? = usuarioDao().buscarPerfilPorId(id)

    fun atualizarPerfil(
        id: Long,
        nome: String,
        dataNascimento: String,
        genero: String,
        pesoAtual: Double,
        objetivoPeso: Double,
        foco: String,
        altura: Double = 1.75,
        email: String? = null
    ): Boolean = usuarioDao().atualizarPerfil(
        id, nome, dataNascimento, genero, pesoAtual, objetivoPeso, foco, altura, email
    )

    fun atualizarFotoPerfil(id: Long, caminhoArquivo: String): Boolean =
        usuarioDao().atualizarFotoPerfil(id, caminhoArquivo)

    fun atualizarTipoPlano(id: Long, tipoPlano: String): Boolean =
        usuarioDao().atualizarTipoPlano(id, tipoPlano)

    fun atualizarPreferenciasPlano(
        id: Long,
        objetivo: String,
        frequencia: String,
        restricao: String
    ): Boolean = usuarioDao().atualizarPreferenciasPlano(id, objetivo, frequencia, restricao)

    fun atualizarDieta(
        usuarioId: Long,
        metaCalorias: Int,
        refeicoesPorDia: Int,
        objetivo: String,
        restricao: String,
        observacoes: String
    ): Boolean = usuarioDao().atualizarDieta(
        usuarioId, metaCalorias, refeicoesPorDia, objetivo, restricao, observacoes
    )

    fun adicionarAgua(usuarioId: Long, ml: Int, limiteMl: Int? = null): Int =
        usuarioDao().adicionarAgua(usuarioId, ml, limiteMl)

    fun listarExercicios(): List<Exercicio> {
        DatabaseSeeder.garantirExercicios(writableDatabase)
        return treinoDao().listarExercicios()
    }

    fun buscarExercicio(id: Int): Exercicio? {
        DatabaseSeeder.garantirExercicios(writableDatabase)
        return treinoDao().buscarExercicio(id)
    }

    fun listarTreinos(usuarioId: Long): List<TreinoDia> {
        DatabaseSeeder.garantirDadosUsuario(writableDatabase, usuarioId)
        return treinoDao().listarTreinos(usuarioId)
    }

    fun criarTreino(
        usuarioId: Long,
        diaSemana: String,
        titulo: String,
        descricao: String,
        duracao: String,
        imagem: Int,
        foco: String,
        exercicioIds: List<Int>
    ): Long = treinoDao().criarTreino(
        usuarioId, diaSemana, titulo, descricao, duracao, imagem, foco, exercicioIds
    )

    fun atualizarTreino(
        usuarioId: Long,
        treinoId: Int,
        diaSemana: String,
        titulo: String,
        descricao: String,
        duracao: String,
        imagem: Int,
        foco: String,
        exercicioIds: List<Int>
    ): Boolean = treinoDao().atualizarTreino(
        usuarioId, treinoId, diaSemana, titulo, descricao, duracao, imagem, foco, exercicioIds
    )

    fun removerTreino(usuarioId: Long, treinoId: Int): Boolean =
        treinoDao().removerTreino(usuarioId, treinoId)

    fun listarRefeicoes(usuarioId: Long): List<RefeicaoDia> {
        DatabaseSeeder.garantirDadosUsuario(writableDatabase, usuarioId)
        val dao = dietaDao()
        dao.garantirPlanoDoDia(usuarioId, dataDeHoje(), planoDoDia())
        return dao.listarRefeicoes(usuarioId)
    }

    fun buscarRefeicao(usuarioId: Long, refeicaoId: Long): RefeicaoDia? =
        dietaDao().buscarRefeicao(usuarioId, refeicaoId)

    fun concluirRefeicao(usuarioId: Long, refeicaoId: Long): Boolean =
        dietaDao().concluirRefeicao(usuarioId, refeicaoId)

    fun listarNotificacoes(usuarioId: Long): List<Notificacao> {
        DatabaseSeeder.garantirDadosUsuario(writableDatabase, usuarioId)
        return notificacaoDao().listarNotificacoes(usuarioId)
    }

    fun marcarNotificacaoLida(usuarioId: Long, notificacaoId: Long): Boolean =
        notificacaoDao().marcarNotificacaoLida(usuarioId, notificacaoId)

    fun listarHistorico(usuarioId: Long): List<HistoricoTreino> {
        DatabaseSeeder.garantirDadosUsuario(writableDatabase, usuarioId)
        return historicoDao().listarHistorico(usuarioId)
    }

    fun registrarTreinoConcluido(
        usuarioId: Long,
        titulo: String,
        duracao: String,
        calorias: String,
        data: String = "Hoje"
    ): Long = historicoDao().registrarTreinoConcluido(usuarioId, titulo, duracao, calorias, data)

    fun listarMensagens(usuarioId: Long): List<MensagemChat> {
        DatabaseSeeder.garantirDadosUsuario(writableDatabase, usuarioId)
        return mensagemDao().listarMensagens(usuarioId)
    }

    fun enviarMensagem(usuarioId: Long, texto: String): MensagemChat? {
        DatabaseSeeder.garantirDadosUsuario(writableDatabase, usuarioId)
        return mensagemDao().enviarMensagem(usuarioId, texto)
    }

    fun buscarCandidatura(usuarioId: Long): CandidaturaPersonal? =
        candidaturaDao().buscarPorUsuario(usuarioId)

    fun salvarCandidatura(candidatura: CandidaturaPersonal): CandidaturaPersonal =
        candidaturaDao().salvar(candidatura)

    fun atualizarStatusCandidatura(usuarioId: Long, status: StatusCandidatura): Boolean =
        candidaturaDao().atualizarStatus(usuarioId, status)

    fun removerCandidatura(usuarioId: Long): Boolean =
        candidaturaDao().remover(usuarioId)

    fun listarPersonaisDisponiveis(): List<CandidaturaPersonal> {
        DatabaseSeeder.garantirPersonaisDemonstracao(writableDatabase)
        return candidaturaDao().listarAprovados()
    }

    fun buscarPersonalEscolhido(alunoId: Long): CandidaturaPersonal? {
        DatabaseSeeder.garantirPersonaisDemonstracao(writableDatabase)
        return personalEscolhidoDao().buscarPersonalEscolhido(alunoId)
    }

    fun escolherPersonal(alunoId: Long, personalId: Long): Boolean {
        DatabaseSeeder.garantirPersonaisDemonstracao(writableDatabase)
        return personalEscolhidoDao().escolher(alunoId, personalId)
    }

    fun removerPersonalEscolhido(alunoId: Long): Boolean =
        personalEscolhidoDao().remover(alunoId)

    fun contarAlunosDoPersonal(personalId: Long): Int =
        personalEscolhidoDao().contarAlunos(personalId)

    fun alunoEscolheuPersonal(alunoId: Long, personalId: Long): Boolean =
        personalEscolhidoDao().alunoEscolheu(alunoId, personalId)

    fun listarAlunosElegiveis(personalId: Long): List<AlunoPersonal> {
        DatabaseSeeder.garantirAlunosDemonstracao(writableDatabase)
        return personalAlunoDao().listarAlunosElegiveis(personalId)
    }

    fun buscarAlunoElegivel(personalId: Long, alunoId: Long): AlunoPersonal? =
        personalAlunoDao().buscarAlunoDoPersonal(personalId, alunoId)

    fun listarMensagensAluno(personalId: Long, alunoId: Long): List<MensagemChat> =
        personalAlunoDao().listarMensagens(personalId, alunoId)

    fun enviarMensagemAluno(personalId: Long, alunoId: Long, texto: String): MensagemChat? =
        personalAlunoDao().enviarMensagem(personalId, alunoId, texto)

    fun ultimaMensagemAluno(personalId: Long, alunoId: Long): MensagemChat? =
        personalAlunoDao().ultimaMensagem(personalId, alunoId)

    fun listarMensagensDoAluno(alunoId: Long, personalId: Long): List<MensagemChat> =
        personalAlunoDao().listarMensagensDoAluno(alunoId, personalId)

    fun enviarMensagemDoAluno(alunoId: Long, personalId: Long, texto: String): MensagemChat? =
        personalAlunoDao().enviarMensagemDoAluno(alunoId, personalId, texto)

    fun ultimaMensagemDoAluno(alunoId: Long, personalId: Long): MensagemChat? =
        personalAlunoDao().ultimaMensagemDoAluno(alunoId, personalId)

    private fun usuarioDao() = UsuarioDao(writableDatabase)
    private fun treinoDao() = TreinoDao(writableDatabase)
    private fun dietaDao() = DietaDao(writableDatabase)
    private fun notificacaoDao() = NotificacaoDao(writableDatabase)
    private fun historicoDao() = HistoricoDao(writableDatabase)
    private fun mensagemDao() = MensagemDao(writableDatabase)
    private fun candidaturaDao() = CandidaturaDao(writableDatabase)
    private fun personalAlunoDao() = PersonalAlunoDao(writableDatabase)
    private fun personalEscolhidoDao() = PersonalEscolhidoDao(writableDatabase)

    companion object {
        private const val DATABASE_NAME = "fitunity.db"
        private const val DATABASE_VERSION = 7
    }
}
