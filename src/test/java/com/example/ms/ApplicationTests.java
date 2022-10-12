package com.example.ms;

import com.example.ms.module.system.model.bo.User;
import com.example.ms.module.system.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.AntPathMatcher;

import java.util.List;

@SpringBootTest
class ApplicationTests {

    @Autowired
    UserRepository userRepository;

    @Test
    void contextLoads() {
        List<User> users = userRepository.findAll();
        System.err.println(users.toString());
    }

    public static void main(String[] args) {
        AntPathMatcher matcher = new AntPathMatcher();
        System.err.println(matcher.match("/user/update/*", "/user/update/12"));
        System.err.println(matcher.match("/user/update/", "/user/update/12"));
        System.err.println(matcher.match("/user/update", "/user/update?id=12"));
        System.err.println(matcher.match("/user/update/?", "/user/update/12"));
        System.err.println(matcher.match("/user/update/??", "/user/update/12"));
        System.err.println(matcher.match("/user/update/??", "/user/update/1"));
        System.err.println(matcher.match("/user/update/?", "/user/update/1"));
        System.err.println(matcher.match("/user/update/*", "/user/update/%20"));
    }
}
