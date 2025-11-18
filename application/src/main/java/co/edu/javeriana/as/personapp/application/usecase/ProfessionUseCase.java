package co.edu.javeriana.as.personapp.application.usecase;

import java.util.List;

import co.edu.javeriana.as.personapp.application.port.in.ProfessionInputPort;
import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.UseCase;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Profession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProfessionUseCase implements ProfessionInputPort {

    private ProfessionOutputPort professionPersistence;

    public ProfessionUseCase(ProfessionOutputPort professionPersistence) {
        this.professionPersistence = professionPersistence;
    }



    @Override
    public void setPersintence(ProfessionOutputPort professionPersistence) {
        this.professionPersistence = professionPersistence;
    }

    @Override
    public Profession create(Profession profession) {
        log.debug("Into create on ProfessionUseCase");
        return professionPersistence.save(profession);
    }

    @Override
    public Profession edit(Integer identification, String professionName, Profession profession)
            throws NoExistException {
        log.debug("Into edit on ProfessionUseCase");
        Profession old = professionPersistence.findById(identification, professionName);
        if (old == null) {
            throw new NoExistException(
                    "The profession with id (" + identification + ", " + professionName + ") does not exist");
        }

        profession.setIdentification(old.getIdentification());
        profession.setName(old.getName());
        return professionPersistence.save(profession);
    }

    @Override
    public Boolean drop(Integer identification, String professionName) throws NoExistException {
        log.debug("Into drop on ProfessionUseCase");
        Profession old = professionPersistence.findById(identification, professionName);
        if (old == null) {
            throw new NoExistException(
                    "The profession with id (" + identification + ", " + professionName + ") does not exist");
        }
        return professionPersistence.delete(identification, professionName);
    }

    @Override
    public List<Profession> findAll() {
        log.debug("Into findAll on ProfessionUseCase");
        return professionPersistence.find();
    }

    @Override
    public Profession findOne(Integer identification, String professionName) throws NoExistException {
        log.debug("Into findOne on ProfessionUseCase");
        Profession profession = professionPersistence.findById(identification, professionName);
        if (profession == null) {
            throw new NoExistException(
                    "The profession with id (" + identification + ", " + professionName + ") does not exist");
        }
        return profession;
    }

    @Override
    public Integer count() {
        log.debug("Into count on ProfessionUseCase");
        return professionPersistence.find().size();
    }
}
