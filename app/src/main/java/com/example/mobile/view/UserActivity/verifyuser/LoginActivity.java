package com.example.mobile.view.UserActivity.verifyuser;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mobile.R;
import com.example.mobile.controller.CartDAO.CartDAOImpl;
import com.example.mobile.utils.InputValidation;
import com.example.mobile.controller.UserDAO.UserDAOImpl;
import com.example.mobile.model.user.User;
import com.example.mobile.utils.PreferenceUtils;
import com.example.mobile.view.UserActivity.main.activities.MainActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class LoginActivity extends AppCompatActivity {
    EditText edtUsername, edtPassword;
    Button btnSignIn, btnSignUp, btnForgotPass;
    UserDAOImpl userDAOImpl;
    private CartDAOImpl cartDAOImpl;
    InputValidation inputValidation;

    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;
    private Button googleBtn;

    ActivityResultLauncher<Intent> activityLauncher;

    public static final String DATABASE_NAME="mobile.db";

    //hehe
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        googleBtn = findViewById(R.id.btn_gg_signin);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this,gso);

        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithGoogle();
            }
        });

        btnSignIn = findViewById(R.id.btn_signin);
        btnSignUp = findViewById(R.id.btn_signup_link);
        btnForgotPass = findViewById(R.id.btn_forgot_pass);

        edtUsername = findViewById(R.id.edt_username);
        edtPassword = findViewById(R.id.edt_password);

        userDAOImpl = new UserDAOImpl(this);
        cartDAOImpl = new CartDAOImpl(this);
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

        btnForgotPass.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), ForgotPassIdentificationActivity.class);
                startActivity(intent);
            }
        });

        activityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                        try {
                            task.getResult(ApiException.class);
                            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(LoginActivity.this);
                            String personEmail;
                            User user;
                            if (acct != null) {
                                personEmail = acct.getEmail();
                                user = userDAOImpl.getUser(personEmail);
                                if(user!=null){
                                    Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                                    mainIntent.putExtra("NAME", user.getUsername());
                                    PreferenceUtils.saveUsername(user.getUsername(), LoginActivity.this);
                                    startActivity(mainIntent);
                                    LoginActivity.this.finish();
                                }
                                else{
                                    Toast.makeText(LoginActivity.this, "This email has not been registered", Toast.LENGTH_SHORT).show();
                                    gsc.signOut(); gsc.revokeAccess();
                                    Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                                    startActivity(registerIntent);
                                }
                            }
                        } catch (ApiException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
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

            startActivity(mainIntent);
            this.finish();
        }
        else{
            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
        }
    }

    private void signInWithGoogle(){
        Intent signInIntent = gsc.getSignInIntent();
        activityLauncher.launch(signInIntent);
    }

    public void emptyInputEditText(){
        edtUsername.setText(null);
        edtPassword.setText(null);
    }
}