import java.util.ArrayList

/*
* Created by MrFox
*/

suspend fun main() {


    // получаем массив из xlsx файла - лист "editor"
    val res = getPixelColors("D:/pix.xlsx","editor")

    // получаем отрендеренное изображение
    val renderedImage = renderImage(res, 30)

    // записываем изображение
    writeImage(renderedImage,"D:/final2322.png")


    renderExcel(res)

}