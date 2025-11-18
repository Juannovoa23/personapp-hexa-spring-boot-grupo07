package co.edu.javeriana.as.personapp.adapter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.PhoneInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PhoneOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.PhoneUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.mapper.PhoneMapperRest;
import co.edu.javeriana.as.personapp.model.request.PhoneRequest;
import co.edu.javeriana.as.personapp.model.response.PhoneResponse;
import co.edu.javeriana.as.personapp.model.response.Response;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class PhoneInputAdapterRest {

    @Autowired
    @Qualifier("phoneOutputAdapterMaria")
    private PhoneOutputPort phoneOutputPortMaria;

    @Autowired
    @Qualifier("phoneOutputAdapterMongo")
    private PhoneOutputPort phoneOutputPortMongo;

    @Autowired
    private PhoneMapperRest phoneMapperRest;

    private PhoneInputPort phoneInputPort;

    private String setPhoneOutputPortInjection(String dbOption) throws InvalidOptionException {
        if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
            phoneInputPort = new PhoneUseCase(phoneOutputPortMaria);
            return DatabaseOption.MARIA.toString();
        } else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
            phoneInputPort = new PhoneUseCase(phoneOutputPortMongo);
            return DatabaseOption.MONGO.toString();
        } else {
            throw new InvalidOptionException("Invalid database option: " + dbOption);
        }
    }

    public List<PhoneResponse> historial(String database) {
        log.info("Into historial Phone in Input Adapter");
        try {
            if (setPhoneOutputPortInjection(database)
                    .equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
                return phoneInputPort.findAll().stream()
                        .map(phoneMapperRest::fromDomainToAdapterRestMaria)
                        .collect(Collectors.toList());
            } else {
                return phoneInputPort.findAll().stream()
                        .map(phoneMapperRest::fromDomainToAdapterRestMongo)
                        .collect(Collectors.toList());
            }
        } catch (InvalidOptionException e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    public PhoneResponse crearPhone(PhoneRequest request) {
        log.info("Into crearPhone in Input Adapter");
        try {
            setPhoneOutputPortInjection(request.getDatabase());
            Phone phone = phoneMapperRest.fromAdapterToDomain(request);
            Phone created = phoneInputPort.create(phone);

            if (request.getDatabase().equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
                return phoneMapperRest.fromDomainToAdapterRestMaria(created);
            } else {
                return phoneMapperRest.fromDomainToAdapterRestMongo(created);
            }
        } catch (InvalidOptionException e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    public PhoneResponse buscarPhone(String database, String number) {
        log.info("Into buscarPhone in Input Adapter");
        try {
            setPhoneOutputPortInjection(database);
            Phone phone = phoneInputPort.findOne(number);

            if (database.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
                return phoneMapperRest.fromDomainToAdapterRestMaria(phone);
            } else {
                return phoneMapperRest.fromDomainToAdapterRestMongo(phone);
            }
        } catch (InvalidOptionException | NoExistException e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    public PhoneResponse editarPhone(String database, PhoneRequest request) {
        log.info("Into editarPhone in Input Adapter");
        try {
            setPhoneOutputPortInjection(database);
            Phone phone = phoneMapperRest.fromAdapterToDomain(request);
            Phone edited = phoneInputPort.edit(request.getNumber(), phone);

            if (database.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
                return phoneMapperRest.fromDomainToAdapterRestMaria(edited);
            } else {
                return phoneMapperRest.fromDomainToAdapterRestMongo(edited);
            }
        } catch (InvalidOptionException | NoExistException e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    public Response eliminarPhone(String database, String number) {
        log.info("Into eliminarPhone in Input Adapter");
        try {
            setPhoneOutputPortInjection(database);
            Boolean deleted = phoneInputPort.drop(number);

            if (deleted) {
                return new Response(
                        "OK",
                        "Phone with number " + number + " was deleted from " + database,
                        LocalDateTime.now()
                );
            } else {
                return new Response(
                        "ERROR",
                        "Phone with number " + number + " could not be deleted from " + database,
                        LocalDateTime.now()
                );
            }

        } catch (InvalidOptionException | NoExistException e) {
            log.warn(e.getMessage());
            return new Response("ERROR", e.getMessage(), LocalDateTime.now());
        }
    }
}
