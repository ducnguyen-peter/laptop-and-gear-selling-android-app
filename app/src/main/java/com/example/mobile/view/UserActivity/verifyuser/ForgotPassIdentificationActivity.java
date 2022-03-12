package com.example.mobile.view.UserActivity.verifyuser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mobile.R;
import com.example.mobile.controller.InputValidation;
import com.example.mobile.controller.UserDAO.DBHelper;
import com.example.mobile.controller.UserDAO.UserDAOImpl;

public class ForgotPassIdentificationActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText edtIdentity;
    private Button btnNext;

    private InputValidation inputValidation;
    private UserDAOImpl DBHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_identifier);
        setTitle("Recover password");
        init();
    }
    public void init(){
        edtIdentity = findViewById(R.id.edt_email_forgotpass);
        btnNext = findViewById(R.id.btn_next_forgotpass);
        btnNext.setOnClickListener(this);
        inputValidation = new InputValidation(this);
        DBHelper = new UserDAOImpl(this);
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if(viewId == R.id.btn_next_forgotpass){
            if(!inputValidation.isInputEditTextFilled(edtIdentity, "Please fill your username, email or tel")){
                return;
            }
            if(DBHelper.getUser(edtIdentity)!=null){
                Intent intent = new Intent(this, ConfirmNewPassActivity.class);
                intent.putExtra("USER_IDENTIFIER", edtIdentity.getText().toString().trim());
                startActivity(intent);
            }
            else{
                Toast.makeText(this, "invalid email or username", Toast.LENGTH_SHORT).show();
            }
        }
    }

}