package es.unir.cuentameuncuento.contexts;

import java.util.List;

public class UserContext {
    private static String userID;
    private static List<String> booksIDs;

    public UserContext() {
        UserContext.userID = null;
        UserContext.booksIDs = null;
    }

    public UserContext(String userID, List<String> booksIDs) {
        UserContext.userID = userID;
        UserContext.booksIDs = booksIDs;
    }

    public static String getUserID() {
        return userID;
    }

    public static List<String> getBooksIDs() {
        return booksIDs;
    }
}
