import com.ibm.cloud.sdk.core.http.HttpMediaType
import com.ibm.cloud.sdk.core.security.Authenticator
import com.ibm.cloud.sdk.core.security.IamAuthenticator
import com.ibm.watson.text_to_speech.v1.TextToSpeech
import com.ibm.watson.text_to_speech.v1.model.SynthesizeOptions
import javazoom.jl.decoder.JavaLayerException
import javazoom.jl.player.Player
import org.apache.commons.io.IOUtils
import java.io.ByteArrayInputStream


fun main() {
    val authenticator: Authenticator = IamAuthenticator.Builder()
        .apikey("").build()
    val service = TextToSpeech(authenticator).apply {
        serviceUrl = ""
    }

    //val voices = service.listVoices().execute().result
    //println(voices)

    val synthesizeOptions = SynthesizeOptions.Builder()
        .voice(SynthesizeOptions.Voice.EN_US_HENRYV3VOICE)
        .text("Hello world!")
        .accept(HttpMediaType.AUDIO_MP3)
        .build()

    val serviceCall = service.synthesize(synthesizeOptions)
    val inputStream = serviceCall.execute().result

    val byteArray = IOUtils.toByteArray(inputStream)

    play(byteArray)
}

private fun play(bytes: ByteArray) {
    val bis = ByteArrayInputStream(bytes)
    try {
        val player = Player(bis)
        player.play()
    } catch (e: JavaLayerException) {
        throw IllegalStateException(e)
    }
}