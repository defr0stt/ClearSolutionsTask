package ua.lpnu.clearsolutionstask.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ua.lpnu.clearsolutionstask.exception.ErrorDetails;
import ua.lpnu.clearsolutionstask.exception.UserCreationException;
import ua.lpnu.clearsolutionstask.exception.UserNotFoundException;
import ua.lpnu.clearsolutionstask.exception.UserUpdateException;
import ua.lpnu.clearsolutionstask.models.User;
import ua.lpnu.clearsolutionstask.repo.UserService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@WebMvcTest(Users.class)
class UsersTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper mapper;

    private static List<User> userList;
    private static List<User> userIncorrectList;
    @BeforeAll
    static void addUsers(){
        userList = List.of(
                new User(1L,"dima.sb713@gmail.com","Dmytro","Dykyi",
                        LocalDate.of(2000,10,20),
                        "Soborna",25,17,"380632730670"),
                new User(2L,"mike052@gmail.com","Mike","Vazovsky",
                        LocalDate.of(1987,2,17),
                        null,null,null,null),
                new User(3L,"emilia_clarke05@gmail.com","Emilia","Clarke",
                        LocalDate.of(1995,5,9),
                        "Wall",15,null,null),
                new User(5L,"martin_borulia@gmail.com", "Martin", "Borulia",
                        LocalDate.of(1999,1,30),"Mirgorodska",
                        null,null,null)
        );
        userIncorrectList = List.of( new User(null,"dima.sb713@gmail.com","Volodymyr","Vernadskyi",
                LocalDate.of(1998,1,22),
                null,null,null,null),
                    new User(null,"AS12.@gmail.com","Kevin","Strootman",
                LocalDate.of(1990,8,2),
                null,null,null,null),
                    new User(null,"afriad.iam@gmail.com","Justin","Kluivert",
                LocalDate.of(2018,5,9),
                null,null,null,null),
                    new User(null,"atmosphere@gmail.com","Francisco","Alcaron",
                LocalDate.of(2001,12,4),
                null,null,null,null));
    }

    @Test
    void getAllUsers() throws Exception {
        // json format and method cases
        String resultJSON = mapper.writeValueAsString(userList);
        when(userService.getAllUsers()).thenReturn(userList);

        // requests
        this.mockMvc.perform(get("/all_users")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(resultJSON)));
    }

    @Test
    void getUserById() throws Exception {
        // users for requests
        User user1 = userList.get(0);
        User user2 = userList.get(1);
        User user3 = userList.get(2);
        User user4 = userList.get(3);

        // error messages
        ErrorDetails errorDetails1 = new ErrorDetails(LocalDate.now(),
                new UserNotFoundException("User not found with this id : 4").getMessage(),
                HttpStatus.NOT_FOUND,
                "uri=/all_users/4");
        ErrorDetails errorDetails2 = new ErrorDetails(LocalDate.now(),
                new UserNotFoundException("User not found with this id : 0").getMessage(),
                HttpStatus.NOT_FOUND,
                "uri=/all_users/0");

        // json format and method cases
        String resultJSON1 = mapper.writeValueAsString(user1);
        String resultJSON2 = mapper.writeValueAsString(user2);
        String resultJSON3 = mapper.writeValueAsString(user3);
        String resultJSON4 = mapper.writeValueAsString(user4);
        String errorJSON1 = mapper.writeValueAsString(errorDetails1);
        String errorJSON2 = mapper.writeValueAsString(errorDetails2);
        when(userService.getUserById(1L)).thenReturn(Optional.of(user1));
        when(userService.getUserById(2L)).thenReturn(Optional.of(user2));
        when(userService.getUserById(3L)).thenReturn(Optional.of(user3));
        when(userService.getUserById(5L)).thenReturn(Optional.of(user4));

        // requests
        this.mockMvc.perform(get("/all_users/1")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(resultJSON1)));
        this.mockMvc.perform(get("/all_users/2")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(resultJSON2)));
        this.mockMvc.perform(get("/all_users/3")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(resultJSON3)));
        this.mockMvc.perform(get("/all_users/5")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(resultJSON4)));
        this.mockMvc.perform(get("/all_users/4")).andDo(print()).andExpect(status().isNotFound())
                .andExpect(content().string(containsString(errorJSON1)));
        this.mockMvc.perform(get("/all_users/0")).andDo(print()).andExpect(status().isNotFound())
                .andExpect(content().string(containsString(errorJSON2)));
    }

    @Test
    void createUserPost() throws Exception {
        // users for requests
        User userEmailUsed = userIncorrectList.get(0);
        User userEmailIncorrect = userIncorrectList.get(1);
        User userBirthDateIncorrect = userIncorrectList.get(2);
        User userCorrect = userIncorrectList.get(3);

        // error messages
        ErrorDetails errorHandler = new ErrorDetails(
                LocalDate.now(),
                new UserCreationException("User creation process error").getMessage(),
                HttpStatus.CONFLICT,
                "uri=/create"
        );

        // json format and method cases
        String resultJSONError= mapper.writeValueAsString(errorHandler);
        String resultJSONCorrect = mapper.writeValueAsString(userCorrect);
        when(userService.createUser(userEmailUsed)).thenReturn(Optional.empty());
        when(userService.createUser(userEmailIncorrect)).thenReturn(Optional.empty());
        when(userService.createUser(userBirthDateIncorrect)).thenReturn(Optional.empty());
        when(userService.createUser(userCorrect)).thenReturn(Optional.of(userCorrect));

        // requests
        this.mockMvc.perform(post("/create?email=dima.sb713@gmail.com&firstName=Volodymyr&lastName=Vernadskyi&birthDate=1998-01-22"))
                .andDo(print()).andExpect(status().isConflict())
                .andExpect(content().string(containsString(resultJSONError)));
        this.mockMvc.perform(post("/create?email=AS12.@gmail.com&firstName=Kevin&lastName=Strootman&birthDate=1990-08-02"))
                .andDo(print()).andExpect(status().isConflict())
                .andExpect(content().string(containsString(resultJSONError)));
        this.mockMvc.perform(post("/create?email=afriad.iam@gmail.com&firstName=Justin&lastName=Kluivert&birthDate=2018-05-09"))
                .andDo(print()).andExpect(status().isConflict())
                .andExpect(content().string(containsString(resultJSONError)));
        this.mockMvc.perform(post("/create?email=atmosphere@gmail.com&firstName=Francisco&lastName=Alcaron&birthDate=2001-12-04"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(resultJSONCorrect)));
    }

    @Test
    void updateUserByIdSomeFields() throws Exception {
        // users for requests
        User user1 = userList.get(0);
        User user2 = userList.get(1);
        user1.setStreetName("Teatralna");
        user1.setBuildingNumber(34);
        user2.setEmail("dima.sb713@gmail.com");

        // error messages
        ErrorDetails errorHandlerUpdate = new ErrorDetails(
                LocalDate.now(),
                new UserUpdateException("User with this email already exists : " + user2.getEmail()).getMessage(),
                HttpStatus.CONFLICT,
                "uri=/update/2"
        );
        ErrorDetails errorHandlerNotFound = new ErrorDetails(
                LocalDate.now(),
                new UserNotFoundException("User not found with this id : 0").getMessage(),
                HttpStatus.NOT_FOUND,
                "uri=/update/0"
        );

        // json format and method cases
        String resultJSONUser1 = mapper.writeValueAsString(user1);
        String resultJSONErrorUpdate = mapper.writeValueAsString(errorHandlerUpdate);
        String resultJSONNotFound = mapper.writeValueAsString(errorHandlerNotFound);
        when(userService.getUserById(user1.getId())).thenReturn(Optional.of(user1));
        when(userService.getUserById(user2.getId())).thenReturn(Optional.of(user2));
        when(userService.getUserById(0L)).thenReturn(Optional.empty());
        when(userService.updateUser(user1)).thenReturn(true);
        when(userService.updateUser(user2)).thenReturn(false);

        // requests
        this.mockMvc.perform(patch("/update/1?streetName=Teatralna&buildingNumber=34"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(resultJSONUser1)));
        this.mockMvc.perform(patch("/update/2?email=dima.sb713@gmail.com"))
                .andDo(print()).andExpect(status().isConflict())
                .andExpect(content().string(containsString(resultJSONErrorUpdate)));
        this.mockMvc.perform(patch("/update/0?email=dima.sb713@gmail.com"))
                .andDo(print()).andExpect(status().isNotFound())
                .andExpect(content().string(containsString(resultJSONNotFound)));
    }

    @Test
    void updateUserByIdAllFields() throws Exception {
        // users for requests
        User userCorrect = (User) userList.get(0).clone();
        userCorrect.setEmail("dima@gmail.com");
        userCorrect.setFirstName("Dima");
        userCorrect.setLastName("Hvicha");
        userCorrect.setBirthDate(LocalDate.of(2003,10,21));
        userCorrect.setStreetName("Teatralna");
        userCorrect.setBuildingNumber(111);
        userCorrect.setFlatNumber(11);
        userCorrect.setPhoneNumber("");
        User userEmailFail = (User) userCorrect.clone();
        userEmailFail.setId(17L);
        userCorrect.setEmail("dima@gmail.com");

        // error messages
        ErrorDetails errorHandlerUpdateAllFields = new ErrorDetails(
                LocalDate.now(),
                new UserUpdateException("All fields must be updated").getMessage(),
                HttpStatus.CONFLICT,
                "uri=/update/1"
        );
        ErrorDetails errorHandlerUpdate = new ErrorDetails(
                LocalDate.now(),
                new UserUpdateException("User with this email already exists : dima@gmail.com").getMessage(),
                HttpStatus.CONFLICT,
                "uri=/update/17"
        );
        ErrorDetails errorHandlerNotFound = new ErrorDetails(
                LocalDate.now(),
                new UserNotFoundException("User not found with this id : 0").getMessage(),
                HttpStatus.NOT_FOUND,
                "uri=/update/0"
        );
        ErrorDetails errorHandlerDate = new ErrorDetails(
                LocalDate.now(),
                new UserUpdateException("Incorrect date format : 2003-10-1").getMessage(),
                HttpStatus.CONFLICT,
                "uri=/update/17"
        );

        // json format and method cases
        String resultJSONErrorUpdateAllFields = mapper.writeValueAsString(errorHandlerUpdateAllFields);
        String resultJSONErrorEmailExists = mapper.writeValueAsString(errorHandlerUpdate);
        String resultJSONNotFound = mapper.writeValueAsString(errorHandlerNotFound);
        String resultJSONIncorrectDate = mapper.writeValueAsString(errorHandlerDate);
        String resultJSONCorrect = mapper.writeValueAsString(userCorrect);
        when(userService.getUserById(1L)).thenReturn(Optional.of(userList.get(0)));
        when(userService.updateUser(userCorrect)).thenReturn(true);
        when(userService.getUserById(0L)).thenReturn(Optional.empty());
        when(userService.getUserById(17L)).thenReturn(Optional.of(userList.get(0)));
        when(userService.updateUser(userEmailFail)).thenReturn(false);

        // requests
        this.mockMvc.perform(put("/update/1?email=dima@gmail.com&firstName=Dima&lastName=Hvicha&birthDate=2003-10-21&streetName=Teatralna&buildingNumber=111&flatNumber=11&phoneNumber="))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(resultJSONCorrect)));
        userCorrect.setEmail("dima.sb713@gmail.com");
        this.mockMvc.perform(put("/update/1?email=dima.sb713@gmail.com&firstName=Dima&lastName=Hvicha&birthDate=2003-10-21&streetName=Teatralna&buildingNumber=111&flatNumber=11&phoneNumber="))
                .andDo(print()).andExpect(status().isConflict())
                .andExpect(content().string(containsString(resultJSONErrorUpdateAllFields)));
        this.mockMvc.perform(put("/update/0?email=dima.sb713@gmail.com&firstName=Dima&lastName=Hvicha&birthDate=2003-10-21&streetName=Teatralna&buildingNumber=111&flatNumber=11&phoneNumber="))
                .andDo(print()).andExpect(status().isNotFound())
                .andExpect(content().string(containsString(resultJSONNotFound)));
        this.mockMvc.perform(put("/update/17?email=dima@gmail.com&firstName=Dima&lastName=Hvicha&birthDate=2003-10-21&streetName=Teatralna&buildingNumber=111&flatNumber=11&phoneNumber="))
                .andDo(print()).andExpect(status().isConflict())
                .andExpect(content().string(containsString(resultJSONErrorEmailExists)));
        this.mockMvc.perform(put("/update/17?email=dima@gmail.com&firstName=Dima&lastName=Hvicha&birthDate=2003-10-1&streetName=Teatralna&buildingNumber=111&flatNumber=11&phoneNumber="))
                .andDo(print()).andExpect(status().isConflict())
                .andExpect(content().string(containsString(resultJSONIncorrectDate)));
    }

    @Test
    void deleteUser() throws Exception {
        // users for requests
        User userCorrect = userList.get(0);

        // error messages
        ErrorDetails errorHandlerNotFound = new ErrorDetails(
                LocalDate.now(),
                new UserNotFoundException("User not found with this id : 0").getMessage(),
                HttpStatus.NOT_FOUND,
                "uri=/delete/0"
        );

        // json format and method cases
        String resultJSONNotFound = mapper.writeValueAsString(errorHandlerNotFound);
        String resultJSONCorrect = mapper.writeValueAsString(userCorrect);
        when(userService.getUserById(1L)).thenReturn(Optional.of(userCorrect));
        when(userService.deleteUser(1L)).thenReturn(true);
        when(userService.getUserById(0L)).thenReturn(Optional.empty());

        // requests
        this.mockMvc.perform(delete("/delete/1"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(resultJSONCorrect)));
        this.mockMvc.perform(delete("/delete/0"))
                .andDo(print()).andExpect(status().isNotFound())
                .andExpect(content().string(containsString(resultJSONNotFound)));
    }
}