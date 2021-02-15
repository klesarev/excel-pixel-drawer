import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.supervisorScope
import java.io.*
import java.lang.Exception
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import java.util.*


suspend fun matrix2D(file: String, delimiter: String): ArrayList<List<Int>> {
    val list = arrayListOf<List<Int>>()

    FileDataHelper().getContentAsync(file).use { matrix ->
        matrix.bufferedReader().lines().forEach { row ->
            list.add(
                row.split(delimiter)
                    .map { it }
                    .filter { it != "" }
                    .map { it.toInt() }
            )
        }
    }
    return list
}

class FileDataHelper {

    @Deprecated("use getContentAsync method")
    suspend fun getContent(source: String): String {
        lateinit var reader: BufferedReader

        return suspendCoroutine { continuation ->
            Thread(Runnable{
                try {
                    reader = Files.newBufferedReader(Paths.get(source))
                    continuation.resume(reader.use { it.readText() })
                } catch (error: IOException) {
                    error.printStackTrace()
                    continuation.resumeWithException(error)
                }
            }).start()
        }
    }

    fun writeContent(source: String, content: String) {
        try {
            Files.write(
                Paths.get(source), content.toByteArray(),
                StandardOpenOption.APPEND,
                StandardOpenOption.CREATE
            )
        } catch (error: IOException) {
            error.printStackTrace()
        }
    }

    suspend fun writeContentAsync(file: String, data: ByteArray, add: Boolean = false) =
        supervisorScope {
            val dataStr = async(Dispatchers.IO) {
                FileOutputStream(file, add).write(data)
            }
            try {
                dataStr.await()
            } catch (ex: Exception) {
                throw Exception("@ ${ex.message}")
            }
        }


    suspend fun getContentAsync(file: String): InputStream =
        supervisorScope {
            val dataStr = async(Dispatchers.IO) {
                FileInputStream(file)
            }
            try {
                dataStr.await()
            } catch (ex: Exception) {
                throw Exception("@ ${ex.message}")
            }
        }

}