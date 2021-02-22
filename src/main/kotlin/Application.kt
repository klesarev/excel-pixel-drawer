import java.util.ArrayList

/*
* Created by MrFox
*/

fun main() {

    // получаем массив из xlsx файла - лист "editor"
    val res = getExcelPixels("D:/pix.xlsx","editor")

    // получаем отрендеренное изображение
    renderImage(res, file="D:/test.png", pixelSize = 2)

    val pixels = getImagePixels("D:/test.png")

    renderExcel(pixels,"list","D:/test.xlsx")

}