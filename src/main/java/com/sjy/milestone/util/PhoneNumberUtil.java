package com.sjy.milestone.util;

public class PhoneNumberUtil {

    public static String formatPhoneNumber(String tel) {
        if (tel.length() == 11) {
            return tel.replaceFirst("(\\d{3})(\\d{4})(\\d{4})", "$1-$2-$3");
        }

        else if (tel.length() == 10) {
            return tel.replaceFirst("(\\d{2})(\\d{3,4})(\\d{4})", "$1-$2-$3");
        }

        return tel;
    }
}
