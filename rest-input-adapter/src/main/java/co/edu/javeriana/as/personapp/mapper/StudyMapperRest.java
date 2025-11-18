package co.edu.javeriana.as.personapp.mapper;

import java.time.LocalDate;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.model.request.StudyRequest;
import co.edu.javeriana.as.personapp.model.response.StudyResponse;

@Mapper
public class StudyMapperRest {

    public StudyResponse fromDomainToAdapterRestMaria(Study study) {
        return fromDomainToAdapterRest(study, "MariaDB");
    }

    public StudyResponse fromDomainToAdapterRestMongo(Study study) {
        return fromDomainToAdapterRest(study, "MongoDB");
    }

    private StudyResponse fromDomainToAdapterRest(Study study, String database) {
        String idProf = null;
        String ccPer = null;
        String graduationDate = null;

        if (study.getProfession() != null && study.getProfession().getIdentification() != null) {
            idProf = study.getProfession().getIdentification().toString();
        }

        if (study.getPerson() != null && study.getPerson().getIdentification() != null) {
            ccPer = study.getPerson().getIdentification().toString();
        }

        if (study.getGraduationDate() != null) {
            graduationDate = study.getGraduationDate().toString(); // ISO yyyy-MM-dd
        }

        return new StudyResponse(
                idProf,
                ccPer,
                graduationDate,
                study.getUniversityName(),
                database,
                "OK"
        );
    }

    public Study fromAdapterToDomain(StudyRequest request) {
        Study study = new Study();

        // Reconstruimos Person y Profession a partir de los IDs
        Integer idProf = null;
        Integer ccPer = null;

        try {
            if (request.getIdProf() != null && !request.getIdProf().isEmpty()) {
                idProf = Integer.parseInt(request.getIdProf());
            }
        } catch (NumberFormatException e) {
            // lo dejamos null
        }

        try {
            if (request.getCcPer() != null && !request.getCcPer().isEmpty()) {
                ccPer = Integer.parseInt(request.getCcPer());
            }
        } catch (NumberFormatException e) {
            // lo dejamos null
        }

        if (ccPer != null) {
            Person person = new Person(ccPer);
            study.setPerson(person);
        }

        if (idProf != null) {
            // No conocemos el nombre de la profesión aquí, solo el idProf
            Profession profession = new Profession(idProf, null);
            study.setProfession(profession);
        }

        if (request.getGraduationDate() != null && !request.getGraduationDate().isEmpty()) {
            study.setGraduationDate(LocalDate.parse(request.getGraduationDate()));
        }

        study.setUniversityName(request.getUniversityName());

        return study;
    }
}
