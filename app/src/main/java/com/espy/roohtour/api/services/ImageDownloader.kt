package com.espy.roohtour.api.services

import android.content.Context

import android.app.DownloadManager
import android.net.Uri
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.*
import java.io.*
import java.net.URL
import kotlin.coroutines.coroutineContext


class ImageDownloader {

    fun downloadImage2Async(context: Context, url: String, fileName: String): Deferred<String> {
        return GlobalScope.async {
            val mydir = File(context.getExternalFilesDir(null).toString() + "/RoohtourFiles")
            if (!mydir.exists()) {
                mydir.mkdirs();
            }
            val manager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager?
            val downloadUri: Uri = Uri.parse(url)
            val request = DownloadManager.Request(downloadUri)

            request.setAllowedNetworkTypes(
                DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE
            )
                .setAllowedOverRoaming(true)
                .setTitle("Downloading")
                .setDestinationInExternalFilesDir(context, "/RoohtourFiles", fileName)
            manager?.enqueue(request)
            mydir.absolutePath + File.separator + fileName
        }
    }

    fun downloadImage3Async(context: Context, url: String, fileName: String): Deferred<String> {
        return GlobalScope.async {
            var response: Response? = null
            try {
                var okHttpClient: OkHttpClient = OkHttpClient()
                var count: Int
                val request = Request.Builder()
                    .url(url)
                    .build()

                response = okHttpClient.newCall(request).execute()

                val file = File(context.getExternalFilesDir(null).toString() + "/RoohtourFiles/", fileName)

                response.body?.let {body ->
                    val desDir = File(context.getExternalFilesDir(null).toString() + "/RoohtourFiles/")
                    if (file.exists() && file.length() == body.contentLength()) {
                        desDir.deleteRecursively()
                    }

                    if (!desDir.isDirectory) {
                        desDir.mkdir()
                    }
                    val fileBufferedSink: BufferedSink = when {
                        file.length() != 0L -> file.appendingSink().buffer()
                        else -> {
                            file.sink().buffer()
                        }
                    }

                    val networkBufferedSource = body.source()
                    bufferedRead(
                        networkBufferedSource,
                        fileBufferedSink,
                        DEFAULT_BUFFER_SIZE.toLong(),
                        body.contentLength(),
                        file.length(),
                        url
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                ""
            }finally {
                response?.close()
                ""
            }

            ""
        }
    }

    private suspend fun bufferedRead(
        source: BufferedSource,
        sink: BufferedSink,
        bufferSize: Long,
        totalBytes: Long,
        seek: Long = 0L,
        url: String
    ) {
        var bytesRead = seek
        try {
            // Skip the no of bytes already downloaded
            source.skip(seek)
            var noOfBytes = source.read(sink.buffer, bufferSize)
            while (noOfBytes != -1L && coroutineContext[Job]?.isActive == true) {
                bytesRead += noOfBytes

                noOfBytes = source.read(sink.buffer, bufferSize)
            }
            if (bytesRead != totalBytes) {
                bytesRead = source.read(sink.buffer, totalBytes - bytesRead)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            source.close()
            sink.close()
        }
    }

    fun downloadImageAsync(context: Context, url: String, fileName: String, storageName: String): Deferred<String> {
        return GlobalScope.async {

            try {
                var pathFolder = context.getExternalFilesDir(null).toString() + "/RoohtourFiles/"
                var pathFile = pathFolder + storageName + "_" + fileName
                val destDir = File(pathFolder)
                if (!destDir.exists()) {
                    destDir.mkdirs()
                }


                val url  = URL(url + fileName)
                val connection = url.openConnection()
                connection.connect()
                val lenghtOfFile = connection.contentLength
                val input: InputStream = BufferedInputStream(
                    url.openStream(),
                    8192
                )
                val output: FileOutputStream  = FileOutputStream(
                    pathFile
                )

                val data = ByteArray(1024)


                var count = input.read(data)
                var total = count
                while (count != -1) {
                    output.write(data, 0, count)
                    count = input.read(data)
                    total += count
                }

                output.flush()

                output.close()
                input.close()

                "1"

            } catch (e: Exception) {
                e.printStackTrace()
                "0"
            }
        }
    }
}