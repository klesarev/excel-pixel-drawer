import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.io.FileNotFoundException
import java.lang.Exception
import javax.imageio.ImageIO
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.file.StandardOpenOption
import java.nio.channels.AsynchronousFileChannel
import java.nio.file.Paths


val toRGBA = { hex: String ->
    val red = hex.toLong(16) and 0xff0000 shr 16
    val green = hex.toLong(16) and 0xff00 shr 8
    val blue = hex.toLong(16) and 0xff
    val alpha = hex.toLong(16) and 0xff000000 shr 24
    Color(red.toInt(),green.toInt(),blue.toInt(),alpha.toInt())
}

val toHEX = { pixColor: Int ->
    val red = pixColor shr 16 and 255
    val green = pixColor shr 8 and 255
    val blue = pixColor and 255
    Integer.toHexString(Color(red,green,blue).rgb)
}

@Synchronized fun writeImage(inputImage: BufferedImage, outputFile: String) {
    val imagethread = Thread {
        ImageIO.write(inputImage, File(outputFile).extension, File(outputFile))
    }
    try {
        imagethread.start()
    } catch (ex: Exception) {
        imagethread.interrupt()
        throw FileNotFoundException(ex.message)
    }
}

fun writeFileAsync(file: String, data: ByteArray) {
    try {
        AsynchronousFileChannel.open(
            Paths.get(file),
            StandardOpenOption.CREATE
        ).use { asyncFile ->
            asyncFile.write(ByteBuffer.wrap(data), 0)
        }
    } catch (e: IOException) {
        throw Exception(e.localizedMessage)
    }
}