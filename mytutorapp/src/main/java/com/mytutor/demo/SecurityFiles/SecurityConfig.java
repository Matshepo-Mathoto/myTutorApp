package com.mytutor.demo.SecurityFiles;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;

import com.mytutor.demo.DatabaseControllers.DatabaseUpdateController;

@Configuration
@EnableWebSecurity
@EnableJdbcHttpSession
public class SecurityConfig {
    @Autowired
    public CustomAuthSucessHandler sucessHandler;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService getDetailsService() {
        return new CustomUserDetailsService();
    }

    @Bean
    public DaoAuthenticationProvider getAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(getDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        System.out.println(daoAuthenticationProvider);
        return daoAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(auth -> {
            auth.requestMatchers("/admin/**").hasRole("ADMIN");
            auth.requestMatchers("/tutor/**").hasRole("TUTOR");
            auth.requestMatchers("/ta/**").hasAnyRole("TA", "CONVENOR");
            auth.requestMatchers("/lecturer/**").hasAnyRole("LECTURER", "CONVENOR");
            auth.requestMatchers("/convenor/**").hasRole("CONVENOR");
            auth.requestMatchers("/user/**").hasAnyRole("LECTURER", "CONVENOR", "ADMIN", "TUTOR", "TA");            
            auth.requestMatchers("/**").permitAll();
            auth.anyRequest().authenticated();
        })

                .formLogin(login -> login.loginPage("/login")
                        .loginProcessingUrl("/processLogin")
                        .successHandler(sucessHandler)
                        .permitAll())

                .sessionManagement(management -> management
                        .sessionFixation().none()
                        // Change session ID after login
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS) // or STATELESS if
                        // using JWT
                        .invalidSessionUrl("/login?expired=true") // Redirect URL on session timeout
                        .maximumSessions(1) // Maximum number of allowed sessions per user
                        .expiredUrl("/login?concurrent=true") // Redirect URL on concurrent session
                        .maxSessionsPreventsLogin(false)) // Prevent new login when the maximum sessions are reached
                .logout(logout -> {
                    logout
                            .logoutUrl("/logout")
                            .invalidateHttpSession(true)
                            .deleteCookies("JSESSIONID")
                            .logoutSuccessHandler(logoutSuccessHandler())
                            .addLogoutHandler(logoutHandler());
                });
        return http.build();
    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return (request, response, authentication) -> {
            // Access the logged-out user's username
            //String username = authentication.getName();

            // Print the username (you can log it or display it as needed)
            //System.out.println("Logged out user: " + username);

            // Handle successful logout (e.g., redirect to a login page)
            response.sendRedirect("/login?logout=true");
        };
    }

    @Bean
    public LogoutHandler logoutHandler() {
        return (request, response, authentication) -> {
            // Access the logged-out user's username
            String username = authentication.getName();

            try {
                DatabaseUpdateController dbUpdateController = new DatabaseUpdateController();
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                String datenow = dtf.format(now);
                dbUpdateController.loggingOut(username, datenow);
                //System.out.println(username + " has successfully logged out");
            } catch (Exception e) {
                System.err.println("Error occured on logout");
            }
            //System.out.println("Custom logout handling for user: " + username);
        };
    }
}
