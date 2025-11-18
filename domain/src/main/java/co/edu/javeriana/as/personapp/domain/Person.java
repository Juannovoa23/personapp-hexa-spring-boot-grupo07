package co.edu.javeriana.as.personapp.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Person {

    @NonNull
    private Integer identification;   // corresponde a cc INT(15)

    @NonNull
    private String firstName;         // nombre VARCHAR(45)

    @NonNull
    private String lastName;          // apellido VARCHAR(45)

    @NonNull
    private Gender gender;            // ENUM('M','F','OTHER')

    private Integer age;              // edad INT(3)

    // Relaciones con teléfono y estudios
    @ToString.Exclude
    private List<Phone> phoneNumbers; // teléfonos asociados

    @ToString.Exclude
    private List<Study> studies;      // estudios asociados

    public Boolean isValidAge() {
        return this.age >= 0;
    }
}
