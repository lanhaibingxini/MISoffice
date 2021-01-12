package com.ujiuye.utils;

public class BaseResult {
    //请求的结果，为true或false
    private boolean success;
    //请求之后输出的提示信息
    private String message;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "BaseResult{" +
                "success=" + success +
                ", message='" + message + '\'' +
                '}';
    }
}
