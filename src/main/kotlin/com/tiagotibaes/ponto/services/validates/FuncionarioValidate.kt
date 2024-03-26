package com.tiagotibaes.ponto.services.validates

import com.tiagotibaes.ponto.controller.lancamento.dto.LancamentoDtoResquest
import com.tiagotibaes.ponto.documents.Funcionario
import com.tiagotibaes.ponto.services.FuncionarioService
import org.springframework.stereotype.Component
import org.springframework.validation.BindingResult
import org.springframework.validation.ObjectError

@Component
class FuncionarioValidate(val funcionarioService: FuncionarioService) {

    public fun validarFuncionario(lancamentoDtoResquest: LancamentoDtoResquest, result: BindingResult){
        if(lancamentoDtoResquest.funcionarioId == null){
            result.addError(ObjectError("funcionário", "Funcionário não informado"))
            return
        }

        val funcionario: Funcionario? = funcionarioService.buscarPorId(lancamentoDtoResquest.funcionarioId)
        if(funcionario == null){
            result.addError(ObjectError("funcionário", "Funcionário não encontrado. ID inexistente"))
        }
    }

}