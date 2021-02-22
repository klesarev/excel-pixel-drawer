import org.apache.poi.ss.usermodel.*
import java.awt.Color
import java.awt.image.BufferedImage
import java.lang.Exception
import java.util.ArrayList
import javax.imageio.ImageIO
import org.apache.poi.xssf.usermodel.*
import org.apache.poi.xssf.usermodel.XSSFColor
import java.io.*
import javax.imageio.stream.ImageInputStream
import org.apache.poi.hssf.usermodel.HeaderFooter.file
import org.apache.xalan.xsltc.util.IntegerArray


fun drawPixel(x:Int, y:Int, red:Int, green:Int, blue: Int, alpha: Int, image: BufferedImage) {
    image.setRGB(x, y, Color(red,green,blue,alpha).rgb)
}

fun drawTile(startX: Int, startY: Int, size: Int, red: Int, green: Int, blue: Int, alpha: Int, image: BufferedImage) {
    for (posX in startX until startX+size) {
        for (posY in startY until startY+size) {
            drawPixel(posX,posY,red,green,blue,alpha,image)
        }
    }
}

fun renderImage(pixels: ArrayList<List<String>>, pxSize: Int = 10): BufferedImage {

    val pixelSize = if(pxSize > 0) pxSize else 10

    val resultImage = BufferedImage(
        pixels[0].size * pixelSize,
        pixels.size * pixelSize,
        BufferedImage.TYPE_INT_ARGB
    )

    pixels.forEachIndexed { posY, row ->
        row.forEachIndexed { posX, col ->
            drawTile(
                (posX) * pixelSize,(posY) * pixelSize, pixelSize,
                toRGBA(col).red, toRGBA(col).green,toRGBA(col).blue,toRGBA(col).alpha,
                resultImage
            )
        }
    }
    return resultImage
}

fun renderSVG(pixels: ArrayList<List<String>>): String {
    return "soon..."
}

fun pixelsFromImage(file: String):ArrayList<List<String>> {

    val image: BufferedImage = ImageIO.read(File(file))

    val pixArray:Array<Array<String>> = Array(image.width) {Array(image.height) {""} }

    for (posX in 0 until image.width) {
        for (posY in 0 until image.height) {

            val pixColor: Int = image.getRGB(posY, posX)

            val red = pixColor shr 16 and 255
            val green = pixColor shr 8 and 255
            val blue = pixColor and 255

            pixArray[posX][posY] = Integer.toHexString(Color(red,green,blue).rgb).substring(2)
        }
    }

    val pixelMatrix = arrayListOf<List<String>>()

    pixArray.forEach { rowX ->
        pixelMatrix.add(rowX.toList())
    }
    return pixelMatrix

}

@Synchronized
fun renderExcel(pixels: ArrayList<List<String>>, file: String, listName: String = "list") {
    val workbook = XSSFWorkbook()
    val sheet = workbook.createSheet(listName)

    for (rowNum in 0 until pixels.size) {
        val row: Row = sheet.createRow(rowNum)
        for (colNum in 0 until pixels[0].size) {
            val cell = row.createCell(colNum)

            sheet.setColumnWidth(cell.address.column, (3 * 256).toInt());


            val rgbHex = pixels[cell.address.row][cell.address.column]

            if ( rgbHex != "ffffff") {
                val style = workbook.createCellStyle()
                style.apply {
                    setFillForegroundColor(XSSFColor(toRGBA(rgbHex), null))
                    fillPattern = FillPatternType.SOLID_FOREGROUND;
                }
                cell.cellStyle = style
            }

        }
    }

    val streamThread = Thread {
        FileOutputStream(file)
            .use { outputStream -> workbook.write(outputStream) }
    }

    try {
        streamThread.start()
    } catch (ex: Exception) {
        throw Exception(ex.localizedMessage)
    } finally {
        streamThread.interrupt()
    }
}

fun getPixelColors(file: String, listName: String): ArrayList<List<String>> {
    val table = FileInputStream(file)
    val sheet = WorkbookFactory.create(table).getSheet(listName)

    val rowIterator: Iterator<Row> = sheet.iterator()
    val rowArray: ArrayList<Int> = ArrayList()
    val cellArray: ArrayList<Int> = ArrayList()

    while (rowIterator.hasNext()) {
        val row: Row = rowIterator.next()
        rowArray.add(row.rowNum)
        val cellIterator = row.cellIterator()

        while (cellIterator.hasNext()) {
            val cell = cellIterator.next()
            cellArray.add(cell.address.column)
        }
    }

    val rowSize = rowArray.maxOf{ el -> el }
    val cellSize = cellArray.maxOf{ el -> el }

    val pixArray:Array<Array<String>> = Array(rowSize+1) {Array(cellSize+1) {""} }

    for (i: Row in sheet) {
        for (j: Cell in i) {
            val cellStyle = j.cellStyle
            val color = cellStyle.fillForegroundColorColor
            if (color != null && color is XSSFColor) {
                pixArray[i.rowNum][j.columnIndex] = color.argbHex
            }
        }
    }

    val pixelMatrix = arrayListOf<List<String>>()

    pixArray.forEach { rowX ->
        for (elem in rowX.indices) {
            if (rowX[elem] == "") {
                rowX[elem] = "FFFFFF"
            }
        }
        pixelMatrix.add(rowX.toList())
    }
    return pixelMatrix
}
@Synchronized
fun writeImage(img: BufferedImage, file: String) {
    val imgthread = Thread {
        ImageIO.write(img, File(file).extension, File(file))
    }
    try {
        imgthread.start()
    } catch (ex: Exception) {
        imgthread.interrupt()
        throw FileNotFoundException(ex.message)
    }
}


val toRGBA = { hex: String ->
    val red = hex.toLong(16) and 0xff0000 shr 16
    val green = hex.toLong(16) and 0xff00 shr 8
    val blue = hex.toLong(16) and 0xff
    val alpha = hex.toLong(16) and 0xff000000 shr 24
    Color(red.toInt(),green.toInt(),blue.toInt(),alpha.toInt())
}