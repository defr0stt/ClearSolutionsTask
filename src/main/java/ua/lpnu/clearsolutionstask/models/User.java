package ua.lpnu.clearsolutionstask.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "user_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User implements Cloneable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_sequence")
    @SequenceGenerator(name = "user_sequence", sequenceName = "user_sequence", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateSerializer.class)
    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "street")
    private String streetName;

    @Column(name = "building_number")
    private Integer buildingNumber;

    @Column(name = "flat_number")
    private Integer flatNumber;

    @Column(name = "phone_number")
    private String phoneNumber;

    public User(String email,
                String firstName,
                String lastName,
                LocalDate birthDate,
                String streetName,
                Integer buildingNumber,
                Integer flatNumber,
                String phoneNumber) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.streetName = streetName;
        this.buildingNumber = buildingNumber;
        this.flatNumber = flatNumber;
        this.phoneNumber = phoneNumber;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return new User(this.getId(),this.getEmail(),this.getFirstName(),this.getLastName(),this.getBirthDate(),
                this.getStreetName(),this.getBuildingNumber(),this.getFlatNumber(),this.getPhoneNumber());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(email, user.email) && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(birthDate, user.birthDate) && Objects.equals(streetName, user.streetName) && Objects.equals(buildingNumber, user.buildingNumber) && Objects.equals(flatNumber, user.flatNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, firstName, lastName, birthDate, streetName, buildingNumber, flatNumber);
    }

    public boolean isDifferentByAllFields(User user){
        List<Object> characteristicOtherUser =
                List.of(user.getEmail(),user.getFirstName(),user.getLastName(),user.getBirthDate(),
                        user.getStreetName(),user.getBuildingNumber(),user.getFlatNumber(),user.getPhoneNumber());
        List<Object> characteristic =
                List.of(this.getEmail(),this.getFirstName(),this.getLastName(),this.getBirthDate(),
                        this.getStreetName(),this.getBuildingNumber(),this.getFlatNumber(),this.getPhoneNumber());
        for (int i=0;i<8;i++){
            Object u1 = characteristic.get(i);
            Object u2 = characteristicOtherUser.get(i);
            if(u1 != null && u2 != null) {
                if (u1.equals(u2))
                    return false;
            } else if(u2 == null && i < 4)
                return false;
              else if(u1 == null && u2 == null)
                return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthDate=" + birthDate +
                ", streetName='" + streetName + '\'' +
                ", buildingNumber=" + buildingNumber +
                ", flatNumber=" + flatNumber +
                '}';
    }
}