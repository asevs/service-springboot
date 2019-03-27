package pl.lukasz.service.admin;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.lukasz.service.user.User;

import java.util.List;

public interface AdminService {

    Page<User> findAll(Pageable pageable);
    User findUserByID(int id);
    void updateUser(int id, int nrRole, int activity);
    List<User> findAllSearch(String param);
}
