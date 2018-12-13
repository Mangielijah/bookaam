package com.omenacle.bookaam;

import android.app.ProgressDialog;
import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.omenacle.bookaam.OnPaymentMade;

import org.json.JSONObject;

import java.security.KeyStore;

import cz.msebera.android.httpclient.Header;

public class PaymentTask {

    private String url, reason, msg;
    private Context ctx;
    private OnPaymentMade listener;
    ProgressDialog pd;
    MySSLSocketFactory sf;


    public PaymentTask(Context ctx, String url, String reason, OnPaymentMade l){
        this.ctx = ctx;
        this.url = url;
        this.reason = reason;
        this.listener = l;
    }

    public void execute(){

        AsyncHttpClient client = new AsyncHttpClient();
        client.setConnectTimeout(120000);
        client.setResponseTimeout(120000);

        //ByPasses SSL Certificate due to expired Mobile money Certificate
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
            sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(MySSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            client.setSSLSocketFactory(sf);
        }
        catch (Exception e) {
        }

        RequestParams params = new RequestParams();
        client.get(url, params, new JsonHttpResponseHandler(){
            @Override
            public void onStart() {
                super.onStart();
                msg = null;
                pd = new ProgressDialog(ctx);
                pd.setTitle("Processing");
                pd.setMessage("Making "+reason+" Payment\n Go to dialer and dial *126# to confirm payment");
                pd.setCancelable(true);
                pd.setCanceledOnTouchOutside(false);
                pd.show();
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                msg = reason+" Payment Successful";
                listener.onCompleted(response.toString(), msg);
                pd.dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                msg = reason+" Payment Failed";
                listener.onFailure(msg);
                pd.dismiss();
            }

        });
    }
}
