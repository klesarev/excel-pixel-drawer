import java.awt.Color

val toRGBA = { hex: String ->
    val red = hex.toLong(16) and 0xff0000 shr 16
    val green = hex.toLong(16) and 0xff00 shr 8
    val blue = hex.toLong(16) and 0xff
    val alpha = hex.toLong(16) and 0xff000000 shr 24
    Color(red.toInt(),green.toInt(),blue.toInt(),alpha.toInt())
}