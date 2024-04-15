package com.mytutor.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

//import com.mytutor.demo.EmailFiles.EmailSenderService;

@SpringBootApplication
@EnableWebSecurity
public class DemoApplication {
	//@Autowired
	//private EmailSenderService senderService;
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	/*@EventListener(ApplicationReadyEvent.class)
	public void triggerMail() throws MappingException {
		
		senderService.sendSimpleEmail("samntokozo1@gmail.com",
				"OTP to verify user",
				"This is email sent by Mduh\nOTP : " + Math.round(Math.random()* 1000));

	}*/

}
