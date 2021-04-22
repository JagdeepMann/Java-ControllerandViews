package com.suraj.loginregistration.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.suraj.loginregistration.models.Client;
@Component
public class ClientValidator implements Validator {
    
    // 1
    @Override
    public boolean supports(Class<?> clazz) {
        return Client.class.equals(clazz);
    }
    
    // 2
    @Override
    public void validate(Object target, Errors errors) {
        Client client = (Client) target;
        
        if (!client.getPasswordConfirmation().equals(client.getPassword())) {
            // 3
            errors.rejectValue("passwordConfirmation", "Match");
        }         
    }
}

