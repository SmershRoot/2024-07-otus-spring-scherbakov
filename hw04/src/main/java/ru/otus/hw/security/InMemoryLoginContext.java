package ru.otus.hw.security;

import org.springframework.stereotype.Component;

@Component
public class InMemoryLoginContext implements LoginContext {

    private String firstName;

    private String lastName;


    @Override
    public void login(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public boolean isAuthenticated() {
        return this.firstName != null && this.lastName != null;
    }

    @Override
    public String getFirstName() {
        return this.firstName;
    }

    @Override
    public String getLastName() {
        return this.lastName;
    }
}
