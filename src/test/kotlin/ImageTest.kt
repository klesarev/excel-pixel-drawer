import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.internal.runners.statements.ExpectException
import java.awt.Color
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.rules.ExpectedException
import java.awt.image.BufferedImage
import java.awt.image.BufferedImage.TYPE_INT_RGB
import java.io.File
import java.io.FileNotFoundException
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