import java.awt.Color
import java.awt.image.BufferedImage
import java.awt.image.BufferedImage.TYPE_INT_ARGB
import java.awt.image.BufferedImage.TYPE_INT_RGB

/*
* Created by MrFox
*/

fun main(args: Array<String>) {


    // получаем массив из xlsx файла - лист "editor"
    val res = getPixelColors("D:/pix.xlsx","editor")

    // получаем отрендеренное изображение
    val renderedImage = renderImage(res)

    // записываем изображение
    writeImage(renderedImage,"D:/final2322.png")

}