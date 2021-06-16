package com.example.tp2_grupo04;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    private Integer band  = 0;
    public static final String URI_REGISTER_USER = "http://so-unlam.net.ar/api/api/register";
    public static final String URI_LOGIN_USER = "http://so-unlam.net.ar/api/api/login";
    public static final String URI_TOKEN_REFRESH = "http://so-unlam.net.ar/api/api/refresh";
    public static final String URI_REGISTER_EVENT = "http://so-unlam.net.ar/api/api/event";

    public Integer getBand(){
        return this.band;
    }

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    public static StringBuilder convertInputStreamToString(InputStreamReader inputStreamReader) throws IOException {
        BufferedReader br = new BufferedReader(inputStreamReader);
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ( (line = br.readLine()) != null ){
            stringBuilder.append(line + "\n");
        }
        br.close();
        return stringBuilder;
    }

    public static boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }
}
