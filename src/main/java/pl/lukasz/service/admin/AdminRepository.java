package pl.lukasz.service.admin;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.lukasz.service.user.User;

import java.util.List;

@Repository("adminRepository")
public interface AdminRepository extends JpaRepository<User, Integer> {

    User findUserById(int id);

    @Modifying
    @Query("UPDATE User u SET u.active = :intActive WHERE u.id= :id")
    void updateActivationUser(@Param("intActive") int active, @Param("id") int id);

    @Modifying
    @Query(value = "UPDATE user_role r SET r.role_id = :roleID where r.user_id= :id", nativeQuery = true)
    void updateRoleUser(@Param("roleID") int nrRole, @Param("id") int id);

    @Query(value = "SELECT * FROM user u WHERE u.name LIKE %:param% OR u.last_name LIKE %:param% OR email LIKE %:param%", nativeQuery = true)
    List<User> findAllSearch(@Param("param") String param);

}
