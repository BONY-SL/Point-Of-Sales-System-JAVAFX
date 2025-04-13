package org.pos.project.possystem.util;

import lombok.Getter;
import lombok.Setter;
import org.pos.project.possystem.tm.User;

public class SessionManager {

    private SessionManager() {
    }

    @Getter
    @Setter
    private static User currentUser;

    public static void clearSession() {
        currentUser = null;
    }
}
