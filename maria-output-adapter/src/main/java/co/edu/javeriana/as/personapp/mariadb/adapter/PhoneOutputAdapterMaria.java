package co.edu.javeriana.as.personapp.mariadb.adapter;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import co.edu.javeriana.as.personapp.application.port.out.PhoneOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.mariadb.entity.TelefonoEntity;
import co.edu.javeriana.as.personapp.mariadb.mapper.TelefonoMapperMaria;
import co.edu.javeriana.as.personapp.mariadb.repository.TelefonoRepositoryMaria;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
@Transactional
public class PhoneOutputAdapterMaria implements PhoneOutputPort {

    @Autowired
    private TelefonoRepositoryMaria telefonoRepositoryMaria;

    @Autowired
    private TelefonoMapperMaria telefonoMapperMaria;

    @Override
    public Phone save(Phone phone) {
        log.debug("Into save on PhoneOutputAdapterMaria");
        TelefonoEntity entity = telefonoMapperMaria.fromDomainToAdapter(phone);
        TelefonoEntity saved = telefonoRepositoryMaria.save(entity);
        return telefonoMapperMaria.fromAdapterToDomain(saved);
    }

    @Override
    public Boolean delete(String number) {
        log.debug("Into delete on PhoneOutputAdapterMaria");
        if (!telefonoRepositoryMaria.existsById(number)) {
            return Boolean.FALSE;
        }
        telefonoRepositoryMaria.deleteById(number);
        return Boolean.TRUE;
    }

    @Override
    public List<Phone> find() {
        log.debug("Into find on PhoneOutputAdapterMaria");
        return telefonoRepositoryMaria.findAll().stream()
                .map(telefonoMapperMaria::fromAdapterToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Phone findByNumber(String number) {
        log.debug("Into findByNumber on PhoneOutputAdapterMaria");
        return telefonoRepositoryMaria.findById(number)
                .map(telefonoMapperMaria::fromAdapterToDomain)
                .orElse(null);
    }

   
}
