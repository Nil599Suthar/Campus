package com.example.sutharnil.buggy;

public class Send_request1 {

    private String Id;
    private String UId;
    private String pickpoint;
    private String droppoint;
    private String pessanger;
    private String uname;
    private double userLat , userLong;
    private String Request_Status;
    public Send_request1(String id, String UId, String uname, String pickpoint, String droppoint, String pessanger, double userLat, double userLong, String request_Status) {

        this.UId = UId;
        this.uname=uname;
        this.pickpoint = pickpoint;
        this.droppoint = droppoint;
        this.pessanger = pessanger;
        this.userLat=userLat;
        this.userLong=userLong;
        this.Request_Status=request_Status;
    }

    public Send_request1() {

    }

    public String getRequest_Status() {
        return Request_Status;
    }

    public void setRequest_Status(String request_Status) {
        Request_Status = request_Status;
    }

    public double getUserLong() {
        return userLong;
    }

    public void setUserLong(double userLong) {
        this.userLong = userLong;
    }

    public double getUserLat() {
        return userLat;
    }

    public void setUserLat(double userLat) {
        this.userLat = userLat;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public void setId(String id) {
        Id = id;
    }

    public void setUId(String UId) {
        this.UId = UId;
    }

    public void setPickpoint(String pickpoint) {
        this.pickpoint = pickpoint;
    }

    public void setDroppoint(String droppoint) {
        this.droppoint = droppoint;
    }

    public void setPessanger(String pessanger) {
        this.pessanger = pessanger;
    }

    public String getId() {
        return Id;
    }

    public String getUId() {
        return UId;
    }

    public String getPickpoint() {
        return pickpoint;
    }

    public String getDroppoint() {
        return droppoint;
    }

    public String getPessanger() {
        return pessanger;
    }
}
