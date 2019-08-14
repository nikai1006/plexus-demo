package com.nikai.demo.plexus;


public interface UserService {

    String ROLE = UserService.class.getName();

    String queryUserNanme(int id);

}
