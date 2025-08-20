package com.supersection.trading.exception;

public class PhoneNumberFormatException extends RuntimeException {
    public PhoneNumberFormatException(String message) {
        super(message);
    }

    public PhoneNumberFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}
