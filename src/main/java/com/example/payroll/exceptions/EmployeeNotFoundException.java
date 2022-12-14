package com.example.payroll.exceptions;

public class EmployeeNotFoundException extends Exception {

    public EmployeeNotFoundException(String message) {
        super(message);
    }

    public EmployeeNotFoundException(Throwable throwable) {
        super(throwable);
    }

    public EmployeeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
