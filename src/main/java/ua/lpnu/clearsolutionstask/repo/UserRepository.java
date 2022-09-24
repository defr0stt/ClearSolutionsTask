package ua.lpnu.clearsolutionstask.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ua.lpnu.clearsolutionstask.models.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long id);
    List<User> findAll();

    @Transactional
    void deleteUserById(Long id);

    @Query(value = "SELECT u FROM User u WHERE u.birthDate BETWEEN ?1 AND ?2")
    List<User> findByBirthDate(LocalDate from, LocalDate to);
}
