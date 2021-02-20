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


class FileDataHelper {

    companion object {
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

}