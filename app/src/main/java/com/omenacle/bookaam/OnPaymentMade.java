package com.omenacle.bookaam;

public interface OnPaymentMade {
    public void onCompleted(String response, String msg);
    public void onFailure(String msg);
}
