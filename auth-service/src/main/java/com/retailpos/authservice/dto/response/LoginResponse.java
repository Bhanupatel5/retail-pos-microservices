package com.retailpos.authservice.dto.response;

public class LoginResponse {

    private String employeeId;
    private String name;
    private String role;
    private String token;

    public LoginResponse() {
    }

    public LoginResponse(String employeeId, String name, String role, String token) {
        this.employeeId = employeeId;
        this.name = name;
        this.role = role;
        this.token = token;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}