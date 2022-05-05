package com.example.IspringTest.dto;

public class Result {

    /**
     * 状态码
     */
    private String status;
    /**
     * 具体信息
     */
    private String message;

    @Override
    public String toString() {
        return "Result{" +
                "status=" + status +
                ", message='" + message + '\'' +
                '}';
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

