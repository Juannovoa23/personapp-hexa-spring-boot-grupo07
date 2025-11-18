package co.edu.javeriana.as.personapp.adapter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.PersonInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PersonOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.PersonUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.mapper.PersonaMapperRest;
import co.edu.javeriana.as.personapp.model.request.PersonaRequest;
import co.edu.javeriana.as.personapp.model.response.PersonaResponse;
import co.edu.javeriana.as.personapp.model.response.Response;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class PersonaInputAdapterRest {

	@Autowired
	@Qualifier("personOutputAdapterMaria")
	private PersonOutputPort personOutputPortMaria;

	@Autowired
	@Qualifier("personOutputAdapterMongo")
	private PersonOutputPort personOutputPortMongo;

	@Autowired
	private PersonaMapperRest personaMapperRest;

	PersonInputPort personInputPort;

	private String setPersonOutputPortInjection(String dbOption) throws InvalidOptionException {
		if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
			personInputPort = new PersonUseCase(personOutputPortMaria);
			return DatabaseOption.MARIA.toString();
		} else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
			personInputPort = new PersonUseCase(personOutputPortMongo);
			return  DatabaseOption.MONGO.toString();
		} else {
			throw new InvalidOptionException("Invalid database option: " + dbOption);
		}
	}

	public List<PersonaResponse> historial(String database) {
		log.info("Into historial PersonaEntity in Input Adapter");
		try {
			if(setPersonOutputPortInjection(database).equalsIgnoreCase(DatabaseOption.MARIA.toString())){
				return personInputPort.findAll().stream().map(personaMapperRest::fromDomainToAdapterRestMaria)
						.collect(Collectors.toList());
			}else {
				return personInputPort.findAll().stream().map(personaMapperRest::fromDomainToAdapterRestMongo)
						.collect(Collectors.toList());
			}
			
		} catch (InvalidOptionException e) {
			log.warn(e.getMessage());
			return new ArrayList<PersonaResponse>();
		}
	}

	public PersonaResponse crearPersona(PersonaRequest request) {
		try {
			setPersonOutputPortInjection(request.getDatabase());
			Person person = personInputPort.create(personaMapperRest.fromAdapterToDomain(request));
			return personaMapperRest.fromDomainToAdapterRestMaria(person);
		} catch (InvalidOptionException e) {
			log.warn(e.getMessage());
			//return new PersonaResponse("", "", "", "", "", "", "");
		}
		return null;
	}
	public PersonaResponse buscarPersona(String database, String dni) {
		try {
			setPersonOutputPortInjection(database);
			Person person = personInputPort.findOne(Integer.parseInt(dni));

			if (database.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
				return personaMapperRest.fromDomainToAdapterRestMaria(person);
			} else {
				return personaMapperRest.fromDomainToAdapterRestMongo(person);
			}
		} catch (InvalidOptionException | NoExistException e) {
			log.warn(e.getMessage());
			return null;
		}
	}

	public PersonaResponse editarPersona(String database, PersonaRequest request) {
		try {
			setPersonOutputPortInjection(database);
			Person person = personaMapperRest.fromAdapterToDomain(request);
			Person edited = personInputPort.edit(Integer.parseInt(request.getDni()), person);

			if (database.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
				return personaMapperRest.fromDomainToAdapterRestMaria(edited);
			} else {
				return personaMapperRest.fromDomainToAdapterRestMongo(edited);
			}
		} catch (InvalidOptionException | NoExistException e) {
			log.warn(e.getMessage());
			return null;
		}
	}

	public Boolean eliminarPersona(String database, String dni) {
		try {
			setPersonOutputPortInjection(database);
			return personInputPort.drop(Integer.parseInt(dni));
		} catch (InvalidOptionException | NoExistException e) {
			log.warn(e.getMessage());
			return false;
		}
	}

	    /**
     * Obtener una persona por identificación (dni)
     */
    public PersonaResponse buscarPersona(String database, Integer identification) {
        try {
            setPersonOutputPortInjection(database);

            Person person = personInputPort.findOne(identification);

            if (person == null) {
                log.warn("Person with id {} not found in {}", identification, database);
                return null;
            }

            DatabaseOption dbOp = DatabaseOption.valueOf(database.toUpperCase());
            if (dbOp == DatabaseOption.MARIA) {
                return personaMapperRest.fromDomainToAdapterRestMaria(person);
            } else if (dbOp == DatabaseOption.MONGO) {
                return personaMapperRest.fromDomainToAdapterRestMongo(person);
            } else {
                throw new InvalidOptionException("Invalid database option: " + database);
            }

        } catch (InvalidOptionException | NoExistException e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    /**
     * Actualizar una persona
     */
    public PersonaResponse editarPersona(String database, Integer identification, PersonaRequest request) {
        try {
            setPersonOutputPortInjection(database);

            // nos aseguramos de que el dni del body coincida con el de la URL
            request.setDni(identification.toString());

            Person personToEdit = personaMapperRest.fromAdapterToDomain(request);
            Person personEdited = personInputPort.edit(identification, personToEdit);

            DatabaseOption dbOp = DatabaseOption.valueOf(database.toUpperCase());
            if (dbOp == DatabaseOption.MARIA) {
                return personaMapperRest.fromDomainToAdapterRestMaria(personEdited);
            } else if (dbOp == DatabaseOption.MONGO) {
                return personaMapperRest.fromDomainToAdapterRestMongo(personEdited);
            } else {
                throw new InvalidOptionException("Invalid database option: " + database);
            }

        } catch (InvalidOptionException | NoExistException e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    /**
     * Eliminar una persona
     */
    public Response eliminarPersona(String database, Integer identification) {
        try {
            setPersonOutputPortInjection(database);

            Boolean deleted = personInputPort.drop(identification);

            if (deleted != null && deleted) {
                return new Response(
                        "OK",
                        "Person with id " + identification + " was deleted successfully from " + database,
                        LocalDateTime.now()
                );
            } else {
                return new Response(
                        "ERROR",
                        "Person with id " + identification + " could not be deleted from " + database,
                        LocalDateTime.now()
                );
            }

        } catch (InvalidOptionException | NoExistException e) {
            log.warn(e.getMessage());
            return new Response("ERROR", e.getMessage(), LocalDateTime.now());
        }
    }



}
