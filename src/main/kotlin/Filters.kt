import java.awt.color.ColorSpace
import java.awt.image.BufferedImage
import java.awt.image.ColorConvertOp

fun BufferedImage.desaturate(): BufferedImage {
    val colorConvert = ColorConvertOp (ColorSpace.getInstance(ColorSpace.CS_GRAY), null)
    return colorConvert.filter(this, this)
}
