package com.ronnelrazo.broilerchecklist.func;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import java.io.File;

public class DownloadHelper {

    public static void deleteExistingFile(String fileName) {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
        if (file.exists()) {
            file.delete();
        }
    }
    public static void downloadFile(Context context, String url,String filename) {
        deleteExistingFile(filename+".pdf");
        // Create a DownloadManager instance
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

        // Create a DownloadManager.Request with the file URL
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

        // Set the destination directory and filename
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename+".pdf");

        // Optionally, set other request parameters such as title, description, etc.
        request.setTitle("Download File");
        request.setDescription("Downloading a file from the URL");

        // Enqueue the download and return the download ID
        long downloadId = downloadManager.enqueue(request);
    }
}
