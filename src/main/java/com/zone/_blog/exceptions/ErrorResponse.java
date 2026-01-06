package com.zone._blog.exceptions;

import java.time.LocalDateTime;

public class ErrorResponse {

    private String message;
    private int status;
    private Object detail;
    private String cause;
    private LocalDateTime time = LocalDateTime.now();

    public ErrorResponse(String message, int status, Object detail, Throwable cause) {
        this.status = status;
        this.message = message;
        this.detail = detail;
        this.cause = cause != null ? cause.toString() : "";
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

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getDetail() {
        return this.detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getCause() {
        return this.cause;
    }

    public void setCause(Throwable cause) {
        this.cause = cause != null ? cause.toString() : "";
    }

    public LocalDateTime getTime() {
        return this.time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return String.format("ErrorResponse{\n  message = %s, \n  status = %d, \n  detail = %s, \n  cause = %s\n  time = %s,\n}", this.getMessage(), this.getStatus(), this.getDetail().toString(), this.getCause(), this.getTime().toString());
    }

}
