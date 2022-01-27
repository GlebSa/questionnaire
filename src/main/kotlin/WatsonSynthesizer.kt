import com.ibm.cloud.sdk.core.http.HttpMediaType
import com.ibm.cloud.sdk.core.security.Authenticator
import com.ibm.cloud.sdk.core.security.IamAuthenticator
import com.ibm.watson.text_to_speech.v1.TextToSpeech
import com.ibm.watson.text_to_speech.v1.model.SynthesizeOptions
import org.apache.commons.io.IOUtils

class WatsonSynthesizer() {
    private val service: TextToSpeech

    init {
        val authenticator: Authenticator = IamAuthenticator.Builder()
            .apikey("").build()
        service = TextToSpeech(authenticator).apply {
            serviceUrl = ""
        }
    }

    fun synthesize(text: String): ByteArray {
        val synthesizeOptions = SynthesizeOptions.Builder()
            .voice(SynthesizeOptions.Voice.EN_US_HENRYV3VOICE)
            .text(text)
            .accept(HttpMediaType.AUDIO_MP3)
            .build()

        val serviceCall = service.synthesize(synthesizeOptions)
        val inputStream = serviceCall.execute().result

        return IOUtils.toByteArray(inputStream)
    }
}