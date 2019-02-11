package com.omenacle.bookaam.Account;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.omenacle.bookaam.MainActivity;
import com.omenacle.bookaam.R;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    private TextInputEditText emailEditText, passwordEditText;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        if(!isNetworkAvailable())
        {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Network")
                    .setMessage(getResources().getString(R.string.connection_warning))
                    .setNegativeButton(getResources().getString(R.string.i_decline), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .setPositiveButton(getResources().getString(R.string.proceed), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                            dialogInterface.dismiss();
                        }
                    })
                    .create();
            builder.show();

        }

        Window window = getWindow();
        // finally change the color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }

        //Initializing views
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        TextView forgotTextView = findViewById(R.id.forgot_password);
        TextView createAccountTextView = findViewById(R.id.create_acc_textview);
        Button loginButton = findViewById(R.id.login_button);
        SignInButton googleSignInButton = findViewById(R.id.login_google);
        setGooglePlusButtonText(googleSignInButton, getResources().getString(R.string.sign_in_with_google));

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = emailEditText.getText().toString().trim();

                if(isValidEmail(email)){
                    if(!TextUtils.isEmpty(passwordEditText.getText().toString())){
                        String password = passwordEditText.getText().toString().trim();
                        signInWithEmailandPass(email, password);
                    }
                    else{
                        Toast.makeText(LoginActivity.this,getResources().getString(R.string.password_too_short), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(LoginActivity.this,getResources().getString(R.string.incorrect_email), Toast.LENGTH_SHORT).show();
                }
            }
        });

        forgotTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                View v = getLayoutInflater().inflate(R.layout.forget_pass_dialog, null);
                final TextInputEditText email = v.findViewById(R.id.email);
                Button resetButton = v.findViewById(R.id.btn_reset);

                builder.setView(v);
                final AlertDialog d = builder.create();
                d.show();

                resetButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!TextUtils.isEmpty(email.getText().toString()))
                        {
                            String resetEmail = email.getText().toString().trim();
                            d.dismiss();
                            final ProgressDialog pd = new ProgressDialog(LoginActivity.this);
                            pd.setTitle(getResources().getString(R.string.resetting_password));
                            pd.setMessage(getResources().getString(R.string.please_wait));
                            pd.show();
                            FirebaseAuth.getInstance().sendPasswordResetEmail(resetEmail)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                pd.dismiss();
                                                AlertDialog.Builder bd = new AlertDialog.Builder(LoginActivity.this);
                                                bd.setMessage(getResources().getString(R.string.reset_message))
                                                        .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                                dialogInterface.dismiss();
                                                            }
                                                        }).create();
                                                bd.show();
                                                Log.d("ResetEmail", "Email sent.");
                                            }
                                        }
                                    });
                        }else{
                            Toast.makeText(LoginActivity.this,getResources().getString(R.string.incorrect_email), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        createAccountTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });

        //Google sign in
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignedInResult(task);
        }

    }

    private void handleSignedInResult(Task<GoogleSignInAccount> completedTask) {

        try {
            // Google Sign In was successful, authenticate with Firebase
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            if (account != null) {
                //Log.d("GoogleSignIn", "Account: "+ account.getEmail());
                firebaseAuthWithGoogle(account);
            }
        } catch (ApiException e) {
            // Google Sign In failed, update UI appropriately
            //Log.w("GoogleSignIn", "Google sign in failed: ", e);            //show toast
            Toast.makeText(this, getResources().getText(R.string.log_failed), Toast.LENGTH_SHORT).show();
            // ...
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        //Log.d("GoogleSignIn", "firebaseAuthWithGoogle:" + acct.getId());
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.log_user));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d("GoogleSignIn", "signInWithCredential:success");
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w("GoogleSignIn", "signInWithCredential:failure", task.getException());
                            Snackbar.make(findViewById(R.id.main_layout), getResources().getText(R.string.log_failed), Snackbar.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }

                        // ...
                    }
                });

    }

    private void signInWithEmailandPass(String email, String password) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.log_user));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();

        Log.d("Login Activity:", "showing progress dialog");

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //FirebaseUser user = mAuth.getCurrentUser();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, getResources().getText(R.string.log_failed),
                                    Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }

                        // ...
                    }

                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    protected void setGooglePlusButtonText(SignInButton signInButton, String buttonText) {
        // Find the TextView that is inside of the SignInButton and set its text
        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);

            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setText(buttonText);
                tv.setTextSize(20);
                return;
            }
        }
    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
}
