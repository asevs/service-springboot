package pl.lukasz.service.utilities;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServiceUtils {

    public static boolean checkEmailOrPassword(String pattern, String stringToCheck){
        Pattern pattern1 = Pattern.compile(pattern);
        Matcher matcher1 = pattern1.matcher(stringToCheck);
        return  matcher1.matches();
    }
}
