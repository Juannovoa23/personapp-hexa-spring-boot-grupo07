package co.edu.javeriana.as.personapp.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonaRequest {
    
    private String dni;        // identificación de la persona
    private String firstName;  // nombre
    private String lastName;   // apellido
    private String age;        // edad (viene como String en el JSON)
    private String sex;        // M, F, OTHER
    private String database;   // MARIA o MONGO
}
