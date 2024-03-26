package com.tiagotibaes.ponto.documents


import com.tiagotibaes.ponto.enums.TypeEnum
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.Date


@Document(collection = "lancamentos")
data class Lancamento(@Id val id: String? = null,
                      val descricao: String? = "",
                      val localizacao: String? = "",
                      val tipo: TypeEnum,
                      val data: Date?,
                      val funcionarioId: String

)