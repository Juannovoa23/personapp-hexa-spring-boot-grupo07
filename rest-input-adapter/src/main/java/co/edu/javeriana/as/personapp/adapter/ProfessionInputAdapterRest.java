package co.edu.javeriana.as.personapp.adapter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.ProfessionInputPort;
import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.ProfessionUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.mapper.ProfessionMapperRest;
import co.edu.javeriana.as.personapp.model.request.ProfessionRequest;
import co.edu.javeriana.as.personapp.model.response.ProfessionResponse;
import co.edu.javeriana.as.personapp.model.response.Response;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class ProfessionInputAdapterRest {

    @Autowired
    @Qualifier("professionOutputAdapterMaria")
    private ProfessionOutputPort professionOutputPortMaria;

    @Autowired
    @Qualifier("professionOutputAdapterMongo")
    private ProfessionOutputPort professionOutputPortMongo;

    @Autowired
    private ProfessionMapperRest professionMapperRest;

    private ProfessionInputPort professionInputPort;

    /**
     * Selecciona la BD (MariaDB o Mongo)
     */
    private String setProfessionOutputPortInjection(String dbOption) throws InvalidOptionException {
        if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
            professionInputPort = new ProfessionUseCase(professionOutputPortMaria);
            return DatabaseOption.MARIA.toString();

        } else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
            professionInputPort = new ProfessionUseCase(professionOutputPortMongo);
            return DatabaseOption.MONGO.toString();
        }

        throw new InvalidOptionException("Invalid database option: " + dbOption);
    }

    /**
     * Listar todas las profesiones
     */
    public List<ProfessionResponse> historial(String database) {
        log.info("Into historial Profession in Input Adapter");

        try {
            String db = setProfessionOutputPortInjection(database);

            if (db.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
                return professionInputPort.findAll().stream()
                        .map(professionMapperRest::fromDomainToAdapterRestMaria)
                        .collect(Collectors.toList());
            } else {
                return professionInputPort.findAll().stream()
                        .map(professionMapperRest::fromDomainToAdapterRestMongo)
                        .collect(Collectors.toList());
            }

        } catch (InvalidOptionException e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    /**
     * Crear profesión
     */
    public ProfessionResponse crearProfession(ProfessionRequest request) {
        log.info("Into crearProfession in Input Adapter");

        try {
            setProfessionOutputPortInjection(request.getDatabase());

            Profession profession = professionMapperRest.fromAdapterToDomain(request);
            Profession created = professionInputPort.create(profession);

            if (request.getDatabase().equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
                return professionMapperRest.fromDomainToAdapterRestMaria(created);
            } else {
                return professionMapperRest.fromDomainToAdapterRestMongo(created);
            }

        } catch (InvalidOptionException e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    /**
     * Buscar profesión por identificación + nombre
     */
    public ProfessionResponse buscarProfession(String database, Integer identification, String name) {
        log.info("Into buscarProfession in Input Adapter");

        try {
            String db = setProfessionOutputPortInjection(database);

            Profession profession = professionInputPort.findOne(identification, name);

            if (db.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
                return professionMapperRest.fromDomainToAdapterRestMaria(profession);
            } else {
                return professionMapperRest.fromDomainToAdapterRestMongo(profession);
            }

        } catch (InvalidOptionException | NoExistException e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    /**
     * Editar profesión
     */
    public ProfessionResponse editarProfession(String database, ProfessionRequest request) {
        log.info("Into editarProfession in Input Adapter");

        try {
            setProfessionOutputPortInjection(database);

            Integer id = Integer.parseInt(request.getIdentification());

            Profession profession = professionMapperRest.fromAdapterToDomain(request);

            Profession edited = professionInputPort.edit(id, request.getName(), profession);

            if (database.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
                return professionMapperRest.fromDomainToAdapterRestMaria(edited);
            } else {
                return professionMapperRest.fromDomainToAdapterRestMongo(edited);
            }

        } catch (InvalidOptionException | NoExistException e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    /**
     * Eliminar profesión
     */
    public Response eliminarProfession(String database, Integer identification, String name) {
        log.info("Into eliminarProfession in Input Adapter");

        try {
            setProfessionOutputPortInjection(database);

            Boolean deleted = professionInputPort.drop(identification, name);

            if (deleted) {
                return new Response(
                        "OK",
                        "Profession (" + identification + ", " + name + ") deleted from " + database,
                        LocalDateTime.now()
                );
            } else {
                return new Response(
                        "ERROR",
                        "Profession (" + identification + ", " + name + ") could not be deleted from " + database,
                        LocalDateTime.now()
                );
            }

        } catch (InvalidOptionException | NoExistException e) {
            log.warn(e.getMessage());
            return new Response("ERROR", e.getMessage(), LocalDateTime.now());
        }
    }
}
