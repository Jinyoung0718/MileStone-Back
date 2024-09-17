package com.sjy.milestone.account.validator;

import java.util.regex.Pattern;

public class PasswordValidatorImpl implements PasswordValidator {

    // Pattern은 정규 표현식을 사용할 수 있게 해주며 검사한다.
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
                    "^(?=.*[0-9])" + // 숫자 포함
                    "(?=.*[a-z])" +  // 소문자 포함
                    "(?=\\S+$).{8,15}$" // 공백 미포함 및 8글자 이상 15글자 이하
    );

    @Override
    public boolean isValid(String password) {
        if (password == null) {
            return false;
        }
        return PASSWORD_PATTERN.matcher(password).matches();
    }
}
