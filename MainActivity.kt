import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    lateinit var input: EditText
    lateinit var chatBox: TextView
    lateinit var tts: TextToSpeech

    val apiKey = "ISI_API_KEY_KAMU"
    val gemini = GeminiClient()

    val VOICE_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        input = findViewById(R.id.input)
        chatBox = findViewById(R.id.chatBox)

        tts = TextToSpeech(this, this)

        val btnSend = findViewById<Button>(R.id.btnSend)
        val btnMic = findViewById<Button>(R.id.btnMic)

        btnSend.setOnClickListener {

            val msg = input.text.toString()

            chatBox.append("\nYou: $msg")

            gemini.send(apiKey, msg) { reply ->

                runOnUiThread {
                    chatBox.append("\nAI: $reply")
                    tts.speak(reply, TextToSpeech.QUEUE_FLUSH, null, null)
                }
            }
        }

        btnMic.setOnClickListener {
            val v = VoiceManager(this)
            v.startVoice(VOICE_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == VOICE_CODE) {
            val result = data?.getStringArrayListExtra(
                android.speech.RecognizerIntent.EXTRA_RESULTS
            )

            input.setText(result?.get(0) ?: "")
        }
    }

    override fun onInit(status: Int) {
        tts.language = Locale.getDefault()
    }
}
