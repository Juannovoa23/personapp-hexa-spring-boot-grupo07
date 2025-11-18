package co.edu.javeriana.as.personapp.application.usecase;

import java.util.List;

import co.edu.javeriana.as.personapp.application.port.in.PersonInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PersonOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.UseCase;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.domain.Study;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UseCase
public class PersonUseCase implements PersonInputPort {

    // Puerto de salida que se usará para ir a la BD
    private PersonOutputPort personPersintence;

    // 🔹 Constructor vacío (lo puede usar Spring si quiere inyectar luego con setPersintence)
    public PersonUseCase() {
    }

    // 🔹 Constructor que espera un PersonOutputPort
    //     -> ESTE es el que usa PersonaInputAdapterRest: new PersonUseCase(personOutputPortX)
    public PersonUseCase(PersonOutputPort personPersintence) {
        this.personPersintence = personPersintence;
    }

    @Override
    public void setPersintence(PersonOutputPort personPersintence) {
        log.debug("Setting persistence implementation in PersonUseCase");
        this.personPersintence = personPersintence;
    }

    @Override
    public Person create(Person person) {
        log.debug("Into create on PersonUseCase");
        if (person == null) {
            throw new IllegalArgumentException("Person cannot be null");
        }
        if (!person.isValidAge()) {
            throw new IllegalArgumentException("Age must be greater or equal than zero");
        }
        return personPersintence.save(person);
    }

    @Override
    public Person edit(Integer identification, Person person) throws NoExistException {
        log.debug("Into edit on PersonUseCase");
        Person oldPerson = personPersintence.findById(identification);
        if (oldPerson == null) {
            throw new NoExistException(
                    "The person with id " + identification + " does not exist into db, cannot be edited");
        }
        person.setIdentification(identification);
        return personPersintence.save(person);
    }

    @Override
    public Boolean drop(Integer identification) throws NoExistException {
        log.debug("Into drop on PersonUseCase");
        Person oldPerson = personPersintence.findById(identification);
        if (oldPerson == null) {
            throw new NoExistException(
                    "The person with id " + identification + " does not exist into db, cannot be deleted");
        }
        return personPersintence.delete(identification);
    }

    @Override
    public List<Person> findAll() {
        log.debug("Into findAll on PersonUseCase");
        return personPersintence.find();
    }

    @Override
    public Person findOne(Integer identification) throws NoExistException {
        log.debug("Into findOne on PersonUseCase");
        Person person = personPersintence.findById(identification);
        if (person == null) {
            throw new NoExistException(
                    "The person with id " + identification + " does not exist into db");
        }
        return person;
    }

    @Override
    public Integer count() {
        log.debug("Into count on PersonUseCase");
        return personPersintence.find().size();
    }

    @Override
    public List<Phone> getPhones(Integer identification) throws NoExistException {
        log.debug("Into getPhones on PersonUseCase");
        Person oldPerson = personPersintence.findById(identification);
        if (oldPerson != null)
            return oldPerson.getPhoneNumbers();
        throw new NoExistException(
                "The person with id " + identification + " does not exist into db, cannot be getting phones");
    }

    @Override
    public List<Study> getStudies(Integer identification) throws NoExistException {
        log.debug("Into getStudies on PersonUseCase");
        Person oldPerson = personPersintence.findById(identification);
        if (oldPerson != null)
            return oldPerson.getStudies();
        throw new NoExistException(
                "The person with id " + identification + " does not exist into db, cannot be getting studies");
    }
}
