package com.androyen.blogreader;

import android.app.ListActivity;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class MainListActivity extends ListActivity {

    protected String[] mBlogPostTitles;

    public static final int NUMBER_OF_POSTS = 20;

    public static final String TAG = MainListActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_list);

        //Get data from the web

        try {
            URL blogFeedURL = new URL("http://blog.teamtreehouse.com/api/get_recent_summary/?count=" + NUMBER_OF_POSTS);
            HttpURLConnection connection = (HttpURLConnection) blogFeedURL.openConnection();
            connection.connect();

            int responseCode = connection.getResponseCode();
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

        //Initialize String array
        Resources resources = getResources();
        mBlogPostTitles = resources.getStringArray(R.array.android_names);




//        String message = getString(R.string.no_items);
//        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
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
}
