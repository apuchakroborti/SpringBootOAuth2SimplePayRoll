package com.example.payroll.exceptions;

public class PayrollException extends Exception {

    public PayrollException(String message) {
        super(message);
    }

    public PayrollException(Throwable throwable) {
        super(throwable);
    }

    public PayrollException(String message, Throwable cause) {
        super(message, cause);
    }
}
