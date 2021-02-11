

#Excel Pixel Drawer

![Альтернативный текст](pixel-heart.jpg "Excel Pixel Drawer")

## Описание
Простой скрипт, который позволяет рисовать пиксельную графику, используя excel в качестве полотна. Заливая цветом 
пиксели мы получаем на выходе картинку в любом форматате, в котором сохраним - png. bmp, jpg.

Вместо закрашеных ячеек в экселе подставляется прозрачный пиксель. Шириня и высота картинки рассчитывается исходя из 
крайних положений закрашенных ячеек по стобцам и строкам (_оси X,Y_).


## Как использовать?
## Рандомное изображение
Ниже указаны стандартные примеры по использованию. Например, чтобы получить картинку с ранодомными пикселями мы сначала 
создаем **_BufferedImage_** с фиксированным размером, затем вызываем метод **_drawRandImage_**, который отрисовывает нашу картинку.
**_writeImage_** сохраняет наше изображение.

```kotlin
    val img = BufferedImage(800,800, TYPE_INT_RGB) // картинка
    drawRandImage(img,2,20) // рисуем картинку
    writeImage(img,"D:/test-git.bmp") // записываем изображение
```
## Пиксельное изображение из Excel
```kotlin
    // загружаем массив пикселей из файла pix.xlsx и листа с названием editor
    getPixelColors("D:/pix.xlsx","editor")

    // рисуем картинку
    drawRandImage(img,2,20)

    // записываем изображение
    writeImage(img,"D:/test-git.bmp")
```


## Функции
### Отрисовка изображений

В файле Drawer.kt находятся функции для отрисовки избражения. Ниже приведено краткое описание каждой.

#### getPixelColors

Метод принимает на вход файл **_file_** в формате **_.xlsx_** и название листа **_listName_**в  документе, в котором 
происходит рисование. Возвращает двуменый список с информацией о пикселях для дальнейшей отрисовки.

```kotlin
fun getPixelColors(file: String, listName: String): ArrayList<List<String>> {
    //....
}
```

#### drawPixel

Рисует пиксель на изображении **_image_**, с координатами **_x_** и **_y_** и цветом **_red_**, **_green_**, **_blue_** 
(_красный, зеленый, синий_).

```kotlin
fun drawPixel(x:Int, y:Int, red:Int, green:Int, blue: Int, image: BufferedImage) {
    //....
}
```

#### drawTile
Рисует плитку (_большой пиксель_) на изображении **_image_**, в точке с координатами **_startX_** и **_startY_** 
с заданным размером **_size_**. Цвет определяется переменными **_red_**, **_green_**, **_blue_** (_красный, зеленый, синий_).

```kotlin
fun drawTile(startX: Int, startY: Int, size: Int, red: Int, green: Int, blue: Int, image: BufferedImage) {
    //....
}
```

#### drawRandImage

Рисует изображение **_image_**, состоящее из пикселей с рандомным цветом через заданный шаг **_stepSize_** в пикселях. 
Можно указать уровень красного **_redRng_**, зеленого **_greenRng_** и синего **_blueRng_**. Чем меньше число, тем меньше
количество конкретного цвета в итоговом изображении.

```kotlin
fun drawRandImage(
    image: BufferedImage, stepSize: Int = 1, 
    redRng: Int = 255, greenRng: Int = 255, blueRng: Int = 255
) {
    //....
}
```

#### toRGB / toRGBA

Обе функции принимают на вход строку **_hex_** с 16-рязрядным представлением цвета и возвращают джавовский объект Color.
В первом случае без альфа-канала(_toRGB_), а во втором - с альфа каналом.
```kotlin
val toRGB = { hex: String -> }
val toRGBA = { hex: String -> }
```



### Работа 

Helpers.kt 

#### Class FileDataHelper

uhkh

```kotlin
suspend fun writeContentAsync(file: String, data: ByteArray, add: Boolean = false) = 
    supervisorScope {
        //....
    }
```
jyjjkh ljglk g

```kotlin
suspend fun getContentAsync(file: String): InputStream = 
    supervisorScope {
        //....
    }
```

#### matrix2D
kjhkjh

```kotlin
suspend fun matrix2D(file: String, delimiter: String): ArrayList<List<Int>> {
    //....
}
```

тут какой то текст


### Фильтры

Здесь указаный фильтры, которые расширяют стандартный класс BufferedImage - черно-белый, сепия, размытие, glitch и
многие другие. Список постоянно пополняется.
#### desaturate

Метод **_desaturate_** расширяет стандартный java класс BufferedImage и возвращает его же в черно-белом виде.
```kotlin
fun BufferedImage.desaturate(): BufferedImage {
    //....
}

```



