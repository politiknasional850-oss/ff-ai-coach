import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class GeminiClient {

    private val client = OkHttpClient()

    fun send(apiKey: String, msg: String, callback: (String) -> Unit) {

        val url =
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=$apiKey"

        val json = """
        {
          "contents": [{
            "parts": [{
              "text": "Kamu AI coach esports Free Fire. Jawab singkat dan bijak: $msg"
            }]
          }]
        }
        """.trimIndent()

        val body = RequestBody.create(
            MediaType.get("application/json"),
            json
        )

        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                callback("Error koneksi")
            }

            override fun onResponse(call: Call, response: Response) {

                val res = response.body?.string()

                try {
                    val obj = JSONObject(res!!)
                    val reply = obj.getJSONArray("candidates")
                        .getJSONObject(0)
                        .getJSONObject("content")
                        .getJSONArray("parts")
                        .getJSONObject(0)
                        .getString("text")

                    callback(reply)

                } catch (e: Exception) {
                    callback("Error parsing AI")
                }
            }
        })
    }
}
