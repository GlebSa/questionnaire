import java.io.ByteArrayInputStream
import java.io.File
import java.nio.file.Files

interface Cache {
    fun store(bytes: ByteArray, filename: String)
    fun retrieve(filename: String): ByteArray?
}

class FileCache : Cache {
    private val savePath = "D:/Documents/Eng/Eng_learnsounds/"

    override fun store(bytes: ByteArray, filename: String) {
        ByteArrayInputStream(bytes).use {
            val file = File(savePath + filename + FORMAT)

            if (file.parentFile != null && !file.parentFile.exists()) {
                Files.createDirectories(file.parentFile.toPath())
            }

            Files.copy(it, file.toPath())
        }
    }

    override fun retrieve(filename: String): ByteArray? {
        val file = File(savePath + filename + FORMAT)

        if (!file.exists()) return null

        return Files.readAllBytes(file.toPath())
    }

    companion object {
        private const val FORMAT = ".mp3"
    }
}