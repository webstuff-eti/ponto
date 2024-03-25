package com.tiagotibaes.ponto.services.impl

import com.tiagotibaes.ponto.documents.Lancamento
import com.tiagotibaes.ponto.enums.TypeEnum
import com.tiagotibaes.ponto.repositories.LancamentoRepository
import com.tiagotibaes.ponto.services.LancamentoService

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito
import org.mockito.Mockito

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.ActiveProfiles

import java.util.*

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureDataMongo
class LancamentoServiceTest {

    @MockBean
    private val lancamentoRepository: LancamentoRepository? = null

    @Autowired
    private val lancamentoService: LancamentoService? = null

    private val id: String = "1"


    @BeforeEach
    @Throws(Exception::class)
    fun setUp() {
        BDDMockito.given(lancamentoRepository?.save(Mockito.any(Lancamento::class.java)))
            .willReturn(lancamento())
        BDDMockito.given<Page<Lancamento>>(lancamentoRepository?.findByFuncionarioId("1", PageRequest.of(0, 10)))
            .willReturn(PageImpl(ArrayList<Lancamento>()))
        BDDMockito.given(lancamentoRepository?.findById("1"))
            .willReturn(Optional.of(lancamento()))
    }

    @Test
    fun testBuscarLancamentoPorFuncionarioId() {
        val lancamento: Page<Lancamento>? = lancamentoService?.buscarPorFuncionarioId(id, PageRequest.of(0, 10))
        Assertions.assertNotNull(lancamento)
    }

    @Test
    fun testBuscarLancamentoPorId() {
        val lancamento: Lancamento? = lancamentoService?.buscarPorId(id)
        Assertions.assertNotNull(lancamento)
    }

    @Test
    fun testPersistirLancamento() {
        val lancamento: Lancamento? = lancamentoService?.criar(lancamento())
        Assertions.assertNotNull(lancamento)
    }


    private fun lancamento(): Lancamento = Lancamento("1",
        "Lançamento horas extras de Sábado",
        "Home Office",
        TypeEnum.INICIO_TRABALHO,
        Date(),
        "1"
    )


}