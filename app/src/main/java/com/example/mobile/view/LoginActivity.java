package com.example.mobile.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobile.R;
import com.example.mobile.controller.InputValidation;
import com.example.mobile.controller.UserDAO.UserDAOImpl;
import com.example.mobile.model.User;
import com.example.mobile.utils.PreferenceUtils;

public class LoginActivity extends AppCompatActivity {
    EditText edtUsername, edtPassword;
    Button btnSignIn, btnSignUp;
    UserDAOImpl userDAOImpl;
    InputValidation inputValidation;
    public static final String DATABASE_NAME="mobile.db";

    //hehe
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //delete database for importing newly added databases in "./assets/databases"
//        this.deleteDatabase(DATABASE_NAME);

        btnSignIn = findViewById(R.id.btn_signin);
        btnSignUp = findViewById(R.id.btn_signup_link);
        edtUsername = findViewById(R.id.edt_username);
        edtPassword = findViewById(R.id.edt_password);

        userDAOImpl = new UserDAOImpl(this);
        inputValidation = new InputValidation(this);

        if(PreferenceUtils.getUsername(this)!=null){
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            this.finish();
        }

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyFromSqlite();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    public void verifyFromSqlite(){
        if(!inputValidation.isInputEditTextFilled(edtUsername, "please fill the username")){
            return;
        }
        if(!inputValidation.isInputEditTextFilled(edtPassword, "please fill the password")){
            return;
        }
        login();
    }

    public void login(){
        if(userDAOImpl.checkLogin(edtUsername, edtPassword)){
            User user = userDAOImpl.getUser(edtUsername);
            System.out.println("User name: "+user.getUsername());
            Intent mainIntent = new Intent(this, MainActivity.class);
            mainIntent.putExtra("NAME", user.getUsername());
            emptyInputEditText();
            PreferenceUtils.saveUsername(user.getUsername(), this);
            PreferenceUtils.savePassword(user.getPassword(), this);

            startActivity(mainIntent);
            this.finish();
        }
        else{
            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
        }
    }

    public void emptyInputEditText(){
        edtUsername.setText(null);
        edtPassword.setText(null);
    }
}