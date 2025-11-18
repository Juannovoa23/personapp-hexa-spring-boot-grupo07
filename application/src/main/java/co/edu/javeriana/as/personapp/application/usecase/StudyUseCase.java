package co.edu.javeriana.as.personapp.application.usecase;

import java.util.List;

import co.edu.javeriana.as.personapp.application.port.in.StudyInputPort;
import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Study;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StudyUseCase implements StudyInputPort {

    private StudyOutputPort studyPersistence;

    public StudyUseCase(StudyOutputPort studyPersistence) {
        this.studyPersistence = studyPersistence;
    }

    @Override
    public void setPersintence(StudyOutputPort studyPersistence) {
        this.studyPersistence = studyPersistence;
    }

    @Override
    public Study create(Study study) {
        log.debug("Into create on StudyUseCase");
        return studyPersistence.save(study);
    }

    @Override
    public Study edit(Integer idProf, Integer ccPer, Study study) throws NoExistException {
        log.debug("Into edit on StudyUseCase");

        Study old = studyPersistence.findById(idProf, ccPer);
        if (old == null) {
            throw new NoExistException(
                    "The study with PK (" + idProf + ", " + ccPer + ") does not exist");
        }

        // Mantener la PK (persona + profesión) tal como está en BD
        study.setPerson(old.getPerson());
        study.setProfession(old.getProfession());
        // Se actualizan solo graduationDate y universityName desde `study`

        return studyPersistence.save(study);
    }

    @Override
    public Boolean drop(Integer idProf, Integer ccPer) throws NoExistException {
        log.debug("Into drop on StudyUseCase");

        Study old = studyPersistence.findById(idProf, ccPer);
        if (old == null) {
            throw new NoExistException(
                    "The study with PK (" + idProf + ", " + ccPer + ") does not exist");
        }

        return studyPersistence.delete(idProf, ccPer);
    }

    @Override
    public List<Study> findAll() {
        log.debug("Into findAll on StudyUseCase");
        return studyPersistence.find();
    }

    @Override
    public Study findOne(Integer idProf, Integer ccPer) throws NoExistException {
        log.debug("Into findOne on StudyUseCase");

        Study study = studyPersistence.findById(idProf, ccPer);
        if (study == null) {
            throw new NoExistException(
                    "The study with PK (" + idProf + ", " + ccPer + ") does not exist");
        }

        return study;
    }

    @Override
    public Integer count() {
        log.debug("Into count on StudyUseCase");
        return studyPersistence.find().size();
    }
}
