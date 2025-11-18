package co.edu.javeriana.as.personapp.mapper;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.model.request.PhoneRequest;
import co.edu.javeriana.as.personapp.model.response.PhoneResponse;

@Mapper
public class PhoneMapperRest {

    public PhoneResponse fromDomainToAdapterRestMaria(Phone phone) {
        return fromDomainToAdapterRest(phone, "MariaDB");
    }

    public PhoneResponse fromDomainToAdapterRestMongo(Phone phone) {
        return fromDomainToAdapterRest(phone, "MongoDB");
    }

    private PhoneResponse fromDomainToAdapterRest(Phone phone, String database) {
        String ownerId = phone.getOwner() != null && phone.getOwner().getIdentification() != null
                ? phone.getOwner().getIdentification().toString()
                : null;

        return new PhoneResponse(
                phone.getNumber(),
                phone.getCompany(),
                ownerId,
                database,
                "OK"
        );
    }

    public Phone fromAdapterToDomain(PhoneRequest request) {

        Phone phone = new Phone();
        phone.setNumber(request.getNumber());
        phone.setCompany(request.getCompany());

        // Sólo seteamos el id del dueño; los demás datos de Persona pueden cargarse en otra capa si quieres.
        if (request.getOwnerId() != null && !request.getOwnerId().isEmpty()) {
            try {
                Integer ownerIdentification = Integer.parseInt(request.getOwnerId());
                Person owner = new Person();
                owner.setIdentification(ownerIdentification);
                phone.setOwner(owner);
            } catch (NumberFormatException e) {
                // si viene mal, simplemente no seteamos dueño
            }
        }

        return phone;
    }
}
