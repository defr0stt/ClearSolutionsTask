package ua.lpnu.clearsolutionstask.repo;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ua.lpnu.clearsolutionstask.models.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private UserRepository userRepository;

    public Optional<User> createUser(User user){
        if(userExists(user.getEmail()).isEmpty()) {
            userRepository.save(user);
            return Optional.of(user);
        }
        return Optional.empty();
    }

    public Optional<User> userExists(String email){
        return userRepository.findByEmail(email);
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public List<User> getUsersAgeRange(LocalDate from, LocalDate to){
        return userRepository.findByBirthDate(from,to);
    }

    public Optional<User> getUserById(Long id){
        return userRepository.findById(id);
    }

    public boolean deleteUser(Long id){
        Optional<User> tempUser = userRepository.findById(id);
        if(tempUser.isPresent()){
            userRepository.deleteUserById(id);
            return true;
        }
        return false;
    }

    public boolean updateUser(User user){
        Optional<User> tempUser = userRepository.findByEmail(user.getEmail());
        if(tempUser.isPresent()){
            if(tempUser.get().getId().equals(user.getId())){
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }
}