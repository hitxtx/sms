package com.example.ms;

import com.example.ms.model.User;
import com.example.ms.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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

}
