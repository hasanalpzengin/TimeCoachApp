package com.hasanalpzengin.lifeorganizator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.prefs.Preferences;

public class MainActivity extends AppCompatActivity {
    ConstraintLayout layout;
    Button loginGoogle, withoutLogin;
    private static final int SIGN_IN_RESULT = 3333;
    GoogleSignInClient googleSignInClient;
    private FirebaseAuth mAuth;
    ProgressBar progressBar;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init sharedPreferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        //set selected language
        String lang = sharedPreferences.getString("lang","en");
        LanguageFragment.changeLang(getApplicationContext(),lang);

        loginGoogle = findViewById(R.id.loginGoogleButton);
        withoutLogin = findViewById(R.id.withoutLogin);
        layout = findViewById(R.id.layout);
        progressBar = findViewById(R.id.progressBar);

        sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);

        showProgressDialog(100);

        final GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("740100946537-mvn5af2676hpqmive2ibl8o8hl2hifrv.apps.googleusercontent.com")
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        mAuth = FirebaseAuth.getInstance();

        loginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, SIGN_IN_RESULT);
            }
        });

        withoutLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferences.edit().putBoolean("isLogined", false).apply();

                Intent configuration = new Intent(getApplicationContext(), ConfigurationActivity.class);
                startActivity(configuration);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == SIGN_IN_RESULT) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("MainActivity", "Google sign in failed", e);
                Snackbar.make(layout, getString(R.string.errorLogin), Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("MainAcitivty", "firebaseAuthWithGoogle:" + acct.getId());

        showProgressDialog(20);

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("MainActivity", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //set shared preferences to define app's mod
                            sharedPreferences.edit().putBoolean("isLogined", true).apply();
                            //call next activity
                            Intent intent = new Intent(getApplicationContext(), ConfigurationActivity.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("MainActivity", "signInWithCredential:failure", task.getException());
                            Snackbar.make(layout, getString(R.string.errorAuthentication), Snackbar.LENGTH_SHORT).show();
                        }

                        showProgressDialog(100);
                    }
                });
    }

    private void showProgressDialog(int progress) {
        if (progress==100){
            progressBar.setVisibility(View.INVISIBLE);
            Log.d("ProgressBar", "INVISIBLE ACTIVE");
        }else{
            progressBar.setVisibility(View.VISIBLE); 
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getUid()!=null) {
            Log.d("NAME: ", firebaseAuth.getUid());
            Intent intent = new Intent(getApplicationContext(), ConfigurationActivity.class);
            startActivity(intent);
        }
    }
}
