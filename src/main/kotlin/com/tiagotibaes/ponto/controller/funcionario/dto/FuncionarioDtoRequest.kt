package com.tiagotibaes.ponto.controller.funcionario.dto


import com.tiagotibaes.ponto.controller.data.DataUtils

import org.hibernate.validator.constraints.Length
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty

data class FuncionarioDtoRequest (

    val id: String? = null,
    @get:NotEmpty(message = "Nome não pode ser vazio.")
    @get:Length(min = 3, max = 200, message = "Nome deve conter entre 3 e 200 caracteres.")
    val nome: String = "",
    @get:NotEmpty(message = "Email não pode ser vazio.")
    @get:Length(min = 5, max = 200, message = "Email deve conter entre 5 e 200 caracteres.")
    @get:Email(message="Email inválido.")
    val email: String = "",
    val cpf: String,
    val valorHora : String? = null,
    val empresaId: String,
    val controleHorasFuncionario: DataUtils? = null
)

