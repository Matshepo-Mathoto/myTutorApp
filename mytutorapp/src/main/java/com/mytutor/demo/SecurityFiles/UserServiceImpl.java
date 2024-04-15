package com.mytutor.demo.SecurityFiles;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.mytutor.demo.DatabaseControllers.DatabaseCreateController;
import com.mytutor.demo.object_files.UserLogin;

@Service
public class UserServiceImpl {
	@Autowired
	private DatabaseCreateController dbCreateController;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	

	public UserLogin saveUser(UserLogin user) throws SQLException{

		String password=passwordEncoder.encode(user.getPassword());
		user.setPassword(password);
		
		dbCreateController.addLoginDetails(user);
		
		return user;
	}

	
	/*public void removeSessionMessage() {

		HttpSession session = ((ServletRequestAttributes) (RequestContextHolder.getRequestAttributes())).getRequest()
				.getSession();

		session.removeAttribute("msg");
	}*/

}
