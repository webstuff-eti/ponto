package com.tiagotibaes.ponto.documents

import com.tiagotibaes.ponto.controller.data.DataUtils
import com.tiagotibaes.ponto.enums.PerfilEnum
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "funcionarios")
data class Funcionario (    @Id val id: String? = null,
                            val nome: String,
                            val email: String,
                            val cpf: String,
                            val valorHora : Double? = 0.0,
                            val empresaId: String,
                            val controleHorasFuncionario: DataUtils? = null
)