package pl.lukasz.service.admin;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.lukasz.service.user.User;

public interface AdminService {

    Page<User> findAll(Pageable pageable);
}
