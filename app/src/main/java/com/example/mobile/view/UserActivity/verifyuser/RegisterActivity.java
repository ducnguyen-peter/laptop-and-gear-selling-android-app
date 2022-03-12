package com.example.mobile.view.UserActivity.verifyuser;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobile.R;
import com.example.mobile.utils.InputValidation;
import com.example.mobile.controller.UserDAO.UserDAOImpl;
import com.example.mobile.model.user.User;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText edtUsername;
    private EditText edtEmailOrTel;
    private EditText edtPassword;
    private EditText edtPassRepeat;
    private Button btnSignup;
    private Button btnLogin;

    private InputValidation inputValidation;
    private UserDAOImpl dbHelper;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }

    private void init(){
        edtUsername = findViewById(R.id.edt_username_signup);
        edtEmailOrTel = findViewById(R.id.edt_email_tel);
        edtPassword = findViewById(R.id.edt_password_signup);
        edtPassRepeat = findViewById(R.id.edt_passrepeat_signup);
        btnSignup = findViewById(R.id.btn_submit);
        btnLogin = findViewById(R.id.btn_signin_link);
        btnSignup.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        inputValidation = new InputValidation(this);
        dbHelper = new UserDAOImpl(this);
        user = new User();
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if(viewId==R.id.btn_submit){
            postDataToSqlite();
        } else if(viewId==R.id.btn_signin_link){
            this.finish();
        }
    }

    public void postDataToSqlite(){
        if(!inputValidation.isInputEditTextFilled(edtUsername, "please fill the username")){
            return;
        }
        if(!inputValidation.isInputEditTextFilled(edtEmailOrTel, "please fill email or phone number")){
            return;
        }
        if(!inputValidation.isInputEditTextFilled(edtPassword, "please fill the password")){
            return;
        }
        if(!inputValidation.isInputEditTextFilled(edtPassRepeat, "please fill the password repeat")){
            return;
        }
        if(!inputValidation.isInputEditTextMatch(edtPassword, edtPassRepeat, "password repeat does not match")){
            return;
        }
        if(!checkExisted(edtEmailOrTel, edtUsername)){
            user.setUsername(edtUsername.getText().toString().trim());
            if(inputValidation.isInputEditTextEmail(edtEmailOrTel, "")){
                user.setEmail(edtEmailOrTel.getText().toString().trim());
            }
            else if(inputValidation.isInputTextTel(edtEmailOrTel, "")){
                user.setTel(edtEmailOrTel.getText().toString().trim());
            }
            user.setPassword(edtPassword.getText().toString().trim());
            user.setRole("user");
            dbHelper.addUser(user);
            Toast.makeText(this, "Registered", Toast.LENGTH_SHORT).show();
            emptyInputEditText();
        }
    }

    public boolean checkExisted(EditText txtUsername, EditText txtEmailOrTel){
        if(dbHelper.checkExistedUser(txtEmailOrTel)||dbHelper.checkExistedUser(txtUsername))
            return true;
        return false;
    }

    public void emptyInputEditText(){
        edtUsername.setText(null);
        edtEmailOrTel.setText(null);
        edtPassword.setText(null);
        edtPassRepeat.setText(null);
    }
}