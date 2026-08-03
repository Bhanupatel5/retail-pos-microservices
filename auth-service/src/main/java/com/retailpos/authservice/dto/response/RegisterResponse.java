package com.retailpos.authservice.dto.response;

public class RegisterResponse {

    private String employeeId;
    private String message;

    public RegisterResponse() {
    }

    public RegisterResponse(String employeeId, String message) {
        this.employeeId = employeeId;
        this.message = message;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}