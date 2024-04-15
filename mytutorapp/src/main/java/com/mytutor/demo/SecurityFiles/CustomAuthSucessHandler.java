package com.mytutor.demo.SecurityFiles;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.List;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.mytutor.demo.DatabaseControllers.DatabaseUpdateController;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class CustomAuthSucessHandler implements AuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

		Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
		//System.out.println("User Roles: " + authentication.getAuthorities());

		try {
			//List<String> list = new ArrayList<String>(roles);
			String loggedUsername = SecurityContextHolder.getContext().getAuthentication().getName();
			HttpSession session = request.getSession();
			session.setAttribute("myUsername", loggedUsername.toUpperCase());
			
			DatabaseUpdateController dbUpdateController = new DatabaseUpdateController();
			FileWriter myWriter = new FileWriter("login.txt", true);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            String datenow = dtf.format(now);
            dbUpdateController.addActive(loggedUsername, datenow);
            String line = roles + "#" + loggedUsername + "#" + datenow + "\n";
            myWriter.write(line);
            myWriter.close();
		} catch (Exception e) {
			System.err.println(e);
		}
		//response.sendRedirect("/openUser");
		if (roles.contains("ROLE_ADMIN")) {
			response.sendRedirect("/admin/homepage");
		} else if (roles.contains("ROLE_TUTOR")) {
			response.sendRedirect("/tutor/homepage");
		} else if (roles.contains("ROLE_TA")) {
			response.sendRedirect("/ta/homepage");
		} else if (roles.contains("ROLE_LECTURER") || (roles.contains("ROLE_CONVENOR")))  {
			response.sendRedirect("/lecturer/homepage");
		}

	}

} 