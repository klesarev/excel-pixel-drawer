import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.apache.poi.xssf.usermodel.XSSFColor
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.io.FileInputStream
import java.lang.Exception
import java.util.ArrayList
import javax.imageio.ImageIO
import kotlin.random.Random


fun renderImage(pixels: ArrayList<List<String>>) {
    val img = BufferedImage(100,100, BufferedImage.TYPE_INT_RGB)

    pixels.forEachIndexed { posY, row ->
        row.forEachIndexed { posX, col ->
            println("CoordX: ${(posX+1)*10} : CoordY: ${(posY+1)*10}")
            drawTile((posX+1)*10,(posY+1)*10, 10,toRGB(col).red,toRGB(col).green,toRGB(col).blue, img)
        }
    }
    writeImage(img,"D:/final.bmp")
}

fun getPixelColors(file: String, listName: String): ArrayList<List<String>> {
    val table = FileInputStream(file)
    val sheet = WorkbookFactory
        .create(table)
        .getSheet(listName)

    val rows = sheet.lastRowNum
    //val cell2 = sheet.getRow(rows).lastCellNum+rows
    val cell2 = sheet.getRow(rows).lastCellNum+rows


    val rowIterator: Iterator<Row> = sheet.iterator()
    val recordArry: MutableList<Int> = ArrayList()

    while (rowIterator.hasNext()) {
        val row: Row = rowIterator.next()

        val cellIterator = row.cellIterator()
        while (cellIterator.hasNext()) {
            val cell = cellIterator.next()
            recordArry.add(cell.address.column)
        }


    }

    println(recordArry.maxOf{el->el})
    val ccc = recordArry.maxOf{el->el}
    println("Rows: $rows == Cells: ${cell2}")

    val pixArray:Array<Array<String>> = Array(rows+1) {Array(ccc+1) {""} } // переделать размеры массива

    for (i: Row in sheet) {
        for (j: Cell in i) {
            val cellStyle = j.cellStyle
            val color = cellStyle.fillForegroundColorColor
            if (color != null && color is XSSFColor) {
                //println("${i.rowNum} % ${j.columnIndex}")
                pixArray[i.rowNum][j.columnIndex] = color.argbHex.substring(2,8)
            }
        }
    }

    val final = arrayListOf<List<String>>()

    pixArray
        .forEach { rowX ->
//            for (elem in rowX.indices) {
//                if (rowX[elem] == "") {
//                    rowX[elem] = "+"
//                }
//            }

            if (rowX.isNotEmpty()) {
                final.add(rowX.toList())
            }

            //print(rowX)
        }

    return final
}

fun drawImage(pixels: ArrayList<List<Int>>, image: BufferedImage) {
    pixels.forEachIndexed { posY, row ->
        row.forEachIndexed { posX, col ->
            when(col) {
                1 -> drawTile(posX*10,posY*10,10,255,2,0, image) // red
                2 -> drawTile(posX*10,posY*10,10,156,25,31, image) // dark red
                3 -> drawTile(posX*10,posY*10,10,255,255,255, image) // violet
                else -> drawTile(posX*10,posY*10,10,23,0,44, image) // white
            }
        }
    }
}

fun writeImage(img: BufferedImage, file: String) {
    val imgthread = Thread(Runnable {
        ImageIO.write(img, File(file).extension, File(file))
    })
    try {
        imgthread.start()
    } catch (ex: Exception) {
        ex.printStackTrace()
        imgthread.interrupt()
    }
}

fun drawTile(startX: Int, startY: Int, size: Int, red: Int, green: Int, blue: Int, image: BufferedImage) {
    for (posX in startX until startX+size) {
        for (posY in startY until startY+size) {
            drawPixel(posX,posY,red,green,blue,image)
        }
    }
}

fun drawPixel(x:Int, y:Int, red:Int, green:Int, blue: Int, image: BufferedImage) {
    image.setRGB(x, y, Color(red,green,blue).rgb)
}

fun drawRandImage(image: BufferedImage, stepSize: Int = 1, redRng: Int = 255, greenRng: Int = 255, blueRng: Int = 255) {
    for(posX in 0 until image.width step stepSize){
        for (posY in 0 until image.height step stepSize) {
            val r = if (redRng <=0) 0 else Random.nextInt(0, redRng)
            val g = if (greenRng <=0) 0 else Random.nextInt(0, greenRng)
            val b = if (blueRng <=0) 0 else Random.nextInt(0, blueRng)

            drawPixel(posX, posY, r, g, b, image)
        }
    }
}

val toRGB = { hex: String ->
    val blue: Int = hex.toInt(16) and 0xff
    val green: Int = hex.toInt(16) and 0xff00 shr 8
    val red: Int = hex.toInt(16) and 0xff0000 shr 16
    Color(red,green,blue)
}