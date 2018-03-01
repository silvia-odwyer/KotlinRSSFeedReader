package com.silviaodwyer.rssfeedreader
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class FeedEntry{
    // Placed this class in MainActivity, since it's a small class and just contains info on the XML data.
    var name: String = ""
    var artist: String = ""
    var releaseDate: String = ""
    var imageURL: String = ""
    override fun toString(): String {
        return """
            name = $name
            artist = $artist
            releaseDate = $releaseDate
            imageURL = $imageURL
            """.trimIndent()
    }
}


class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "onCreate called")
        val downloadData = DownloadData()
        downloadData.execute("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=10/xml")
        Log.d(TAG, "onCreate: done")
    }

    companion object {
        private class DownloadData : AsyncTask<String, Void, String>() {
            private val TAG = "DownloadData"

            override fun onPostExecute(result: String?) {
                super.onPostExecute(result)
                Log.d(TAG, "onPostExecute: parameter is $result")
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
