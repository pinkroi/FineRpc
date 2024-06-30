package com.sf.fine.rpc.test;

public class UserService {

    String getUserName(Long id) {
        if (id == 1L) {
            return "mark";
        }
        return "james";
    }

}
