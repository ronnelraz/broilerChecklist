package com.ronnelrazo.broilerchecklist.func;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.content.FileProvider;

import com.ronnelrazo.broilerchecklist.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AppUpdater {

    private Context context;
    private String updateUrl;
    private DownloadManager downloadManager;
    private long downloadId;
    private BroadcastReceiver downloadReceiver;

    private static final String APK_FILE_NAME = "newupdate.apk";

    public AppUpdater(Context context, String updateUrl) {
        this.context = context;
        this.updateUrl = updateUrl;
    }

    public void checkForUpdate() {
        deleteExistingApk(); // Delete existing APK before checking for update
        new UpdateTask().execute();
    }

    private void deleteExistingApk() {
        File apkFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), APK_FILE_NAME);
        if (apkFile.exists()) {
            boolean deleted = apkFile.delete();
            if (!deleted) {
                Log.e("AppUpdater", "Failed to delete existing APK file");
            }
        }
    }

    private class UpdateTask extends AsyncTask<Void, Integer, Boolean> {

        private AlertDialog progressDialog;
        private int latestVersionCode;
        private String VersionName;
        private String releaseNotes;
        private String apkUrl;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                URL url = new URL(updateUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    String jsonString = stringBuilder.toString();
                    JSONObject jsonObject = new JSONObject(jsonString);

                    latestVersionCode = jsonObject.getInt("versionCode");
                    VersionName = jsonObject.getString("versionName");
                    releaseNotes = jsonObject.getString("releaseNotes");
                    apkUrl = jsonObject.getString("apkUrl");

                    // Checking version codes and initiating download if update is available
                    if (latestVersionCode > getCurrentVersionCode()) {
                        startDownload(apkUrl);
                        return true;
                    }

                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                Log.e("UpdateThread", "Error fetching update", e);
            }
            return false;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            // Update progress dialog with download progress
            if (progressDialog != null) {
                ProgressBar progressBar = progressDialog.findViewById(R.id.progress_bar);
                TextView textProgress = progressDialog.findViewById(R.id.text_progress);

                if (progressBar != null && textProgress != null) {
                    progressBar.setProgress(values[0]);
                    textProgress.setText(values[0] + "%");
                }
            }
        }

        @Override
        protected void onPostExecute(Boolean updateAvailable) {
            super.onPostExecute(updateAvailable);
            dismissProgressDialog();
            // Dismiss progress dialog or update UI indication
            if (updateAvailable) {
                showUpdateDialog();
            } else {
                // Handle case where no update is available
            }
        }

        private void showProgressDialog() {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
            LayoutInflater inflater = LayoutInflater.from(context);
            View dialogView = inflater.inflate(R.layout.dialog_download_progress, null);
            dialogBuilder.setView(dialogView);
            dialogBuilder.setCancelable(false);
            dialogBuilder.setTitle("Updating App");
            dialogBuilder.setMessage(VersionName);
            progressDialog = dialogBuilder.create();
            progressDialog.show();
        }

        private void dismissProgressDialog() {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog = null;
            }
        }

        private void showUpdateDialog() {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
            dialogBuilder.setTitle("New Update Available");
            dialogBuilder.setMessage("Version " + VersionName + "\n(" + releaseNotes + ")");
            dialogBuilder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startDownload(apkUrl);
                }
            });
            dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialogBuilder.setCancelable(false);
            dialogBuilder.show();
        }
    }

    private int getCurrentVersionCode() {
        try {
            // Get current version code of the app
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (Exception e) {
            Log.e("AppUpdater", "Error getting current version code", e);
            return -1;
        }
    }

    private void startDownload(String apkUrl) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(apkUrl));
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, APK_FILE_NAME);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        downloadId = downloadManager.enqueue(request);

        // Register BroadcastReceiver to receive download complete intent
        downloadReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (id == downloadId) {
                    // Download completed, unregister receiver and initiate APK installation
                    context.unregisterReceiver(this);
                    installApk();
                }
            }
        };
        context.registerReceiver(downloadReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    private void installApk() {
        // Create an intent to open the downloaded APK file
        File apkFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), APK_FILE_NAME);
        Uri apkUri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", apkFile);

        // Create an intent to install the APK file
        Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
        intent.setData(apkUri);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(intent);
    }

    public void cleanup() {
        // Unregister the receiver to prevent memory leaks
        if (downloadReceiver != null) {
            context.unregisterReceiver(downloadReceiver);
        }
    }
}
