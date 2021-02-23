import org.apache.poi.ss.usermodel.*
import java.awt.Color
import java.awt.image.BufferedImage
import java.lang.Exception
import java.util.ArrayList
import javax.imageio.ImageIO
import org.apache.poi.xssf.usermodel.*
import org.apache.poi.xssf.usermodel.XSSFColor
import java.awt.Image
import java.awt.image.WritableRaster
import java.io.*


fun drawPixel(x:Int, y:Int, red:Int, green:Int, blue:Int, alpha:Int, image:BufferedImage) {
    image.setRGB(x, y, Color(red,green,blue,alpha).rgb)
}

fun drawTile(startX: Int, startY:Int, size:Int, red:Int, green:Int, blue:Int, alpha:Int, image:BufferedImage) {
    for (posX in startX until startX + size) {
        for (posY in startY until startY + size) {
            drawPixel(posX, posY, red, green, blue, alpha, image)
        }
    }
}

fun getImagePixels(file: String): ArrayList<List<String>> {

    val image: BufferedImage = ImageIO.read(FileInputStream(file))


    val pixArray: Array<Array<String>> = Array(image.height) {Array(image.width) {""} }

    for (posX in 0 until image.height) {
        for (posY in 0 until image.width) {
            pixArray[posX][posY] = intToHEX(image.getRGB(posY, posX))
        }
    }

    val pixelMatrix = arrayListOf<List<String>>()

    pixArray.forEach { rowX ->
        pixelMatrix.add(rowX.toList())
    }
    return pixelMatrix

}

fun getExcelPixels(file: String, listName: String): ArrayList<List<String>> {
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
                rowX[elem] = "ffffff"
            }
        }
        pixelMatrix.add(rowX.toList())
    }
    return pixelMatrix
}

fun renderImage(
    pixelArray: ArrayList<List<String>>,
    pixelSize: Int = 10, file:
    String = "excel_image.png",
) {

    val pxSize = if(pixelSize > 0) pixelSize else 10

    val resultImage = BufferedImage(
        pixelArray[0].size * pxSize,
        pixelArray.size * pxSize,
        BufferedImage.TYPE_INT_ARGB
    )

    pixelArray.forEachIndexed { posY, row ->
        row.forEachIndexed { posX, col ->
            drawTile(
                (posX) * pxSize,(posY) * pxSize, pxSize,
                hexToRGBA(col).red,
                hexToRGBA(col).green,
                hexToRGBA(col).blue,
                hexToRGBA(col).alpha,
                resultImage
            )
        }
    }

    writeImage(resultImage.setHSL(0.5f,1f,1f), file)
}

fun renderSVG(pixels: ArrayList<List<String>>): String {
    return "soon..."
}

fun renderExcel(pixelArray: ArrayList<List<String>>, listName: String = "list", file: String = "excel_from_image.xlsx") {
    val workbook = XSSFWorkbook()
    val sheet = workbook.createSheet(listName)

    val rowSize = pixelArray.size
    val columnSize = pixelArray[0].size

    for (rowNum in 0 until rowSize) {
        val row: Row = sheet.createRow(rowNum)

        for (columnNum in 0 until columnSize) {
            val cell = row.createCell(columnNum)

            // set column width for pretty pixellook in excel
            sheet.setColumnWidth(cell.address.column, (3 * 256));

            val hexColor = pixelArray[cell.address.row][cell.address.column]

            if ( hexColor != "ffffffff") {
                val style = workbook.createCellStyle()
                style.apply {
                    setFillForegroundColor(XSSFColor(hexToRGBA(hexColor), null))
                    fillPattern = FillPatternType.SOLID_FOREGROUND;
                }
                cell.cellStyle = style
            }

        }
    }

    try {
        FileOutputStream(file).use {
                outputStream -> workbook.write(outputStream)
        }
    } catch (ex: Exception) {
        throw Exception(ex.localizedMessage)
    }

}