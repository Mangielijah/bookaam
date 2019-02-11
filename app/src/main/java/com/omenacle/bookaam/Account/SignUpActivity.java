package com.omenacle.bookaam.Account;

import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.omenacle.bookaam.DataClasses.User;
import com.omenacle.bookaam.MainActivity;
import com.omenacle.bookaam.R;
import com.omenacle.bookaam.UserViewModel;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private TextInputEditText nameEditText, phoneEditText, emailEditText, passwordEditText;
    TextView loginTextView;
    Button createAccountButton;
    private UserViewModel userViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        Window window = getWindow();
        // finally change the color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }
        nameEditText = findViewById(R.id.username);
        phoneEditText = findViewById(R.id.usernumber);
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        createAccountButton = findViewById(R.id.create_acc_btn);
        loginTextView = findViewById(R.id.goto_login_textview);

        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                finish();
            }
        });

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameEditText.getText().toString();
                String phone = phoneEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String pass = passwordEditText.getText().toString().trim();

                if (!name.isEmpty()){
                    if (!phone.isEmpty()){
                        if (!email.isEmpty() && isValidEmail(email)){
                            if(!pass.isEmpty() && pass.length() > 5){
                                User user = new User(name, phone, email);
                                userViewModel.insert(user);
                                createUserWithEmailAndPassword(email, pass);
                            }else{
                                Toast.makeText(SignUpActivity.this, getResources().getString(R.string.password_too_short), Toast.LENGTH_SHORT).show();
                            }

                        }else{
                            Toast.makeText(SignUpActivity.this, getResources().getString(R.string.incorrect_email), Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(SignUpActivity.this, getResources().getString(R.string.please_fill_in_your_number), Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(SignUpActivity.this, getResources().getString(R.string.please_fill_in_your_name), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
            startActivity(new Intent(SignUpActivity.this, MainActivity.class));
            finish();
        }
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
    }

    private void createUserWithEmailAndPassword(String email, String password){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage(getResources().getString(R.string.reg_user));
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("SignUpActivity", "createUserWithEmail:success");
                            //FirebaseUser user = mAuth.getCurrentUser();
                            startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("SignUpActivity", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUpActivity.this, getResources().getText(R.string.signup_failed),
                                    Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }

                        // ...
                    }
                });
    }
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}
