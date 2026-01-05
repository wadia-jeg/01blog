package com.zone._blog.exceptions;

public class ErrorResponse {

    private int status;
    private String message;

    public ErrorResponse(String message, int status) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return this.message;
    }

    public void setmessage(String message) {
        this.message = message;
    }

}
