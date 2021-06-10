package com.example.tp2_grupo04;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public static final String URI_REGISTER_USER = "http://so-unlam.net.ar/api/api/register";
    public static final String URI_LOGIN_USER = "http://so-unlam.net.ar/api/api/login";

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }
}
