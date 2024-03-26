package com.tiagotibaes.ponto.utils

import org.springframework.stereotype.Component
import java.text.SimpleDateFormat
import java.util.*

@Component
class ConverterDates {

     fun converterStringToDate(testeData: String) : Date {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")  //dd/MM/yyyy
        val dateConverted: Date = formatter.parse(testeData)
        return dateConverted;
     }

     fun converterDateToString(testeData: Date) : String {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")  //dd/MM/yyyy
        val dateConverted: String = formatter.format(testeData)
        return dateConverted;
     }

}