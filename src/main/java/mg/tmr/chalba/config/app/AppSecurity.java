package mg.tmr.chalba.config.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import mg.tmr.chalba.auth.service.AppLoginHandler;
import mg.tmr.chalba.auth.service.AppLogoutHandler;
import mg.tmr.chalba.config.service.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class AppSecurity extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsServiceImpl userDetailsService;
	
	@Autowired
	private AppLoginHandler appLoginHandler;
	
	@Autowired
	private AppLogoutHandler appLogoutHandler;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.antMatchers("/css/**").permitAll()
	            .antMatchers("/js/**").permitAll()
	            .antMatchers("/webjars/**").permitAll()
				.antMatchers("/auth/**").permitAll()
				.antMatchers("/login/**").permitAll()
	            .anyRequest().authenticated()
	        .and()
	            .formLogin()
		            .loginPage("/login").permitAll()
		            .usernameParameter("userName")
		            .passwordParameter("password")
		            .successHandler(appLoginHandler)
		            .failureUrl("/auth/login")
            .and()
	            .logout()
	            	.permitAll()
	            	.logoutSuccessHandler(appLogoutHandler);	 
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth)
	  throws Exception {
	    auth.authenticationProvider(authenticationProvider());
	}
	 
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
	    DaoAuthenticationProvider authProvider
	      = new DaoAuthenticationProvider();
	    authProvider.setUserDetailsService(userDetailsService);
	    authProvider.setPasswordEncoder(passwordEncoder());
	    return authProvider;
	}
	 
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}	
}
