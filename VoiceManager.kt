import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent

class VoiceManager(private val activity: Activity) {

    fun startVoice(code: Int) {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "id-ID")
        activity.startActivityForResult(intent, code)
    }
}
