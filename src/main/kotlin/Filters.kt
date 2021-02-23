import java.awt.Color
import java.awt.image.BufferedImage


fun BufferedImage.grayscale(): BufferedImage {
    for (x in 0 until this.width) {
        for (y in 0 until this.height) {
            val color = intToRGBA(this.getRGB(x,y))
            this.setRGB(x,y, Color(
                ((color.red * 0.299f) + (color.green * 0.587) + (color.blue * 0.114)).toInt(),
                ((color.red * 0.299f) + (color.green * 0.587) + (color.blue * 0.114)).toInt(),
                ((color.red * 0.299f) + (color.green * 0.587) + (color.blue * 0.114)).toInt(),
                color.alpha.toInt()
            ).rgb)
        }
    }
    return this
}
@Deprecated("dont use alpha channel")
fun BufferedImage.setHSL(hue: Float=1f, saturation: Float=1f, brightness: Float=1f): BufferedImage {
    for (x in 0 until this.width) {
        for (y in 0 until this.height) {
            val color = intToRGBA(this.getRGB(x,y))

            val hsb = Color.RGBtoHSB(color.red, color.green, color.blue,null)
            val h = hsb[0]
            val s = hsb[1]
            val b = hsb[2]

            val rgbColor = Color.HSBtoRGB(h * hue, s*saturation, b*brightness)

            this.setRGB(x,y,rgbColor)
        }
    }

    return this
}
