/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;


public class MainActivity extends AppCompatActivity {

  EditText usernameInput;
  EditText passwordInput;
  Button signUpButton;
  ImageView logo;




  public void signUp(View view) {


      if (usernameInput.getText().toString().equals("") || passwordInput.getText().toString().equals("")) {
        Toast.makeText(this, "Username & Password are required", Toast.LENGTH_SHORT).show();
      } else {
        ParseUser parseUser = new ParseUser();
        parseUser.setUsername(usernameInput.getText().toString());
        parseUser.setPassword(passwordInput.getText().toString());
        parseUser.signUpInBackground(new SignUpCallback() {
          @Override
          public void done(ParseException e) {
            if (e == null) {

              Toast.makeText(MainActivity.this, "Sign UP Successfully ;)", Toast.LENGTH_SHORT).show();
              showUserList();

            } else {

              ParseUser.logInInBackground(usernameInput.getText().toString(), passwordInput.getText().toString(), new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                  if (e == null && user != null) {
                    Toast.makeText(MainActivity.this, "Login Successfully ;)", Toast.LENGTH_SHORT).show();
                    showUserList();
                  } else {
                    Toast.makeText(MainActivity.this, e.getMessage() , Toast.LENGTH_SHORT).show();
                  }

                }
              });
            }
          }
        });
      }

    }




  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    setTitle("Twitter");

    usernameInput = findViewById(R.id.editTextUserName);
    passwordInput = findViewById(R.id.editTextPassword);
    logo = findViewById(R.id.imageView);
    signUpButton = findViewById(R.id.signUpButton);
    if (ParseUser.getCurrentUser() != null) {

      showUserList();

    }




    ParseAnalytics.trackAppOpenedInBackground(getIntent());
  }


  public void showUserList() {
    Intent intent = new Intent(getApplicationContext(), UserListActivity.class);
    startActivity(intent);
  }

}
