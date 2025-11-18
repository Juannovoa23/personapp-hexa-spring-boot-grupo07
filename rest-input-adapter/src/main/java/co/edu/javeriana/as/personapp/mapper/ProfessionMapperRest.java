package co.edu.javeriana.as.personapp.mapper;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.model.request.ProfessionRequest;
import co.edu.javeriana.as.personapp.model.response.ProfessionResponse;

@Mapper
public class ProfessionMapperRest {

    public ProfessionResponse fromDomainToAdapterRestMaria(Profession profession) {
        return fromDomainToAdapterRest(profession, "MariaDB");
    }

    public ProfessionResponse fromDomainToAdapterRestMongo(Profession profession) {
        return fromDomainToAdapterRest(profession, "MongoDB");
    }

    private ProfessionResponse fromDomainToAdapterRest(Profession profession, String database) {
        String identification = profession.getIdentification() != null
                ? profession.getIdentification().toString()
                : null;

        return new ProfessionResponse(
                identification,
                profession.getName(),
                profession.getDescription(),
                database,
                "OK"
        );
    }

    public Profession fromAdapterToDomain(ProfessionRequest request) {
        Profession profession = new Profession();

        // identification viene como String en el request
        if (request.getIdentification() != null && !request.getIdentification().isEmpty()) {
            try {
                Integer id = Integer.parseInt(request.getIdentification());
                profession.setIdentification(id);
            } catch (NumberFormatException e) {
                // si viene mal, lo dejamos nulo
            }
        }

        profession.setName(request.getName());
        profession.setDescription(request.getDescription());

        // studies no se mapean desde REST en este nivel
        return profession;
    }
}
