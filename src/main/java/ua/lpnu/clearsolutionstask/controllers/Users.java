package ua.lpnu.clearsolutionstask.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.lpnu.clearsolutionstask.exception.UserNotFoundException;
import ua.lpnu.clearsolutionstask.exception.UserUpdateException;
import ua.lpnu.clearsolutionstask.exception.UserCreationException;
import ua.lpnu.clearsolutionstask.models.User;
import ua.lpnu.clearsolutionstask.repo.UserService;

import java.io.FileInputStream;;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@AllArgsConstructor
@RestController
public class Users {
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<User> createUserPost(@RequestParam String email, @RequestParam String firstName,
                                 @RequestParam String lastName, @RequestParam String birthDate,
                                 @RequestParam(required = false) String streetName,
                                 @RequestParam(required = false) Integer buildingNumber,
                                 @RequestParam(required = false) Integer flatNumber,
                                 @RequestParam(required = false) String phoneNumber){
        Properties properties = null;
        LocalDate birthDateTemp;
        try {
        birthDateTemp = LocalDate.of(
                Integer.parseInt(birthDate.substring(0,4)),
                Integer.parseInt(birthDate.substring(5,7)),
                Integer.parseInt(birthDate.substring(8,10))
            );
        } catch (IndexOutOfBoundsException e) {
            throw new UserCreationException("User creation process error");
        }
        Integer dateDifference = LocalDate.now()
                .minus(Period.ofYears(birthDateTemp.getYear()))
                .minus(Period.ofMonths(birthDateTemp.getMonthValue()))
                .minus(Period.ofDays(birthDateTemp.getDayOfMonth())).getYear();
        try {
            InputStream input = new FileInputStream("src/main/resources/validation.properties");
            properties = new Properties();
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Pattern pattern = Pattern.compile("[a-z0-9._\\-]+@[a-z0-9]{2,}+\\.[a-z]{2,}");
        Matcher matcher = pattern.matcher(email);
        if(matcher.find()) {
            if(userService.userExists(email).isEmpty()) {
                if (dateDifference > Integer.parseInt(String.valueOf(properties.get("min_age"))) &&  // min age check
                        birthDateTemp.isBefore(LocalDate.now())) {    // less than today date
                    User user = new User(email, firstName, lastName, birthDateTemp, streetName, buildingNumber, flatNumber, phoneNumber);
                    if(userService.createUser(user).isPresent())
                        return ResponseEntity.ok(user);
                }
            }
        }
        throw new UserCreationException("User creation process error");
    }

    @GetMapping("/all_users")
    public ResponseEntity<List<User>> getAllUsers(@RequestParam(required = false) Integer from,
                                 @RequestParam(required = false) Integer to){
        if(from != null && to != null){
            LocalDate localDateTemp = LocalDate.now();
            LocalDate fromDate = localDateTemp
                    .minus(Period.ofYears(to))
                    .minus(Period.ofMonths(localDateTemp.getMonthValue()-1))
                    .minus(Period.ofDays(localDateTemp.getDayOfMonth()-1));
            LocalDate toDate = LocalDate.now().minus(Period.ofYears(from));
            return ResponseEntity.ok(userService.getUsersAgeRange(fromDate,toDate));
        }
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/all_users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable(value = "id") Long id){
        Optional<User> tempUser = userService.getUserById(id);
        if(tempUser.isPresent())
            return ResponseEntity.ok(tempUser.get());
        throw new UserNotFoundException("User not found with this id : " + id);
    }

    // one/some fields
    @PatchMapping("/update/{id}")
    public ResponseEntity<User> updateUserByIdSomeFields(@PathVariable(value = "id") Long id,
                               @RequestParam(required = false) String email,
                               @RequestParam(required = false) String firstName,
                               @RequestParam(required = false) String lastName,
                               @RequestParam(required = false) String birthDate,
                               @RequestParam(required = false) String streetName,
                               @RequestParam(required = false) Integer buildingNumber,
                               @RequestParam(required = false) Integer flatNumber,
                               @RequestParam(required = false) String phoneNumber){
        Optional<User> tempUser = userService.getUserById(id);
        if(tempUser.isPresent()){
            if(email != null)
                tempUser.get().setEmail(email);
            if(firstName != null)
                tempUser.get().setFirstName(firstName);
            if(lastName != null)
                tempUser.get().setLastName(lastName);
            if(birthDate != null)
                tempUser.get().setBirthDate(LocalDate.of(
                        Integer.parseInt(birthDate.substring(0,4)),
                        Integer.parseInt(birthDate.substring(5,7)),
                        Integer.parseInt(birthDate.substring(8,10))
                ));
            if(streetName != null)
                tempUser.get().setStreetName(streetName);
            if(buildingNumber != null)
                tempUser.get().setBuildingNumber(buildingNumber);
            if(flatNumber != null)
                tempUser.get().setFlatNumber(flatNumber);
            if(phoneNumber != null)
                tempUser.get().setPhoneNumber(phoneNumber);
            if(userService.updateUser(tempUser.get()))
                return ResponseEntity.ok(tempUser.get());
            throw new UserUpdateException("User with this email already exists : " + email);
        }
        throw new UserNotFoundException("User not found with this id : " + id);
    }

    // all fields
    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateUserByIdAllFields(@PathVariable(value = "id") Long id,
                                               @RequestParam String email,
                                               @RequestParam String firstName,
                                               @RequestParam String lastName,
                                               @RequestParam String birthDate,
                                               @RequestParam String streetName,
                                               @RequestParam Integer buildingNumber,
                                               @RequestParam Integer flatNumber,
                                               @RequestParam String phoneNumber){
        Optional<User> tempUser = userService.getUserById(id);
        User compareUser;
        try {
            compareUser = new User(id,email, firstName, lastName, LocalDate.of(
                    Integer.parseInt(birthDate.substring(0, 4)),
                    Integer.parseInt(birthDate.substring(5, 7)),
                    Integer.parseInt(birthDate.substring(8, 10))),
                    streetName, buildingNumber, flatNumber, phoneNumber);
        } catch (IndexOutOfBoundsException e){
            throw new UserUpdateException("Incorrect date format : " + birthDate);
        }
        if(tempUser.isPresent()){
                if (tempUser.get().isDifferentByAllFields(compareUser)) {
                    if (userService.updateUser(compareUser))
                        return ResponseEntity.ok(compareUser);
                    throw new UserUpdateException("User with this email already exists : " + email);
                }
                throw new UserUpdateException("All fields must be updated");
        }
        throw new UserNotFoundException("User not found with this id : " + id);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable(value = "id") Long id){
        Optional<User> tempUser = userService.getUserById(id);
        if(tempUser.isPresent()){
            if(userService.deleteUser(tempUser.get().getId()))
                return ResponseEntity.ok(tempUser.get());
        }
        throw new UserNotFoundException("User not found with this id : " + id);
    }
}
