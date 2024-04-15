package com.mytutor.demo.HelperClasses;

import java.util.Enumeration;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.servlet.http.HttpSession;

/**
 * Manages user HttpSession like the saved attributes or items
 */
public class SpringSessionController {

	   /**
     * Gets the username of the user that is logged in to this session (Web-browser
     * and IP-address)
     * 
     * @return the username
     */
    public static String getLoggedUser() {
        String loggedUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        return loggedUsername;
    }

	/**
     * Retrives the role of this user for this session from the Http Security
     * Context authentication
     * 
     * @return the user_role on the system if authenticated
     */
    public static String getRole() {
        // Get the currently authenticated user's principal
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String role = "";
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            for (GrantedAuthority authority : userDetails.getAuthorities()) {
                role = authority.getAuthority();
            }

            // Return roles as a response
            return role;
        } else {
            // User is not authenticated
            return "User is not authenticated";
        }
    }



	/**
	 * Sets the string attribute to the http session for the user
	 * @param httpSession: user session to set or add the attribute to
	 * @param name: name of the setting attribute
	 * @param value: value for the attribute to be saved
	 */
	public static void setStringAttribute(HttpSession httpSession, String name, String value) {
		httpSession.setAttribute(name, value);
	}

	/**
	 * Retrieves a string attribute from the user http session
	 * @param httpSession: user session to search or get the string attribute from
	 * @param name: name of the saved attributes
	 * @return the string attribute if found and serializable to string else return null
	 */
	public static String getStringAttribute(HttpSession httpSession, String name) {
		if (httpSession.getAttribute(name) == null) {
			return null;
		}
		String attribute = "";
		try {
			attribute =  (String) httpSession.getAttribute(name);
		} catch (Exception e) {
			attribute = null;
		}
		return attribute;
	}

	/**
	 * Removes an attribute from the user httpSession
	 * @param httpSession: user session to remove attribute from
	 * @param name
	 */
	public static void removeAttribute(HttpSession httpSession, String name) {
		httpSession.removeAttribute(name);
	}

	/**
	 * Clears the user session attributes and leaves only their username and saved security context
	 * @param httpSession: user HttpSession with attributes or user data
	 */
	public static void clearSessionAttributes(HttpSession httpSession) {
		String loggedUsername = SecurityContextHolder.getContext().getAuthentication().getName();
		// Get an enumeration of attribute names
		Enumeration<String> attributeNames = httpSession.getAttributeNames();

		// Iterate through the attribute names and access the attributes
		while (attributeNames.hasMoreElements()) {
			String attributeName = attributeNames.nextElement();
			if (attributeName.toUpperCase().indexOf("SECURITY") <= 0) {
				httpSession.removeAttribute(attributeName);
			}
			httpSession.setAttribute("myUsername", loggedUsername);

		}
	}
}
