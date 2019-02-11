package com.omenacle.bookaam;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ContactActivity extends AppCompatActivity {
    private EditText messageEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        messageEditText = findViewById(R.id.messageField);
    }

    public void sendMessage(View view) {
        if (!TextUtils.isEmpty(messageEditText.getText()))
        {
            String msg = messageEditText.getText().toString();
            sendSmsMessage("672084140", msg);
        }
        else
        {
            Snackbar.make(view, getResources().getText(R.string.empty_message), Snackbar.LENGTH_SHORT).show();
        }
    }
    private void sendSmsMessage(String number, String msg){
        String smsNumber = "smsto:"+number;
        Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
        smsIntent.setData(Uri.parse(smsNumber));
        smsIntent.putExtra("sms_body", msg);
        // If package resolves (target app installed), send intent.
        if (smsIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(smsIntent);
        } else {
            Log.e("TAG", "Can't resolve app for ACTION_SENDTO Intent.");
        }
    }
}
