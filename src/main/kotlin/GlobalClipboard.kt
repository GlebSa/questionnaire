import androidx.compose.ui.text.toLowerCase
import com.github.kwhat.jnativehook.GlobalScreen
import com.github.kwhat.jnativehook.dispatcher.SwingDispatchService
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener
import javazoom.jl.player.Player
import java.awt.Toolkit
import java.awt.datatransfer.DataFlavor
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileOutputStream

class GlobalClipboard {

    val synthesizer = WatsonSynthesizer()
    val cache: Cache = FileCache()

    fun createListener() {
        val systemClipboard = Toolkit.getDefaultToolkit().systemClipboard

        GlobalScreen.setEventDispatcher(SwingDispatchService())
        GlobalScreen.registerNativeHook()
        GlobalScreen.addNativeKeyListener(object : NativeKeyListener {
            private var isControlPressed = false

            override fun nativeKeyPressed(nativeEvent: NativeKeyEvent) {
                //println(nativeEvent.keyCode)
                if (nativeEvent.keyCode == 29) {
                    isControlPressed = true
                }
            }

            override fun nativeKeyReleased(nativeEvent: NativeKeyEvent) {
                when {
                    nativeEvent.keyCode == 31 && isControlPressed -> {
                        println("control + s pressed")
                        Thread.sleep(500)
                        val message = systemClipboard
                            .getContents(this)
                            .getTransferData(DataFlavor.stringFlavor)
                            .toString()
                            .trim()
                            .lowercase()

                        handleMessage(message)
                    }
                    nativeEvent.keyCode == 29 -> {
                        isControlPressed = false
                    }
                }
            }
        })
    }

    private fun handleMessage(message: String) {
        println("message \"${message}\"")

        repeat(5) {
            try {
                var bytes = cache.retrieve(message)

                if (bytes == null) {
                    bytes = synthesizer.synthesize(message)
                    cache.store(bytes, message)
                    writeText(message)
                }

                playMp3(bytes)
                return
            } catch (e: Exception) {
                println("Error ${e.message}")
            }
        }
    }

    private fun playMp3(bytes: ByteArray) {
        ByteArrayInputStream(bytes).use {
            Player(it).play()
        }
    }

    private fun writeText(message: String) {
        val file = File("D:/Documents/Eng/Eng_learnsounds/words.txt")

        FileOutputStream(file, true).bufferedWriter().use { writer ->
            writer.write(message)
            writer.newLine()
        }
    }
}