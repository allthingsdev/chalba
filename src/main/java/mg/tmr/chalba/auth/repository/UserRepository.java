package mg.tmr.chalba.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mg.tmr.chalba.auth.model.User;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Long>  {

	User findByEmail(String email);
	User findByConfirmationToken(String confirmationToken);
	User findByUserName(String userName);
}
