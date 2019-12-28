package com.example.scannerapp;

public class ScanType {

    private String timeStamp,UserId,ProductId;

    public ScanType(){

    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }

    public ScanType(String t, String u, String p){
        timeStamp = t;
        UserId = u;
        ProductId = p;
    }
}
