package com.example.flab.soft.shoppingmallfashion.auth.authentication.filter;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class CrewAuthenticationToken extends UsernamePasswordAuthenticationToken {
    public CrewAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
    }
}
