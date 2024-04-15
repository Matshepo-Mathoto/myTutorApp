package com.mytutor.demo.SecurityFiles;

import java.sql.SQLException;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.mytutor.demo.DatabaseControllers.DatabaseQueryController;
import com.mytutor.demo.object_files.UserLogin;

@Component
public class CustomUserDetailsService implements UserDetailsService {
	private DatabaseQueryController dbQueryController;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		dbQueryController = new DatabaseQueryController();
		UserLogin user;
		try {
			user = dbQueryController.getUserLogin(username);
			if (user == null) {
				throw new UsernameNotFoundException("user not found");
			} else {
				return new CustomUser(user);
			}

		} catch (SQLException e) {
			throw new UsernameNotFoundException("user not found");

		}

	}

}
