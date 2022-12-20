package com.example.customerprofile;

import com.example.customerprofile.data.CustomerProfileEntity;
import com.example.customerprofile.data.CustomerProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.UUID;

@SpringBootApplication
public class Application implements CommandLineRunner {

    @Autowired
    private CustomerProfileRepository repository;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        repository.save(new CustomerProfileEntity().setId(UUID.randomUUID().toString()).setFirstName("Paul").setLastName("Warren").setEmail("warrenpa@vmware.com"));
        repository.save(new CustomerProfileEntity().setId(UUID.randomUUID().toString()).setFirstName("Ming").setLastName("Xiao").setEmail("mixiao@vmware.com"));
    }
}
