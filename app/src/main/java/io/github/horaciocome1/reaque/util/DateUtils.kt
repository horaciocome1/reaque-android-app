/*
 *    Copyright 2019 Horácio Flávio Comé Júnior
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and limitations under the License.
 */

package io.github.horaciocome1.reaque.util

import com.google.firebase.Timestamp
import java.util.*

val Timestamp.string: String
    get() {
        val calendar = Calendar.getInstance()
        calendar.time = toDate()
        return calendar.month +
                " ${calendar[Calendar.DAY_OF_MONTH]}," +
                " ${calendar[Calendar.YEAR]}"
    }

val Calendar.month: String
    get() = when (this[Calendar.MONTH]) {
        Calendar.JANUARY -> "Janeiro"
        Calendar.FEBRUARY -> "Fevereiro"
        Calendar.MARCH -> "Março"
        Calendar.APRIL -> "Abril"
        Calendar.MAY -> "Maio"
        Calendar.JUNE -> "Junho"
        Calendar.JULY -> "Julho"
        Calendar.AUGUST -> "Agosto"
        Calendar.SEPTEMBER -> "Setembro"
        Calendar.OCTOBER -> "Outubro"
        Calendar.NOVEMBER -> "Novembro"
        Calendar.DECEMBER -> "Dezembro"
        else -> "month"
    }