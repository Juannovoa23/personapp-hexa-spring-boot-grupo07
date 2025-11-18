package co.edu.javeriana.as.personapp.model.response;

import co.edu.javeriana.as.personapp.model.request.StudyRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudyResponse extends StudyRequest {

    private String status;

    public StudyResponse(String idProf,
                         String ccPer,
                         String graduationDate,
                         String universityName,
                         String database,
                         String status) {
        super(idProf, ccPer, graduationDate, universityName, database);
        this.status = status;
    }
}
