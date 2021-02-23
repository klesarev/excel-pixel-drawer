import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.lang.Exception
import javax.imageio.ImageIO
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.file.StandardOpenOption
import java.nio.channels.AsynchronousFileChannel
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

// 1 color - 8 bit
// FF-FF-FF-FF (A-R-G-B) -> Color(182,23,34,255)
val hexToRGBA = { hexColor: String ->
    val red = hexColor.toLong(16) and 0x00ff0000 shr 16
    val green = hexColor.toLong(16) and 0x0000ff00 shr 8
    val blue = hexColor.toLong(16) and 0x000000ff
    val alpha = hexColor.toLong(16) and 0xff000000 shr 24
    Color(red.toInt(),green.toInt(),blue.toInt(),alpha.toInt())
}

// FF-FF-FF (R-G-B) -> "0CB95A"
val intToHEX = { pixelColor: Int ->
    val red = pixelColor and 0xff0000 shr 16
    val green = pixelColor and 0x00ff00 shr 8
    val blue = pixelColor and 0x0000ff
    Integer.toHexString(Color(red,green,blue).rgb)
}

val intToRGBA = { color: Int ->
    val red = color.toLong() and 0x00ff0000 shr 16
    val green = color.toLong() and 0x0000ff00 shr 8
    val blue = color.toLong() and 0x000000ff
    val alpha = color.toLong() and 0xff000000 shr 24
    Color(red.toInt(),green.toInt(),blue.toInt(),alpha.toInt())
}

// (1,127,16,255) (R-G-B-A) -> "ff00c057"
val rgbaToHEX = { red: Int, green: Int, blue: Int, alpha: Int ->
    val a = if (alpha == 0) "00" else Integer.toHexString(alpha)
    val r = if (red == 0) "00" else Integer.toHexString(red)
    val g = if (green == 0) "00" else Integer.toHexString(green)
    val b = if (blue == 0) "00" else Integer.toHexString(blue)
    "$a$r$g$b"
}

fun writeImage(inputImage: BufferedImage, outputFile: String) {
    try {
        ImageIO.write(inputImage, File(outputFile).extension, File(outputFile))
    } catch (ex: Exception) {
        throw FileNotFoundException(ex.message)
    }
}
