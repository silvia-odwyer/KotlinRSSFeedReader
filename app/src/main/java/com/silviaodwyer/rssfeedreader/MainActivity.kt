package com.silviaodwyer.rssfeedreader
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import kotlinx.android.synthetic.main.activity_main.*
import java.net.URL
import kotlin.properties.Delegates

class FeedEntry{
    // Placed this class in MainActivity, since it's a small class and just contains info on the XML data.
    var name: String = ""
    var artist: String = ""
    var releaseDate: String = ""
    var imageURL: String = ""
    var summary: String = ""
    var title: String = ""
    var published: String = ""
    override fun toString(): String {
        return """
//            name = $name  // A selection of tags found in XML RSS feeds, I may use them in future projects, so I've left them here.
//            artist = $artist
//            releaseDate = $releaseDate
//            summary = $summary
//            imageURL = $imageURL
            title = $title
            published = $published
            """.trimIndent()
    }
}

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private var username: EditText? = null
    private var submitButton: Button? = null
    private val downloadData by lazy {DownloadData(     this, xmlListView)} // downloadData val is initialized to private, since the object is private

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        username = findViewById(R.id.username)
        submitButton = findViewById(R.id.submitButton)

        Log.d(TAG, "onCreate called")

        Log.d(TAG, "onCreate: done")



        username?.setOnClickListener(object : View.OnClickListener {

            override fun onClick(p0: View?) {

                username?.setText("")
            }
        })

        submitButton?.setOnClickListener(object : View.OnClickListener {

            override fun onClick(p0: View?) {
                var username = username?.text.toString()
                var stringURL = "https://github.com/${username}.atom"

                stringURL = when {
                    stringURL == "" -> "https://github.com/silvia-odwyer.atom" // If they didn't type a username, they can look at my GitHub feed instead
                    else -> stringURL
                }
                // Check if no view has focus:
                val view: View = getCurrentFocus()
                if (view != null) {
                    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(view!!.getWindowToken(), 0)
                }
                downloadData.execute(stringURL)


            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        downloadData.cancel(true)
    }

    companion object {
        // Created a companion object out of an inner class to avoid memory leaks
        private class DownloadData (context: Context, listView: ListView): AsyncTask<String, Void, String>() {
            private val TAG = "DownloadData"

            var propContext : Context by Delegates.notNull()
            var propListView : ListView by Delegates.notNull()

            init {
                propContext = context
                propListView = listView

            }

            override fun onPostExecute(result: String) {
                super.onPostExecute(result)
                //Log.d(TAG, "onPostExecute: parameter is $result")
                val parseApplications = ParseApplications()
                parseApplications.parse(result)
                // An adapter needs the context of the app, the textView, and the listView individual items
                //val arrayAdapter = ArrayAdapter<FeedEntry>(propContext, R.layout.list_item, parseApplications.applications)
                //propListView.adapter = arrayAdapter // Links the arrayAdapter instance to the adapter to the propListView's own adapter
                val feedAdapter = FeedAdapter(propContext, R.layout.list_record, parseApplications.applications)
                propListView.adapter = feedAdapter
            }

            override fun doInBackground(vararg url: String?): String {
                Log.d(TAG, "doInBackground: starts with ${url[0]}")
                val rssFeed = downloadXML(url[0])
                if (rssFeed.isEmpty()) {
                    Log.e(TAG, "doInBackground: Error downloading")
                }
                return rssFeed
            }

            private fun downloadXML(urlPath: String?): String {
                // A very Kotlin like way of writing the below 30 lines of code!
                // From thirty lines down to one! x) Amazing stuff
                return URL(urlPath).readText()
            }
//                val xmlResult = StringBuilder()
//
//                try {
//                    val url = URL(urlPath)
//                    val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
//                    val response = connection.responseCode
//                    Log.d(TAG, "downloadXML: The response code was $response")
//
////
////                    val reader = BufferedReader(InputStreamReader(connection.inputStream))
////
////                    val inputBuffer = CharArray(500)
////                    var charsRead = 0
////                    while (charsRead >= 0) {
////                        charsRead = reader.read(inputBuffer)
////                        if (charsRead > 0) {
////                            xmlResult.append(String(inputBuffer, 0, charsRead))
////                        }
////                    }
////                    reader.close()

//                    Alternative One-Liner:
//                    connection.inputStream.buffered().reader().use{ xmlResult.append(it.readText()) }
//
//                    Log.d(TAG, "Received ${xmlResult.length} bytes")
//                    return xmlResult.toString()

//                } catch (e: MalformedURLException) {
//                    Log.e(TAG, "downloadXML: Invalid URL ${e.message}")
//
//
//                } catch(e: SecurityException){
//                    Log.e(TAG, "downloadXML: Security Exception. No internet permissions have been set! :(")
//                }
//                catch (e: IOException) {
//                    Log.e(TAG, "downloadXML: IO Exception reading data: ${e.message}")
////                } catch (e: Exception) {
////                    Log.e(TAG, "Unknown error: ${e.message}")
//                }
//                } catch (e: Exception) {
//                    val errorMessage: String
//                    when (e) {
//                        is MalformedURLException -> "downloadXML: Invalid URL ${e.message}"
//                        is IOException -> "downloadXML: IO Exception reading data ${e.message}"
//                        is SecurityException -> {
//                            e.printStackTrace()
//                            "downloadXML: Security Exception: Needs permission? ${e.message}"
//                        }
//                        else -> "Unknown error: ${e.message}"
//                    }
//                }
//
//                return ""  // If it gets to here, there's been a problem. Return an empty string
//            }
//        }
//    }
        }
    }
}
