package pl.lukasz.service.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.lukasz.service.user.Role;
import pl.lukasz.service.user.RoleRepository;
import pl.lukasz.service.user.User;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Service("adminService")
@Transactional
public class AdminServiceImpl implements AdminService {

    @Autowired
    private JpaContext jpaContext;

    @Qualifier("adminRepository")
    @Autowired
    private AdminRepository adminRepository;


    @Qualifier("roleRepository")
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;




    @Override
    public Page<User> findAll(Pageable pageable) {
        Page<User> userList = adminRepository.findAll(pageable);
        return userList;
    }

    @Override
    public User findUserByID(int id) {
        User user = adminRepository.findUserById(id);
        return user;
    }

    @Override
    public void updateUser(int id, int nrRole, int activity) {
        adminRepository.updateActivationUser(activity, id);
        adminRepository.updateRoleUser(nrRole,id);
    }

    @Override
    public Page<User> findAllSearch(String param, Pageable pageable) {
        Page<User> userList = adminRepository.findAllSearch(param, pageable);
        return userList;
    }

    @Override
    public void insertInBatch(List<User> userList) {
        EntityManager entityManager = jpaContext.getEntityManagerByManagedType(User.class);

        for (int i = 0; i < userList.size(); i++) {
            User u = userList.get(i);
            Role role = roleRepository.findByRole("ROLE_USER");
            u.setRoles(new HashSet<Role>(Arrays.asList(role)));
            u.setPassword(bCryptPasswordEncoder.encode(u.getPassword()));
            entityManager.persist(u);
            if (i % 50 == 0 && i > 0) {
                entityManager.flush();
                entityManager.clear();
                System.out.println("**** Załadowano " + i + " rekordów z " + userList.size() );
            }
        }
    }

    @Override
    public void saveAll(List<User> userList) {
        for (int i = 0; i < userList.size(); i++) {
            Role role = roleRepository.findByRole("ROLE_USER");
            userList.get(i).setRoles(new HashSet<Role>(Arrays.asList(role)));
            userList.get(i).setPassword(bCryptPasswordEncoder.encode(userList.get(i).getPassword()));
        }
        adminRepository.saveAll(userList);
    }
    }



