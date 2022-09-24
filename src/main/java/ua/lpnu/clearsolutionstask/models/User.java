package ua.lpnu.clearsolutionstask.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "user_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
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