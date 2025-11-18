package co.edu.javeriana.as.personapp.mariadb.adapter;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.mariadb.entity.ProfesionEntity;
import co.edu.javeriana.as.personapp.mariadb.mapper.ProfesionMapperMaria;
import co.edu.javeriana.as.personapp.mariadb.repository.ProfesionRepositoryMaria;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter("professionOutputAdapterMaria")
@Transactional
public class ProfessionOutputAdapterMaria implements ProfessionOutputPort {

    @Autowired
    private ProfesionRepositoryMaria profesionRepositoryMaria;

    @Autowired
    private ProfesionMapperMaria profesionMapperMaria;

    @Override
    public Profession save(Profession profession) {
        log.debug("Into save on ProfessionOutputAdapterMaria");
        ProfesionEntity entity = profesionMapperMaria.fromDomainToAdapter(profession);
        ProfesionEntity saved = profesionRepositoryMaria.save(entity);
        return profesionMapperMaria.fromAdapterToDomain(saved);
    }

    @Override
    public Boolean delete(Integer identification, String professionName) {
        log.debug("Into delete on ProfessionOutputAdapterMaria");
        Integer pk = new Integer(identification); // ajusta constructor
        if (!profesionRepositoryMaria.existsById(pk)) {
            return Boolean.FALSE;
        }
        profesionRepositoryMaria.deleteById(pk);
        return Boolean.TRUE;
    }

    @Override
    public List<Profession> find() {
        log.debug("Into find on ProfessionOutputAdapterMaria");
        return profesionRepositoryMaria.findAll().stream()
                .map(profesionMapperMaria::fromAdapterToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Profession findById(Integer identification, String professionName) {
        log.debug("Into findById on ProfessionOutputAdapterMaria");
        Integer pk = new Integer(identification); // ajusta
        return profesionRepositoryMaria.findById(pk)
                .map(profesionMapperMaria::fromAdapterToDomain)
                .orElse(null);
    }
}
