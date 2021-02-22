import java.util.ArrayList

/*
* Created by MrFox
*/

suspend fun main() {

    // получаем массив из xlsx файла - лист "editor"
    val res = getPixelColors("D:/pix.xlsx","editor")

    // получаем отрендеренное изображение
    val renderedImage = renderImage(res, 5)

    writeImage(renderedImage,"D:/test.png")

    val px = pixelsFromImage("D:/test.png")
    px.forEach {
        println(it)
    }

    renderExcel(px,"D:/text.xlsx")

}