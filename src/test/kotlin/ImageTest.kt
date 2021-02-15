import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import java.awt.Color
import java.awt.image.BufferedImage
import java.awt.image.BufferedImage.TYPE_INT_ARGB
import java.io.FileNotFoundException
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTimeout
import org.junit.jupiter.api.Assertions.assertTimeoutPreemptively
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.assertThrows
import java.awt.image.BufferedImage.TYPE_INT_RGB
import java.io.File
import javax.imageio.ImageIO


class ImageTest {


    @Test
    fun testHexToRgba() {
        Assert.assertEquals("HextoRGBA", Color(204,18,223), toRGBA("FFCC12DF"))
        Assert.assertEquals("HextoRGBA", Color(16,201,38), toRGBA("FF10C926"))
    }

    @Test
    fun testExceptionFile() {
        assertThrows(Exception::class.java) {
           runBlocking {
               FileDataHelper().getContentAsync("D:/bubub.txt")
           }
        }
    }

}