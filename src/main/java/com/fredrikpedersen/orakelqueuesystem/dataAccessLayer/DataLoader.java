package com.fredrikpedersen.orakelqueuesystem.dataAccessLayer;

import com.fredrikpedersen.orakelqueuesystem.dataAccessLayer.models.authentication.ERole;
import com.fredrikpedersen.orakelqueuesystem.dataAccessLayer.models.authentication.Role;
import com.fredrikpedersen.orakelqueuesystem.dataAccessLayer.models.authentication.User;
import com.fredrikpedersen.orakelqueuesystem.dataAccessLayer.models.queue.ESubject;
import com.fredrikpedersen.orakelqueuesystem.dataAccessLayer.models.queue.QueueEntity;
import com.fredrikpedersen.orakelqueuesystem.dataAccessLayer.repositories.QueueEntityRepository;
import com.fredrikpedersen.orakelqueuesystem.dataAccessLayer.repositories.authentication.RoleRepository;
import com.fredrikpedersen.orakelqueuesystem.dataAccessLayer.repositories.authentication.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;

/**
 * @author Fredrik Pedersen
 * @version 1.0
 * @since 20/09/2020 at 21:37
 */

@Slf4j
@Component
public class DataLoader implements CommandLineRunner {

    private QueueEntityRepository entityRepository;
    private RoleRepository roleRepository;
    private UserRepository userRepository;

    public DataLoader(final QueueEntityRepository queueEntityRepository, final UserRepository userRepository, final RoleRepository roleRepository) {
        this.entityRepository = queueEntityRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Seeding data...");
        seedEntities();
        seedRoles();
        seedUsers();
        log.info("Seeding done!");
    }

    private void seedEntities() {
        log.info("Seeding Queue Entities");
        QueueEntity queueEntity1 = new QueueEntity("Fredrik", ESubject.PROGRAMMING, 1, true);
        QueueEntity queueEntity2 = new QueueEntity("Ana-Maria", ESubject.DISCRETE_MATHEMATICS, 2, false);
        QueueEntity queueEntity3 = new QueueEntity("Maria", ESubject.WEB_DEVELOPMENT, 1, false);

        entityRepository.save(queueEntity1);
        entityRepository.save(queueEntity2);
        entityRepository.save(queueEntity3);
        log.info("Done seeding Queue Entities!");
    }

    private void seedUsers() {
        log.info("Seeding Users");
        List<Role> roleList = roleRepository.findAll();

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        User testAdmin = new User("Fredrik", "fredrikmail", encoder.encode("fredrikpw"));
        testAdmin.setRoles(new HashSet<>(roleList));
        userRepository.save(testAdmin);

        HashSet<Role> userRole = new HashSet<>();
        userRole.add(roleList.get(0));
        User testUser = new User("Nikita", "nikitamail", encoder.encode("nikitapw"));
        testUser.setRoles(userRole);
        userRepository.save(testUser);

        log.info("Done seeding Users!");
    }


    private void seedRoles() {
        log.info("Seeding Roles");
        Role user = new Role();
        user.setName(ERole.ROLE_USER);

        Role admin = new Role();
        admin.setName(ERole.ROLE_ADMIN);

        roleRepository.save(user);
        roleRepository.save(admin);
        log.info("Done seeding roles!");
    }
}
