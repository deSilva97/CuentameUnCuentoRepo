package es.unir.cuentameuncuento.helpers;

public class CredentialsHelper {

    public static boolean verifyEmail(String email){
        String regex_email = "[a-zA-Z0-9_+&*-]+";
        String opt_re_email_points = "(?:\\\\.[a-zA-Z0-9_+&*-]+)*";
        String regex_prov = "[a-zA-Z0-9]+";
        String regex_domain = "[a-zA-Z]{2,7}";

        String emRegix= "^" + regex_email + opt_re_email_points + "@" + regex_prov + "." + regex_domain + "$";

        return  !email.isEmpty() && email.matches(emRegix);
    }

    public static boolean verifyPassword(String password){
        return !password.isEmpty() && (password.length() >= 6);
    }
}
