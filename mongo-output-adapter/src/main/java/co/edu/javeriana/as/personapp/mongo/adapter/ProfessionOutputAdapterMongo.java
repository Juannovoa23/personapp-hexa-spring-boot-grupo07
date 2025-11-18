package co.edu.javeriana.as.personapp.mongo.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.mongo.document.ProfesionDocument;
import co.edu.javeriana.as.personapp.mongo.mapper.ProfesionMapperMongo;
import co.edu.javeriana.as.personapp.mongo.repository.ProfesionRepositoryMongo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter("professionOutputAdapterMongo")
public class ProfessionOutputAdapterMongo implements ProfessionOutputPort {

    @Autowired
    private ProfesionRepositoryMongo profesionRepositoryMongo;

    @Autowired
    private ProfesionMapperMongo profesionMapperMongo;

    private String buildId(Integer identification, String professionName) {
        return identification + "-" + professionName;
    }

    @Override
    public Profession save(Profession profession) {
        log.debug("Into save on ProfessionOutputAdapterMongo");
        ProfesionDocument doc = profesionMapperMongo.fromDomainToAdapter(profession);

        if (doc.getId() == null
                && profession.getIdentification() != null
                && profession.getName() != null) {
            doc.setId(buildId(profession.getIdentification(), profession.getName()));
        }

        ProfesionDocument saved = profesionRepositoryMongo.save(doc);
        return profesionMapperMongo.fromAdapterToDomain(saved);
    }

    @Override
    public Boolean delete(Integer identification, String professionName) {
        log.debug("Into delete on ProfessionOutputAdapterMongo");
        String id = buildId(identification, professionName);
        if (!profesionRepositoryMongo.existsById(id)) {
            return Boolean.FALSE;
        }
        profesionRepositoryMongo.deleteById(id);
        return Boolean.TRUE;
    }

    @Override
    public List<Profession> find() {
        log.debug("Into find on ProfessionOutputAdapterMongo");
        return profesionRepositoryMongo.findAll().stream()
                .map(profesionMapperMongo::fromAdapterToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Profession findById(Integer identification, String professionName) {
        log.debug("Into findById on ProfessionOutputAdapterMongo");
        String id = buildId(identification, professionName);
        return profesionRepositoryMongo.findById(id)
                .map(profesionMapperMongo::fromAdapterToDomain)
                .orElse(null);
    }
}
