package co.edu.javeriana.as.personapp.mariadb.adapter;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mariadb.entity.EstudiosEntity;
import co.edu.javeriana.as.personapp.mariadb.entity.EstudiosEntityPK;
import co.edu.javeriana.as.personapp.mariadb.mapper.EstudiosMapperMaria;
import co.edu.javeriana.as.personapp.mariadb.repository.EstudiosRepositoryMaria;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter("studyOutputAdapterMaria")
@Transactional
public class StudyOutputAdapterMaria implements StudyOutputPort {

    @Autowired
    private EstudiosRepositoryMaria estudiosRepositoryMaria;

    @Autowired
    private EstudiosMapperMaria estudiosMapperMaria;

    @Override
    public Study save(Study study) {
        log.debug("Into save on StudyOutputAdapterMaria");
        EstudiosEntity entity = estudiosMapperMaria.fromDomainToAdapter(study);
        EstudiosEntity saved = estudiosRepositoryMaria.save(entity);
        return estudiosMapperMaria.fromAdapterToDomain(saved);
    }

    @Override
    public Boolean delete(Integer idProf, Integer ccPer) {
        log.debug("Into delete on StudyOutputAdapterMaria");
        EstudiosEntityPK pk = new EstudiosEntityPK(idProf, ccPer);
        if (!estudiosRepositoryMaria.existsById(pk)) {
            return Boolean.FALSE;
        }
        estudiosRepositoryMaria.deleteById(pk);
        return Boolean.TRUE;
    }

    @Override
    public List<Study> find() {
        log.debug("Into find on StudyOutputAdapterMaria");
        return estudiosRepositoryMaria.findAll().stream()
                .map(estudiosMapperMaria::fromAdapterToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Study findById(Integer idProf, Integer ccPer) {
        log.debug("Into findById on StudyOutputAdapterMaria");
        EstudiosEntityPK pk = new EstudiosEntityPK(idProf, ccPer);
        return estudiosRepositoryMaria.findById(pk)
                .map(estudiosMapperMaria::fromAdapterToDomain)
                .orElse(null);
    }

    

    
}
