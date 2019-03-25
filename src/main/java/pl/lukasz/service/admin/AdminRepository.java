package pl.lukasz.service.admin;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.lukasz.service.user.User;

@Repository("adminRepository")
public interface AdminRepository extends JpaRepository<User, Integer> {

    User findUserById(int id);


}
