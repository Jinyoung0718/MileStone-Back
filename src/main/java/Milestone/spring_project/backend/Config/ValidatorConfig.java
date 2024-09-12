package Milestone.spring_project.backend.Config;

import Milestone.spring_project.backend.Util.Validator.Member.MemberValidator;
import Milestone.spring_project.backend.Util.Validator.Member.MemberValidatorImpl;
import Milestone.spring_project.backend.Util.Validator.Passwaord.PasswordValidator;
import Milestone.spring_project.backend.Util.Validator.Passwaord.PasswordValidatorImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.validation.Validator;

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