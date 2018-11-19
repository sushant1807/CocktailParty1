/**
 * Author: Ravi Tamada
 * URL: www.androidhive.info
 * twitter: http://twitter.com/ravitamada
 */
package com.example.susha.StudioProjects.cocktailparty.activities.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.susha.StudioProjects.cocktailparty.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import helper.SQLiteHandler;
import helper.SessionManager;

import app.AppConfig;
import app.AppController;
import helper.SQLiteHandler;
import helper.SessionManager;

public class LoginActivity1 extends AppCompatActivity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button btnLogin;
    private Button btnLinkToRegister;
    private EditText inputEmail,dname;
    private EditText inputPassword;
    private static ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;
    private FirebaseAuth auth;
    private static SharedPreferences sharedpreferences;
    private static SharedPreferences.Editor editor;
    private static CheckBox keepLogin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login1);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        sharedpreferences= getSharedPreferences("login", 0);
        editor = sharedpreferences.edit();
        keepLogin=(CheckBox) findViewById(R.id.keepLogin);
        showDialog();
        if(sharedpreferences.getBoolean("islogin",false))
        {
            hideDialog();
            try {
                String email = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
                int index = email.indexOf("@");
                String email1 = (email.substring(0, index));
                Toast.makeText(LoginActivity1.this, "Welcome Back " + email1.toUpperCase(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(LoginActivity1.this, UsersActivity.class);
                startActivity(intent);
                finish();
            }
            catch(NullPointerException e)
            {
                editor.putBoolean("islogin",false);
               /* Intent intent = new Intent(LoginActivity1.this, LoginActivity1.class);
                startActivity(intent);
                finish();*/

            }
            catch(Exception ge)
            {
                Log.e("Login Error",ge.getMessage().toString());
            }

        }
        hideDialog();


        dname = (EditText) findViewById(R.id.dname);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);

        // Progress dialog


        // SQLite database handler
       // db = new SQLiteHandler(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(LoginActivity1.this, UsersActivity.class);
            startActivity(intent);
            //finish();
        }

        // Login button Click Event
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                // Check for empty data in the form
                if (!email.isEmpty() && !password.isEmpty()) {
                    // login user
                    firebaseLogin(email, password);
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "Please enter the credentials!", Toast.LENGTH_LONG)
                            .show();
                }
            }

        });

        // Link to Register Screen
        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    /**
     * function to verify login details in mysql db
     * */

    private void firebaseLogin(String email, String password){
        auth = FirebaseAuth.getInstance();
        String email1=email;
        String password1=password;
        auth.signInWithEmailAndPassword(email1, password1)
                .addOnCompleteListener(LoginActivity1.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                       // progressBar.setVisibility(View.GONE);
                        if (!task.isSuccessful()) {
                            Log.d("bharath","logged in error");

                                Toast.makeText(LoginActivity1.this, "Authentication Failed", Toast.LENGTH_LONG).show();

                        } else {
                            if(keepLogin.isChecked()){
                            Log.d("bharath","logged in");
                            editor.putBoolean("islogin",true);
                            editor.apply();
                            editor.commit();}



                            Intent intent = new Intent(LoginActivity1.this, UsersActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });

    }


    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
