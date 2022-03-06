package com.example.mobile.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobile.R;
import com.example.mobile.controller.UserDAO.UserDAOImpl;
import com.example.mobile.model.User;

public class MainActivity extends AppCompatActivity {
    EditText tf_username, pf_password;
    Button btn_signin, btn_signup;
    UserDAOImpl userDAOImpl;

    public static final String DATABASE_NAME="mobile.db";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //delete database for importing newly added databases in "./assets/databases"
        this.deleteDatabase(DATABASE_NAME);
        userDAOImpl = new UserDAOImpl(this);

        btn_signin = findViewById(R.id.btn_signin);
        btn_signup = findViewById(R.id.btn_signup);
        tf_username = findViewById(R.id.tf_username);
        pf_password = findViewById(R.id.pf_password);

        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = tf_username.getText().toString();
                String password = pf_password.getText().toString();
                System.out.println(username+","+password);

                User u = userDAOImpl.checkCredentials(username, password);
                if(u == null){
                    System.out.println("Cannot find!");
                }
                else{
                    System.out.println("found!");
                }
            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}