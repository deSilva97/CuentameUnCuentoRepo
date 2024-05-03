package es.unir.cuentameuncuento.helpers;

public class CredentialsHelper {

    public static boolean verifyEmail(String email){

        return  !email.isEmpty() && email.matches(getEmailRegex());
    }

    public static boolean verifyPassword(String password){
        return !password.isEmpty() && (password.length() >= 6);
    }

    public static String getEmailRegex(){
        String regex_email = "[a-zA-Z0-9_+&*-]+";
        String opt_re_email_points = "(?:\\\\.[a-zA-Z0-9_+&*-]+)*";
        String regex_prov = "[a-zA-Z0-9]+";
        String regex_domain = "[a-zA-Z]{2,7}";

        return  "^" + regex_email + opt_re_email_points + "@" + regex_prov + "." + regex_domain + "$";
    }
}
