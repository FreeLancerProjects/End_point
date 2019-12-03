package com.creativeshare.end_point.interfaces;



public interface Listeners {

    interface LoginListener {
        void checkDataLogin(String email, String password);
    }



    interface BackListener
    {
        void back();
    }







}
