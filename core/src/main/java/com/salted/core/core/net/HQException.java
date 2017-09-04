package com.salted.core.core.net;

public class HQException extends Exception {
    private static final long serialVersionUID = 1L;

    private int status;

    public HQException(String message) {
        super(message);
    }

    public HQException(int status, String message) {
        super(message);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}