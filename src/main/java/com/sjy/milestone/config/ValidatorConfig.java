package com.sjy.milestone.config;

import com.sjy.milestone.account.validator.MemberValidator;
import com.sjy.milestone.account.validator.MemberValidatorImpl;
import com.sjy.milestone.account.validator.PasswordValidator;
import com.sjy.milestone.account.validator.PasswordValidatorImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ValidatorConfig {

    @Bean
    public PasswordValidator passwordValidator() {
        return new PasswordValidatorImpl();
    }

    @Bean
    public MemberValidator memberValidator() {
        return new MemberValidatorImpl();
    }
}