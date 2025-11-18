package co.edu.javeriana.as.personapp.mapper;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Gender;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.model.request.PersonaRequest;
import co.edu.javeriana.as.personapp.model.response.PersonaResponse;

@Mapper
public class PersonaMapperRest {
	
	public PersonaResponse fromDomainToAdapterRestMaria(Person person) {
		return fromDomainToAdapterRest(person, "MariaDB");
	}
	public PersonaResponse fromDomainToAdapterRestMongo(Person person) {
		return fromDomainToAdapterRest(person, "MongoDB");
	}
	
	public PersonaResponse fromDomainToAdapterRest(Person person, String database) {
			return new PersonaResponse(
					person.getIdentification()+"", 
					person.getFirstName(), 
					person.getLastName(), 
					person.getAge()+"", 
					person.getGender().toString(), 
					database,
					"OK");
		}

		public Person fromAdapterToDomain(PersonaRequest request) {
		Integer identification = null;
		Integer age = null;

		// DNI → Integer
		if (request.getDni() != null && !request.getDni().isBlank()) {
			identification = Integer.parseInt(request.getDni());
		}

		// age → Integer
		if (request.getAge() != null && !request.getAge().isBlank()) {
			age = Integer.parseInt(request.getAge());
		}

		// sex → Gender enum
		Gender gender;
		switch (request.getSex().toUpperCase()) {
			case "M":
			case "MALE":
				gender = Gender.MALE;
				break;
			case "F":
			case "FEMALE":
				gender = Gender.FEMALE;
				break;
			default:
				gender = Gender.OTHER;
		}

		// Crear objeto de dominio
		Person person = new Person(
				identification,
				request.getFirstName(),
				request.getLastName(),
				gender
		);
		person.setAge(age);

		return person;
	}
		
}
