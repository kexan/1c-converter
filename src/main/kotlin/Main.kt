package ru.kexan

import java.awt.Toolkit
import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.StringSelection
import java.awt.datatransfer.Transferable
import java.util.*

fun main() {
    while (true) {
        val scanner = Scanner(System.`in`)
        println("Выберите режим:\n1. Для клиентов\n2. Прайс\n3. Тест")
        val mode = scanner.nextInt()
        while (scanner.nextLine() != "end") {
            println("Скопируй текст в буфер и нажми Enter для конвертации:")
            scanner.nextLine()
            val convertedText = convert(
                Toolkit.getDefaultToolkit()
                    .systemClipboard.getData(DataFlavor.stringFlavor) as String, mode
            )
            println(convertedText)
            val clipboard: Clipboard = Toolkit.getDefaultToolkit().systemClipboard
            val transferable: Transferable = StringSelection(convertedText)
            clipboard.setContents(transferable, null)
            println("Текст скопирован обратно в буфер. Нажми Enter для повторной конвертации или 'end' для выбора режима'")
        }
    }
}

fun convert(text: String, mode: Int): String {
    val convertedText: List<String>

    when (mode) {
        1 -> {
            convertedText = text.replace("(1)", "")
                .replace("(2)", "")
                .replace("(3)", "")
                .replace("(4)", "")
                .replace("(5)", "")
                .replace("(термопакет)", "")
                .replace("(коробочка)", "")
                .replace(",00", "")
                .replace("шт", "-")
                .split("\n")
                .distinct()
        }
        2 -> {
            convertedText = text
                .replace("(1)", "")
                .replace("(2)", "")
                .replace("(3)", "")
                .replace("(4)", "")
                .replace("(5)", "")
                .replace(",00", "")
                .replace("шт", "-")
                .split("\n")
                .distinct()
        }
        else -> {
            convertedText = text
                .replace(",00", "")
                .replace("шт", "-")
                .replace(",0", " шт")
                .split("\n")
                .filter { it.contains("(1)") ||
                        it.contains("(2)") ||
                        it.contains("(3)") ||
                        it.contains("(4)") }
                .filterNot { it.contains("шт") }
        }
    }
    var formattedText = ""
    for (string in convertedText) {
        formattedText += if (mode != 3) "$string р.\n" else "$string\n"
    }
    return formattedText
}