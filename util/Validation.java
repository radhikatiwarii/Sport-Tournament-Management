package util;

 
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {
  
    public static boolean isValidName(String name)
    {
        String regex="^[A-Z][a-z]*\\s[A-Z][a-z]*$";
        Pattern patern=Pattern.compile(regex);
        final Matcher matcher=  patern.matcher(name);
        return matcher.matches();
    }

    public static boolean isValidPhoneNo(String number)
    {
        String regex="^[6-9]\\d{9}$";
        Pattern pattern=Pattern.compile(regex);
        Matcher matcher=pattern.matcher(number);
        return matcher.matches();
    }
}
