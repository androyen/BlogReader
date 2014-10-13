package com.androyen.blogreader;

import android.app.ListActivity;
import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class MainListActivity extends ListActivity {

    protected String[] mBlogPostTitles;
    protected JSONObject mBlogData;

    public static final int NUMBER_OF_POSTS = 20;

    public static final String TAG = MainListActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_list);

        if (isNetworkAvailable()) {

            //Using AsyncTask to get the blog URL
            GetBlogPostsTask getBlogPostsTask = new GetBlogPostsTask();
            getBlogPostsTask.execute();
        }
        else {
            Toast.makeText(this, "Network is unavailable", Toast.LENGTH_LONG).show();
        }



        //Initialize String array
        Resources resources = getResources();
        mBlogPostTitles = resources.getStringArray(R.array.android_names);




//        String message = getString(R.string.no_items);
//        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private boolean isNetworkAvailable() {

        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        boolean isAvailable = false;

        if (networkInfo != null && networkInfo.isConnected()) {

            isAvailable = true;
        }

        return isAvailable;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateList() {

        if (mBlogData == null) {
            //TODO: Handle error
        }
        else {

            try {
                Log.d(TAG, mBlogData.toString(2));
            }
            catch (JSONException e) {
                Log.d(TAG, "Exception caught!");
            }
        }
    }

    //Separate class for AsyncTask thread to handle network connection
    private class GetBlogPostsTask extends AsyncTask<Object, Void, JSONObject> {


        //Will return a string
        @Override
        protected JSONObject doInBackground(Object[] objects) {

            //-1 is the response code for an error
            int responseCode = -1;

            //Used to return
            JSONObject jsonResponse = null;



            //Get data from the web

            try {

                URL blogFeedURL = new URL("http://blog.teamtreehouse.com/api/get_recent_summary/?count=" + NUMBER_OF_POSTS);
                HttpURLConnection connection = (HttpURLConnection) blogFeedURL.openConnection();
                connection.connect();

                //Get inputstream and store the characters in Array
                if (responseCode == HttpURLConnection.HTTP_OK) {

                    InputStream inputStream = connection.getInputStream();
                    Reader reader = new InputStreamReader(inputStream);
                    int contentLength = connection.getContentLength();
                    char[] charArray = new char[contentLength];

                    reader.read(charArray);


                    //Convert charArray to string
                    String responseData = new String(charArray);

                    //Convert to JSON
                    jsonResponse = new JSONObject(responseData);

                }
                else {
                    Log.i(TAG, "Unsuccessful HTTP Code: " + responseCode);
                }


                Log.i(TAG, "Code: " + responseCode + "");

            }
            catch (MalformedURLException e) {
                Log.e(TAG, "Exception caught", e);
            }
            catch (IOException e) {
                Log.e(TAG, "IOException caught", e);
            }
            catch (Exception e) {
                Log.e(TAG, "Exception caught", e);
            }

            return jsonResponse;
        }


        //Get result of doInBackground() method. Runs in main UI thread
        @Override
        protected void onPostExecute(JSONObject result) {

            mBlogData = result;
            updateList();
        }
    }
}
