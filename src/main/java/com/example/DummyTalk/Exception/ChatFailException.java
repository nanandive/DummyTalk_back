package com.example.DummyTalk.Exception;

import org.springframework.dao.DataAccessException;

public class ChatFailException extends RuntimeException {
    public ChatFailException(String message) {
        super(message);
    }

    public ChatFailException(String message, DataAccessException e){ //
        super(message, e);
    }
}

