import java.util.ArrayList

/*
* Created by MrFox
*/

suspend fun main() {

    // получаем массив из xlsx файла - лист "editor"
    val res = getPixelColors("D:/pix.xlsx","editor")

    // получаем отрендеренное изображение
    val renderedImage = renderImage(res, 5)

    // записываем изобаражение
    writeImage(renderedImage,"D:/test.png")

    // получаем массив пикселей
    val px = pixelsFromImage("D:/test.png")

    // рендерим изображение в excel файл
    renderExcel(px,"D:/text.xlsx")

}