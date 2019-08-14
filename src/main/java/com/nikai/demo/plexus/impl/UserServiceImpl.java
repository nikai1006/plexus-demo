package com.nikai.demo.plexus.impl;

import com.nikai.demo.plexus.UserService;
import org.codehaus.plexus.component.annotations.Component;

@Component(role = UserService.class, hint = "nikai")
public class UserServiceImpl implements UserService {

    public String queryUserNanme(int id) {
        System.out.println(id);
        return "nikai";
    }
}
