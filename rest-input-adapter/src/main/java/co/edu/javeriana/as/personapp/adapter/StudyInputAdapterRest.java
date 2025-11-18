package co.edu.javeriana.as.personapp.adapter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.StudyInputPort;
import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.StudyUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mapper.StudyMapperRest;
import co.edu.javeriana.as.personapp.model.request.StudyRequest;
import co.edu.javeriana.as.personapp.model.response.Response;
import co.edu.javeriana.as.personapp.model.response.StudyResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class StudyInputAdapterRest {

    @Autowired
    @Qualifier("studyOutputAdapterMaria")
    private StudyOutputPort studyOutputPortMaria;

    @Autowired
    @Qualifier("studyOutputAdapterMongo")
    private StudyOutputPort studyOutputPortMongo;

    @Autowired
    private StudyMapperRest studyMapperRest;

    private StudyInputPort studyInputPort;

    private String setStudyOutputPortInjection(String dbOption) throws InvalidOptionException {
        if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
            studyInputPort = new StudyUseCase(studyOutputPortMaria);
            return DatabaseOption.MARIA.toString();
        } else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
            studyInputPort = new StudyUseCase(studyOutputPortMongo);
            return DatabaseOption.MONGO.toString();
        } else {
            throw new InvalidOptionException("Invalid database option: " + dbOption);
        }
    }

    public List<StudyResponse> historial(String database) {
        log.info("Into historial Study in Input Adapter");
        try {
            String db = setStudyOutputPortInjection(database);

            if (db.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
                return studyInputPort.findAll().stream()
                        .map(studyMapperRest::fromDomainToAdapterRestMaria)
                        .collect(Collectors.toList());
            } else {
                return studyInputPort.findAll().stream()
                        .map(studyMapperRest::fromDomainToAdapterRestMongo)
                        .collect(Collectors.toList());
            }
        } catch (InvalidOptionException e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    public StudyResponse crearStudy(StudyRequest request) {
        log.info("Into crearStudy in Input Adapter");
        try {
            setStudyOutputPortInjection(request.getDatabase());

            Study study = studyMapperRest.fromAdapterToDomain(request);
            Study created = studyInputPort.create(study);

            if (request.getDatabase().equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
                return studyMapperRest.fromDomainToAdapterRestMaria(created);
            } else {
                return studyMapperRest.fromDomainToAdapterRestMongo(created);
            }
        } catch (InvalidOptionException e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    public StudyResponse buscarStudy(String database, Integer idProf, Integer ccPer) {
        log.info("Into buscarStudy in Input Adapter");
        try {
            String db = setStudyOutputPortInjection(database);

            Study study = studyInputPort.findOne(idProf, ccPer);

            if (db.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
                return studyMapperRest.fromDomainToAdapterRestMaria(study);
            } else {
                return studyMapperRest.fromDomainToAdapterRestMongo(study);
            }
        } catch (InvalidOptionException | NoExistException e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    public StudyResponse editarStudy(String database, StudyRequest request) {
        log.info("Into editarStudy in Input Adapter");
        try {
            setStudyOutputPortInjection(database);

            Integer idProf = Integer.parseInt(request.getIdProf());
            Integer ccPer = Integer.parseInt(request.getCcPer());

            Study study = studyMapperRest.fromAdapterToDomain(request);
            Study edited = studyInputPort.edit(idProf, ccPer, study);

            if (database.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
                return studyMapperRest.fromDomainToAdapterRestMaria(edited);
            } else {
                return studyMapperRest.fromDomainToAdapterRestMongo(edited);
            }
        } catch (InvalidOptionException | NoExistException | NumberFormatException e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    public Response eliminarStudy(String database, Integer idProf, Integer ccPer) {
        log.info("Into eliminarStudy in Input Adapter");
        try {
            setStudyOutputPortInjection(database);

            Boolean deleted = studyInputPort.drop(idProf, ccPer);

            if (deleted) {
                return new Response(
                        "OK",
                        "Study (" + idProf + ", " + ccPer + ") was deleted from " + database,
                        LocalDateTime.now()
                );
            } else {
                return new Response(
                        "ERROR",
                        "Study (" + idProf + ", " + ccPer + ") could not be deleted from " + database,
                        LocalDateTime.now()
                );
            }
        } catch (InvalidOptionException | NoExistException e) {
            log.warn(e.getMessage());
            return new Response("ERROR", e.getMessage(), LocalDateTime.now());
        }
    }
}
