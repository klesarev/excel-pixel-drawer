import java.awt.Color
import java.awt.image.BufferedImage
import java.awt.image.BufferedImage.TYPE_INT_RGB

/*
* https://stackoverflow.com/questions/16497390/illegalargumentexception-color-parameter-outside-of-expected-range-red-green-b
* But color.getRed() (Blue, Green) can return a value up to 255. So you can get the following
*  // implementation 'org.apache.poi:poi:5.0.0'
*  // implementation 'org.apache.poi:poi-ooxml:5.0.0'
*
* Get last cell index for create array
* https://stackoverflow.com/questions/29386722/apache-poi-find-last-cell-in-row?newreg=e8fb8f5b0b2f4015a7718aa08db36ab7
*/

fun main(args: Array<String>) {

    println(
        getPixelColors("D:/pix.xlsx","editor")
            .forEach { elem->
                println(elem)
            }
    )



    val res = getPixelColors("D:/pix.xlsx","editor")

    val renderedImage = renderImage(res)

    writeImage(renderedImage,"D:/final.png")

}