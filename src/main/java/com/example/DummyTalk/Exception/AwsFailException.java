package com.example.DummyTalk.Exception;

public class AwsFailException extends RuntimeException{
    public AwsFailException(String message) {
        super(message);
    }
}
