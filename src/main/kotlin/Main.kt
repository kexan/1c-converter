package ru.kexan

import java.awt.Toolkit
import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.DataFlavor.stringFlavor
import java.awt.datatransfer.StringSelection
import java.awt.datatransfer.UnsupportedFlavorException
import java.sql.Timestamp
import java.util.*


fun main() {
    val scanner = Scanner(System.`in`)
    val clipboard = Toolkit.getDefaultToolkit().systemClipboard
    clipboard.addFlavorListener {
        with(clipboard) {
            try {
                convertClipboardContents(this)
            } catch (e: IllegalStateException) {
                val timestamp = Timestamp(System.currentTimeMillis())
                println("$timestamp Ошибка! Нет доступа к буферу обмена, пробую еще раз...")
                convertClipboardContents(this)
            } catch (e: UnsupportedFlavorException) {
                val timestamp = Timestamp(System.currentTimeMillis())
                println("$timestamp Ошибка! Неподдерживаемый тип данных, копируйте строки")
                convertClipboardContents(this)
            } catch (e: Exception) {
                val timestamp = Timestamp(System.currentTimeMillis())
                println("$timestamp Неизвестная ошибка! Попробуйте еще раз. Запрос:")
                convertClipboardContents(this)
            }
        }
    }
    println("Программу можно свернуть, просто копируйте текст.")
    while (true) {
        scanner.nextLine()
    }
}

fun convertClipboardContents(clipboard: Clipboard): String {
    val clipboardContent = clipboard.getData(stringFlavor) as String
    if (!clipboardContent.contains("00-")) {
        return clipboardContent
    }
    val convertedText = clipboardContent
        .split("\n")
        .toMutableList()
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
            .replace("шт", "-") //FIXME удаляет все проявления "шт", надо исправить
            .plus(" р. \n")
    }
    var formattedText = ""
    for (string in convertedText.toMutableSet()) {
        formattedText += string
    }
    clipboard.setContents(StringSelection(formattedText), null)
    return formattedText
}