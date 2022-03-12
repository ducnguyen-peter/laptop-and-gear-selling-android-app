package com.example.mobile.view.UserActivity.verifyuser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.mobile.R;
import com.example.mobile.controller.InputValidation;
import com.example.mobile.controller.UserDAO.DBHelper;
import com.example.mobile.controller.UserDAO.UserDAOImpl;

public class ConfirmNewPassActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText edtNewPass;
    private EditText edtRepeatPass;

    private Button btnResetPass;

    private InputValidation inputValidation;
    private UserDAOImpl DBHelper;

    private String userIdentity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_new_pass);
        setTitle("Reset password");
        init();
    }
    private void init(){
        edtNewPass = findViewById(R.id.edt_new_pass);
        edtRepeatPass = findViewById(R.id.edt_new_pass_repeat);
        btnResetPass = findViewById(R.id.btn_reset_password);
        btnResetPass.setOnClickListener(this);
        inputValidation = new InputValidation(this);
        DBHelper = new UserDAOImpl(this);
        userIdentity = getIntent().getExtras().getString("USER_IDENTIFIER");
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if(viewId==R.id.btn_reset_password){
            resetPassword();
        }
    }

    public void resetPassword(){
        if(!inputValidation.isInputEditTextFilled(edtNewPass, "please enter your new password")){
            return;
        }
        if(!inputValidation.isInputEditTextFilled(edtRepeatPass,"please re-enter your new password")){
            return;
        }
        if(!inputValidation.isInputEditTextMatch(edtNewPass, edtRepeatPass, "password repeat doesn't match")){
            return;
        }
        String newPass = edtNewPass.getText().toString().trim();
        String repeatPass = edtRepeatPass.getText().toString().trim();
        String[] columnPassword = new String[]{"password"};
        String[] valueNewPassword = new String[]{newPass};
        DBHelper.updateUser(userIdentity, columnPassword, valueNewPassword);
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        this.finish();
    }
}