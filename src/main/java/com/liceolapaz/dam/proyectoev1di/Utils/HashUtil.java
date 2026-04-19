package com.liceolapaz.dam.proyectoev1di.Utils;

import org.mindrot.jbcrypt.BCrypt;

public class HashUtil
{
    public static String hashPassword(String password)
    {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean checkPassword(String password, String hashedPassword)
    {
        return BCrypt.checkpw(password, hashedPassword);
    }
}
