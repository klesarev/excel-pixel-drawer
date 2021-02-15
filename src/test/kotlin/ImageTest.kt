import org.junit.Assert
import org.junit.Test
import java.awt.Color


class ImageTest {

    @Test
    fun testHexToRgba() {
        Assert.assertEquals("HextoRGBA", Color(204,18,223), toRGBA("FFCC12DF"))
        Assert.assertEquals("HextoRGBA", Color(16,201,38), toRGBA("FF10C926"))
    }

}