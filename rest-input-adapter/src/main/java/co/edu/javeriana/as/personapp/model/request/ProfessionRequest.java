package co.edu.javeriana.as.personapp.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfessionRequest {

    private String identification;   // Identificación de la persona
    private String name;             // Nombre de la profesión
    private String description;      // Descripción o detalles
    private String database;         // MONGO o MARIA (DB a utilizar)
}
