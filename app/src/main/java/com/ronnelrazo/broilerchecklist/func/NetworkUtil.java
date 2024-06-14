package com.ronnelrazo.broilerchecklist.func;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkUtil {

    private Context context;
    private BroadcastReceiver networkReceiver;
    private NetworkStatusListener listener;

    public interface NetworkStatusListener {
        void onNetworkStatusChanged(int status);
        void onNetworkStable();
        void onNetworkUnstable();
    }

    public NetworkUtil(Context context, NetworkStatusListener listener) {
        this.context = context;
        this.listener = listener;
        initNetworkReceiver();
    }

    private void initNetworkReceiver() {
        networkReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

                int status;
                if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
                    if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                        status = 1; // Connected to WiFi
                    } else {
                        status = 2; // Connected to Mobile Data
                    }
                } else {
                    status = 3; // Disconnected
                }

                // Pass the status to the listener
                handleNetworkStatus(status);
            }
        };

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        context.registerReceiver(networkReceiver, filter);
    }

    private void handleNetworkStatus(int status) {
        // Notify the listener about the network status change
        listener.onNetworkStatusChanged(status);
        if (status == 1 || status == 2) {
            // Connected to WiFi or Mobile Data, perform stability test
            new NetworkStabilityTest().execute();
        }
    }

    public void unregisterReceiver() {
        if (networkReceiver != null) {
            context.unregisterReceiver(networkReceiver);
            networkReceiver = null;
        }
    }

    private class NetworkStabilityTest extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            return isNetworkStable();
        }

        @Override
        protected void onPostExecute(Boolean isStable) {
            if (isStable) {
                Func.getInstance(context).log("Network is stable");
                listener.onNetworkStable();
            } else {
                Func.getInstance(context).log("Network is unstable");
                listener.onNetworkUnstable();
            }
        }

        private boolean isNetworkStable() {
            try {
                URL url = new URL("https://www.google.com");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setConnectTimeout(3000); // Timeout in milliseconds
                urlConnection.connect();
                int responseCode = urlConnection.getResponseCode();
                return (responseCode == HttpURLConnection.HTTP_OK);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
    }
}
