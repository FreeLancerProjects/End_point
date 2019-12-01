package com.creativeshare.end_point.interfaces;



public interface Listeners {

    interface LoginListener {
        void checkDataLogin(String user_name, String password);
    }



    interface BackListener
    {
        void back();
    }







}
