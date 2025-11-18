package co.edu.javeriana.as.personapp.application.usecase;

import java.util.List;

import co.edu.javeriana.as.personapp.application.port.in.PhoneInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PhoneOutputPort;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.domain.Phone;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PhoneUseCase implements PhoneInputPort {

    private PhoneOutputPort phonePersistence;

    public PhoneUseCase(PhoneOutputPort phonePersistence) {
        this.phonePersistence = phonePersistence;
    }

    @Override
    public void setPersintence(PhoneOutputPort phonePersistence) {
        this.phonePersistence = phonePersistence;
    }

    @Override
    public Phone create(Phone phone) {
        log.debug("Into create on PhoneUseCase");
        return phonePersistence.save(phone);
    }

    @Override
    public Phone edit(String number, Phone phone) throws NoExistException {
        log.debug("Into edit on PhoneUseCase");
        Phone old = phonePersistence.findByNumber(number);
        if (old == null) {
            throw new NoExistException(
                    "The phone with number " + number + " does not exist into db, cannot be edited");
        }
        phone.setNumber(number);
        return phonePersistence.save(phone);
    }

    @Override
    public Boolean drop(String number) throws NoExistException {
        log.debug("Into drop on PhoneUseCase");
        Phone old = phonePersistence.findByNumber(number);
        if (old == null) {
            throw new NoExistException(
                    "The phone with number " + number + " does not exist into db, cannot be deleted");
        }
        return phonePersistence.delete(number);
    }

    @Override
    public List<Phone> findAll() {
        log.debug("Into findAll on PhoneUseCase");
        return phonePersistence.find();
    }

    @Override
    public Phone findOne(String number) throws NoExistException {
        log.debug("Into findOne on PhoneUseCase");
        Phone phone = phonePersistence.findByNumber(number);
        if (phone == null) {
            throw new NoExistException(
                    "The phone with number " + number + " does not exist into db, cannot be retrieved");
        }
        return phone;
    }

    @Override
    public Integer count() {
        log.debug("Into count on PhoneUseCase");
        return phonePersistence.find().size();
    }
}
