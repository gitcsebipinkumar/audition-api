package com.audition.common.exception;

import lombok.Getter;

@Getter
public class PostNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -5876728854007114881L;

    public PostNotFoundException(String message) {
        super(message);
    }
}
