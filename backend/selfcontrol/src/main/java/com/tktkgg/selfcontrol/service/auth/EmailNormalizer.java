package com.tktkgg.selfcontrol.service.auth;

import java.util.Locale;

import org.springframework.stereotype.Component;

@Component 
public class EmailNormalizer {
    public String normalize(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
