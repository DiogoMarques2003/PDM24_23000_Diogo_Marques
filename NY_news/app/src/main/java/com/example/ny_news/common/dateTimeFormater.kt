package com.example.ny_news.common

fun dateTimeFormater(dateTime: String): String {
    val dateTimeParts = dateTime.split("T")
    // Se não tiver duas partes quer dizer que não é dataThora
    if (dateTimeParts.size != 2) {
        return dateTime
    }

    return "${dateFormater(dateTimeParts[0])} ${timeFormater(dateTimeParts[1])}"
}

private fun dateFormater(date: String): String {
    val dateParts = date.split("-")
    // Se não tiver 3 posições é porque não é um formato correto
    if (dateParts.size != 3) {
        return date
    }

    return "${dateParts[2]}/${dateParts[1]}/${dateParts[0]}"
}

private fun timeFormater(time: String): String {
    return time.split("-")[0]
}