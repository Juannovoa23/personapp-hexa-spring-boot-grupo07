package co.edu.javeriana.as.personapp.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudyRequest {

    // PK
    private String idProf;   // id de la profesión (Profession.identification)
    private String ccPer;    // identificación de la persona (Person.identification)

    // Datos del estudio
    private String graduationDate;  // "yyyy-MM-dd"
    private String universityName;

    // BD a usar: "MARIA" o "MONGO"
    private String database;
}
