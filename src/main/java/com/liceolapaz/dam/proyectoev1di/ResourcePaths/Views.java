package com.liceolapaz.dam.proyectoev1di.ResourcePaths;

public enum Views
{
    LOGIN("login-view.fxml"),
    REGISTER("register-view.fxml"),
    WINDOWBAR("window-bar.fxml"),
    USERMENU("usermenu-view.fxml"),
    ACCOUNT("account-view.fxml"),
    USERSTATS("userstats-view.fxml"),
    COMMUNITY("community-view.fxml"),
    MAINPAGE("mainpage-view.fxml"),
    GLOBALSTATS("globalstats-view.fxml"),
    USERLIBRARY("userlibrary-view.fxml"),
    CREATEUSER("admincreateuser-view.fxml"),
    ADMINUSER("adminuser-view.fxml"),
    ADMINLIBRARY("adminlibrary-view.fxml"),
    ITEMDETAIL("gamedetail-view.fxml"),
    REVIEWCONTAINER("review-view.fxml"),
    MODIFYUSER("adminmodifyuser-view.fxml");


    private final String fxml;

    Views(String fxml)
    {
        this.fxml = fxml;
    }

    public String getFXML()
    {
        return fxml;
    }
}
