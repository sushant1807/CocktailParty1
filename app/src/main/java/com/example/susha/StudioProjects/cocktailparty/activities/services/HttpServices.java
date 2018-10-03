package com.example.susha.StudioProjects.cocktailparty.activities.services;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.susha.StudioProjects.cocktailparty.activities.activities.LoginActivity1;
import com.example.susha.StudioProjects.cocktailparty.activities.activities.UsersActivity;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class HttpServices extends AsyncTask<String, String, String>
{
    //ProgressDialog pdLoading = new ProgressDialog(LoginActivity.class);

    public static boolean result1=false;
    HttpURLConnection conn;
    URL url = null;
    public interface AsyncResponse {
        void processFinish(String output);
    }

    public AsyncResponse delegate = null;

    public HttpServices(AsyncResponse delegate){
        this.delegate = delegate;
    }


    public static boolean isResult1() {
        return result1;
    }

    public static void setResult1(boolean result1) {
        HttpServices.result1 = result1;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        //this method will be running on UI thread
        //pdLoading.setMessage("\tLoading...");

       // pdLoading.setCancelable(false);
       // pdLoading.show();

    }
    @Override
    protected String doInBackground(String... params) {
        try {

            // Enter URL address where your php file resides
            url = new URL("http://192.168.0.27/android_login_api/login.php/");
            Log.d("bharath","connected");

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "exception";
        }
        try {
            // Setup HttpURLConnection class to send and receive data from php and mysql
            conn = (HttpURLConnection)url.openConnection();

           // conn.setReadTimeout(READ_TIMEOUT);
           // conn.setConnectTimeout(CONNECTION_TIMEOUT);
            conn.setRequestMethod("POST");

            // setDoInput and setDoOutput method depict handling of both send and receive
            conn.setDoInput(true);
            conn.setDoOutput(true);


            // Append parameters to URL
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("username", params[0])
                    .appendQueryParameter("password", params[1]);
            String query = builder.build().getEncodedQuery();

            // Open connection for sending data
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            conn.connect();

        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return "exception";
        }

        try {

            int response_code = conn.getResponseCode();
            Log.d("bharath1",""+response_code);

            // Check if successful connection made
            if (response_code == HttpURLConnection.HTTP_OK) {

                // Read data sent from server
                InputStream input = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    result.append(line);

                }

                // Pass data to onPostExecute method
                return(result.toString());

            }else{

                return("unsuccessful");
            }

        } catch (IOException e) {
            e.printStackTrace();
            return "exception";
        } finally {
            conn.disconnect();
        }


    }

    @Override
    protected void onPostExecute(String result) {

        //this method will be running on UI thread

       // pdLoading.dismiss();

        if(result.equalsIgnoreCase("true"))
        {

            delegate.processFinish(result);
            Log.d("result5",""+this.delegate.toString());


        }else if (result.equalsIgnoreCase("false")){
            delegate.processFinish(result);

            // If username and password does not match display a error message
            //Toast.makeText(HttpServices.this, "Invalid email or password", Toast.LENGTH_LONG).show();

        } else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {

           // Toast.makeText(MainActivity.this, "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();

        }
    }

}
