package com.parse.starter;

import android.support.v4.util.SimpleArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TweetsActivity extends AppCompatActivity {

    
    ListView listView;
    SimpleAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweets);

        setTitle("Tweets");

        listView = findViewById(R.id.tweetsListView);
        final List<Map<String, String>> tweetData = new ArrayList<>();

        arrayAdapter = new SimpleAdapter(this, tweetData, android.R.layout.simple_list_item_2, new String[]{"content", "username"}, new int[]{android.R.id.text1, android.R.id.text2});
        listView.setAdapter(arrayAdapter);

        ParseQuery<ParseObject> query = new ParseQuery<>("Tweet");
        query.whereContainedIn("username", ParseUser.getCurrentUser().getList("isFollowing"));
        query.addDescendingOrder("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    for (ParseObject object : objects) {
                        Map<String, String> tweetInfo = new HashMap<>();
                        tweetInfo.put("content", object.getString("tweet"));
                        tweetInfo.put("username", object.getString("username"));
                        tweetData.add(tweetInfo);
                        arrayAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

    }
}