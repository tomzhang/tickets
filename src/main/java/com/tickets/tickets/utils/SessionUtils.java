package com.tickets.tickets.utils;

import net.dongliu.requests.Requests;
import net.dongliu.requests.Session;

public class SessionUtils {
    private static Session _session = Requests.session();
    private SessionUtils(){
    }

    public static Session session(){
        return _session;
    }

}
