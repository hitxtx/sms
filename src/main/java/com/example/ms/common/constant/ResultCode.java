package com.example.ms.common.constant;

public enum  ResultCode {

    SUCCESS(true,10000,"Successful operation!"),
    //---System error return code-----
    FAILED(false,10001,"operation failed"),
    UNAUTHENTICATED(false,10002,"You are not logged in"),
    UNAUTHORISE(false,10003,"Insufficient permissions"),
    SERVER_ERROR(false,99999,"Sorry, the system is busy, please try again later!"),

    //---User operation return code 2xxxx----
    MOBILEORPASSWORDERROR(false,20001,"wrong user name or password");
    //---Enterprise operation return code 3xxxx----
    //---Authorization operation return code----
    //---Other operation return code----

    //Whether the operation is successful
    public boolean success;
    //Operation code
    public int code;
    //Prompt information
    public String message;

    ResultCode(boolean success,int code, String message){
        this.success = success;
        this.code = code;
        this.message = message;
    }

    public boolean success() {
        return success;
    }

    public int code() {
        return code;
    }

    public String message() {
        return message;
    }

}
