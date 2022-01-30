package ru.kexan

import java.awt.Toolkit

import java.awt.datatransfer.DataFlavor.stringFlavor
import java.awt.datatransfer.StringSelection
import java.awt.datatransfer.UnsupportedFlavorException
import java.lang.Thread.sleep
import java.sql.Timestamp
import java.util.*


fun main() {

    val scanner = Scanner(System.`in`)
    val clipboard = Toolkit.getDefaultToolkit().systemClipboard
    var boolean = false

    clipboard.addFlavorListener {
        try {
            boolean = !boolean
            //FIXME два раза вызывается листенер т.к. в нем же инфа копируется в буфер, придумать фикс
            //FIXME Можно попробовать сделать листенер на кей-комбо типа ctrl+c
            if (boolean) {
                val clipboardContent = convert(clipboard.getData(stringFlavor) as String)
                sleep(200)
                clipboard.setContents(StringSelection(clipboardContent), null)
                println("\n $clipboardContent \n")
            }
        } catch (e: IllegalStateException) {
            val timestamp = Timestamp(System.currentTimeMillis())
            println("$timestamp Ошибка! Нет доступа к буферу обмена, пробую еще раз...")
            return@addFlavorListener
        } catch (e: UnsupportedFlavorException) {
            val timestamp = Timestamp(System.currentTimeMillis())
            println("$timestamp Ошибка! Неподдерживаемый тип данных, копируйте строки")
            return@addFlavorListener
        } catch (e: Exception) {
            val timestamp = Timestamp(System.currentTimeMillis())
            val text = clipboard.getData(stringFlavor) as String
            println("$timestamp Неизвестная ошибка! Попробуйте еще раз. Запрос:")
            println(text)
            return@addFlavorListener
        }
    }

    println("Программу можно свернуть, просто копируйте текст.")
    while (true) {
        scanner.nextLine()
    }

}

fun convert(text: String): String {

    val convertedText = text
        .split("\n")
        .toMutableList()

    if (!text.contains("НФ-")) {
        return text
    }

    convertedText.forEachIndexed { index, string ->
        convertedText[index] = string.substring(12)
            .replace("(1)", "")
            .replace("(2)", "")
            .replace("(3)", "")
            .replace("(4)", "")
            .replace("(5)", "")
            .replace("(термопакет)", "")
            .replace("(коробочка)", "")
            .replace(",00", "")
            .replace(" шт", "-") //FIXME удаляет все проявления "шт", надо исправить
            .plus(" р. \n")
    }

    var formattedText = ""
    for (string in convertedText.toMutableSet()) {
        formattedText += string
    }

    return formattedText
}