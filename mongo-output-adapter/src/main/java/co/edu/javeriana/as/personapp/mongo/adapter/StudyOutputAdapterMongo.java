package co.edu.javeriana.as.personapp.mongo.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mongo.document.EstudiosDocument;
import co.edu.javeriana.as.personapp.mongo.mapper.EstudiosMapperMongo;
import co.edu.javeriana.as.personapp.mongo.repository.EstudiosRepositoryMongo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter("studyOutputAdapterMongo")
public class StudyOutputAdapterMongo implements StudyOutputPort {

    @Autowired
    private EstudiosRepositoryMongo estudiosRepositoryMongo;

    @Autowired
    private EstudiosMapperMongo estudiosMapperMongo;

    private String buildId(Integer identification, String professionName) {
        return identification + "-" + professionName;
    }

    @Override
    public Study save(Study study) {
        log.debug("Into save on StudyOutputAdapterMongo");

        EstudiosDocument doc = estudiosMapperMongo.fromDomainToAdapter(study);

        // Generar id a partir de person + profession
        if (study.getPerson() != null && study.getProfession() != null
                && study.getPerson().getIdentification() != null
                && study.getProfession().getName() != null) {

            String id = buildId(study.getPerson().getIdentification(),
                                study.getProfession().getName());
            doc.setId(id);
        }

        EstudiosDocument saved = estudiosRepositoryMongo.save(doc);
        return estudiosMapperMongo.fromAdapterToDomain(saved);
    }

    @Override
    public Boolean delete(Integer idProf, Integer ccPer) {
        log.debug("Into delete on StudyOutputAdapterMongo");

        // OJO: aquí debes decidir qué significa cada parámetro:
        // - según tu MariaDB, idProf = id_prof (profesión), ccPer = cc_per (persona)
        // Para Mongo hemos venido usando identificacion (persona) primero y nombre de profesión en el id.
        // Si mantienes esa lógica, puedes reinterpretar:
        //   idProf = identificación persona
        //   ccPer  = id de profesión, pero como en Mongo usamos nombre, lo ideal es que
        //   el que llame a este método use (identification, professionName) directamente.
        // Para no romper tu flujo actual, asumo:
        String id = buildId(idProf, ccPer.toString()); // AJUSTA si prefieres otro orden/uso.

        if (!estudiosRepositoryMongo.existsById(id)) {
            return Boolean.FALSE;
        }

        estudiosRepositoryMongo.deleteById(id);
        return Boolean.TRUE;
    }

    @Override
    public List<Study> find() {
        log.debug("Into find on StudyOutputAdapterMongo");
        return estudiosRepositoryMongo.findAll().stream()
                .map(estudiosMapperMongo::fromAdapterToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Study findById(Integer idProf, Integer ccPer) {
        log.debug("Into findById on StudyOutputAdapterMongo");

        String id = buildId(idProf, ccPer.toString()); // mismo comentario que en delete()

        return estudiosRepositoryMongo.findById(id)
                .map(estudiosMapperMongo::fromAdapterToDomain)
                .orElse(null);
    }
}
