package co.edu.javeriana.as.personapp.mongo.mapper;

import org.springframework.stereotype.Component;

import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.mongo.document.ProfesionDocument;

@Component
public class ProfesionMapperMongo {

    /**
     * Domain -> Mongo Document
     */
    public ProfesionDocument fromDomainToAdapter(Profession profession) {
        ProfesionDocument document = new ProfesionDocument();

        if (profession.getIdentification() != null && profession.getName() != null) {
            String id = buildId(profession.getIdentification(), profession.getName());
            document.setId(id);
        }

        document.setNom(profession.getName());
        document.setDes(profession.getDescription());

        // estudios se manejan por @DocumentReference (no se mapean aquí)
        return document;
    }

    /**
     * Mongo Document -> Domain
     */
    public Profession fromAdapterToDomain(ProfesionDocument document) {
        Profession profession = new Profession();

        // identification la reconstruimos desde el id "identification-name"
        if (document.getId() != null) {
            try {
                String[] parts = document.getId().split("-", 2);
                if (parts.length > 0) {
                    Integer identification = Integer.parseInt(parts[0]);
                    profession.setIdentification(identification);
                }
            } catch (NumberFormatException e) {
                // si el id no tiene el formato esperado, simplemente no seteamos identification
            }
        }

        profession.setName(document.getNom());
        profession.setDescription(document.getDes());
        // studies se podría mapear desde document.getEstudios() en otro mapper/servicio
        return profession;
    }

    private String buildId(Integer identification, String name) {
        return identification + "-" + name;
    }
}
