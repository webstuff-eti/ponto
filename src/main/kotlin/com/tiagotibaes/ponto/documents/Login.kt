package com.tiagotibaes.ponto.documents

import com.tiagotibaes.ponto.enums.PerfilEnum
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "logins")
data class Login (@Id val id: String? = null,
                  val funcionarioId: String,
                  val perfil: PerfilEnum,
                  val senha: String,
)