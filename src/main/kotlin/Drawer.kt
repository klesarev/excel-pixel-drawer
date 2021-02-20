import org.apache.poi.ss.usermodel.*
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.lang.Exception
import java.util.ArrayList
import javax.imageio.ImageIO
import org.apache.poi.xssf.usermodel.*
import java.io.FileOutputStream
import org.apache.poi.xssf.usermodel.XSSFColor


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

fun drawSVG(pixels: ArrayList<List<String>>): String {
    return "soon..."
}

fun renderToExcel(pixels: ArrayList<List<String>>, file: String, listName: String = "list") {
    val workbook = XSSFWorkbook()
    val sheet = workbook.createSheet(listName)

    for (rowNum in 0 until pixels.size-1) {
        val row: Row = sheet.createRow(rowNum)
        for (colNum in 0 until pixels[0].size-1) {
            val cell = row.createCell(colNum)

            sheet.setColumnWidth(cell.address.column, (3 * 256).toInt());

            val style = workbook.createCellStyle()
            val rgbS = pixels[cell.address.row][cell.address.column]

            if ( rgbS != "FFFFFF") {

                style.apply {
                    setFillForegroundColor(XSSFColor(toRGBA(rgbS), null))
                    fillPattern = FillPatternType.SOLID_FOREGROUND;
                }

                cell.cellStyle = style
            }

            println(
                "ROW: ${cell.address.row} " +
                "COLUMN: ${cell.address.column} " +
                "COLOR: ${pixels[cell.address.row][cell.address.column]}"
            )
        }
    }

    FileOutputStream(file)
        .use { outputStream -> workbook.write(outputStream) }
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


val toRGB = { hex: String ->
    val red = hex.toLong(16) and 0xff0000 shr 16
    val green = hex.toLong(16) and 0xff00 shr 8
    val blue = hex.toLong(16) and 0xff
    Color(red.toInt(),green.toInt(),blue.toInt())
}

val toRGBA = { hex: String ->
    val red = hex.toLong(16) and 0xff0000 shr 16
    val green = hex.toLong(16) and 0xff00 shr 8
    val blue = hex.toLong(16) and 0xff
    val alpha = hex.toLong(16) and 0xff000000 shr 24
    Color(red.toInt(),green.toInt(),blue.toInt(),alpha.toInt())
}