package com.erasmus.appli;

import java.util.List;

import javax.swing.text.html.parser.Entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.erasmus.appli.entity.Users;
import com.erasmus.appli.repository.UserRepository;

@SpringBootApplication
public class ErasmusApplication implements CommandLineRunner {
	
	@Autowired
	UserRepository userRepo;

	public static void main(String[] args) {
		SpringApplication.run(ErasmusApplication.class, args);
	}
	
	@Override
    public void run(String...args) throws Exception {
		List<Users> adminList = userRepo.findAllByRole("admin");
		if(adminList == null || (adminList!= null && adminList.isEmpty())) {
			Users admin = new Users();
			admin.setEmail("admin@gmail.com");
			admin.setFirstName("admin");
			admin.setLastName("admin");
			admin.setPassword("admin");
			admin.setPhone("3456789078");
			admin.setRole("admin");
			admin.setEnabled("Y");
			userRepo.save(admin);
		}
    }

}
