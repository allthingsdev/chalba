package mg.tmr.chalba.config.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import mg.tmr.chalba.auth.model.User;
import mg.tmr.chalba.auth.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{
	@Autowired
    private UserRepository userRepository;
 
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
    	
    	//===================================================
		System.out.println("===================================");
		System.out.println("\n");
		System.out.println("userRepository.findByUserName(userName)");
		System.out.println("\n");
		
		System.out.println(userName);
	    
		System.out.println("\n");
		System.out.println("\n");
		System.out.println("========================================");
	    //===================================================    
		
    	
    	
    	User user = userRepository.findByUserName(userName);

    	if(user == null){
    		
    		//===================================================
    		System.out.println("===================================");
    		System.out.println("\n");
    		System.out.println("This IS NULL");
    		System.out.println("\n");
    		
    		System.out.println(user);
    	    
    		System.out.println("\n");
    		System.out.println("\n");
    		System.out.println("========================================");
    	    //===================================================    
    		
            throw new UsernameNotFoundException("User not authorized.");
        }
    	
    	
    	//===================================================
    			System.out.println("===================================");
    			System.out.println("\n");
    			System.out.println("This IS NULL");
    			System.out.println("\n");
    			
    			System.out.println("LOOKING FOR ROLES");
    		    
    			System.out.println("\n");
    			System.out.println("\n");
    			System.out.println("========================================");
    		    //===================================================   
    	
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        
        /*
        for (UserRole role : user.getUserRoles()){
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }
        */
        
        
      //===================================================
		System.out.println("===================================");
		System.out.println("\n");
		System.out.println("This IS NULL");
		System.out.println("\n");
		
		System.out.println(grantedAuthorities);
	    
		System.out.println("\n");
		System.out.println("\n");
		System.out.println("========================================");
	    //===================================================    
        

        return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(), grantedAuthorities);
    }
}
