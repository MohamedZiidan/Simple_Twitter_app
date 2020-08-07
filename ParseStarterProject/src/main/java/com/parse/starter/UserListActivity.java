package com.parse.starter;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity {

    static ArrayList<String> userList = new ArrayList<>();
    static ArrayAdapter arrayAdapter;
    ListView listView;
    ParseObject parseObject;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        setTitle("Users");

        listView = findViewById(R.id.userListView);
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_checked, userList);
        listView.setAdapter(arrayAdapter);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckedTextView checkedTextView = (CheckedTextView) view;

                if (checkedTextView.isChecked()) {
                    // IF THE USER IS CHECKED
                    ParseUser.getCurrentUser().add("isFollowing", userList.get(position));

                } else {
                    // IF THE USER IS NOT CHECKED
                    ParseUser.getCurrentUser().getList("isFollowing").remove(userList.get(position));
                    List tempUsers = ParseUser.getCurrentUser().getList("isFollowing");
                    ParseUser.getCurrentUser().remove("isFollowing");
                    ParseUser.getCurrentUser().put("isFollowing", tempUsers);
                }

                ParseUser.getCurrentUser().saveInBackground();
            }
        });

        userList.clear();
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null && objects.size() > 0) {
                    for (ParseUser user : objects) {
                        userList.add(user.getUsername());
                        arrayAdapter.notifyDataSetChanged();
                    }

                    for (String username : userList) {
                        if (ParseUser.getCurrentUser().getList("isFollowing").contains(username)) {
                            listView.setItemChecked(userList.indexOf(username), true);
                            arrayAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            ParseUser.logOut();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);

        }

        if (item.getItemId() == R.id.tweet) {
            final EditText tweet = new EditText(this);
            new AlertDialog.Builder(this).setTitle("Send a Tweet").setView(tweet)
                    .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            parseObject = new ParseObject("Tweet");
                            parseObject.put("tweet", tweet.getText().toString());
                            parseObject.put("username", ParseUser.getCurrentUser().getUsername());
                            parseObject.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        Toast.makeText(UserListActivity.this, "Tweet Sent ;)", Toast.LENGTH_SHORT).show();

                                    } else {
                                        Toast.makeText(UserListActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }).setNegativeButton("Cancel", null).show();

        }

        if (item.getItemId() == R.id.feed) {
            Intent intent = new Intent(getApplicationContext(), TweetsActivity.class);
            startActivity(intent);

        }


        return super.onOptionsItemSelected(item);
    }
}