

#Excel Pixel Drawer

![Альтернативный текст](pixel-heart.jpg "Подсказка")

## Описание
Простой скрипт, который позволяет рисовать пиксельную графику, используя excel в качестве полотна. Заливая цветом 
пиксели мы получаем на выходе картинку в любом форматате, в котором сохраним - png. bmp, jpg.

Вместо отсутствущих закрашенных ячеек в экселе подставляется прозрачаня область. Шириная и высота картинки рассчитывается так
* __ширина__ = 
* __высота__ = 

## Функции
### Отрисовка изображений

В файле Drawer.kt находятся функции для отрисовки избражения. Ниже краткое описание каждой

#### drawPixel

Рисует пиксель на изображении **_image_**, с координатами **_x_** и **_y_** и цветом **_red_**, **_green_**, **_blue_** (_красный, зеленый, синий_)

```kotlin
fun drawPixel(x:Int, y:Int, red:Int, green:Int, blue: Int, image: BufferedImage) {
    image.setRGB(x, y, Color(red,green,blue).rgb)
}
```

#### drawTile
Рисует плитку (_большой пиксель_) на изображении **_image_**, в точке с координатами **_startX_** и **_startY_** 
с заданным размером **_size_**. Цвет определяется переменными **_red_**, **_green_**, **_blue_** (_красный, зеленый, синий_)

```kotlin
fun drawTile(startX: Int, startY: Int, size: Int, red: Int, green: Int, blue: Int, image: BufferedImage) {
    for (posX in startX until startX+size) {
        for (posY in startY until startY+size) {
            drawPixel(posX,posY,red,green,blue,image)
        }
    }
}
```

#### drawRandImage

Рисует изображение **_image_**, состоящее из пикселей с рандомным цветом.

```kotlin
fun drawRandImage(image: BufferedImage, stepSize: Int = 1, redRng: Int = 255, greenRng: Int = 255, blueRng: Int = 255) {
    for(posX in 0 until image.width step stepSize){
        for (posY in 0 until image.height step stepSize) {
            val r = if (redRng <= 0) 0 else Random.nextInt(0, redRng)
            val g = if (greenRng <= 0) 0 else Random.nextInt(0, greenRng)
            val b = if (blueRng <= 0) 0 else Random.nextInt(0, blueRng)

            drawPixel(posX, posY, r, g, b, image)
        }
    }
}
```



### Работа с файлами


тут какой то текст



