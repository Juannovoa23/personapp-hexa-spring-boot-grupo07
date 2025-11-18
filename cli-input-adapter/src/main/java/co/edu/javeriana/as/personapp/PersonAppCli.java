package co.edu.javeriana.as.personapp;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import co.edu.javeriana.as.personapp.application.port.out.PhoneOutputPort;
import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.PhoneUseCase;
import co.edu.javeriana.as.personapp.application.usecase.ProfessionUseCase;
import co.edu.javeriana.as.personapp.application.usecase.StudyUseCase;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PersonAppCli implements CommandLineRunner {

    @Autowired
    @Qualifier("phoneOutputAdapterMaria")
    private PhoneOutputPort phoneOutputPortMaria;

    @Autowired
    @Qualifier("phoneOutputAdapterMongo")
    private PhoneOutputPort phoneOutputPortMongo;

    @Autowired
    @Qualifier("professionOutputAdapterMaria")
    private ProfessionOutputPort professionOutputPortMaria;

    @Autowired
    @Qualifier("professionOutputAdapterMongo")
    private ProfessionOutputPort professionOutputPortMongo;

    @Autowired
    @Qualifier("studyOutputAdapterMaria")
    private StudyOutputPort studyOutputPortMaria;

    @Autowired
    @Qualifier("studyOutputAdapterMongo")
    private StudyOutputPort studyOutputPortMongo;

    private final Scanner scanner = new Scanner(System.in);

    @Override
    public void run(String... args) throws Exception {
        log.info("=== Iniciando CLI PersonApp ===");

        boolean running = true;
        while (running) {
            System.out.println("\n=== Menú Principal CLI ===");
            System.out.println("1. Gestión de teléfonos (Phone)");
            System.out.println("2. Gestión de profesiones (Profession)");
            System.out.println("3. Gestión de estudios (Study)");
            System.out.println("0. Salir");
            System.out.print("Opción: ");
            String option = scanner.nextLine();

            switch (option) {
                case "1":
                    menuPhone();
                    break;
                case "2":
                    menuProfession();
                    break;
                case "3":
                    menuStudy();
                    break;
                case "0":
                    running = false;
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }

        log.info("CLI PersonApp finalizado.");
    }

    // =======================
    //   MENÚ PHONE
    // =======================

    private void menuPhone() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Menú Phone ---");
            System.out.println("1. Listar teléfonos");
            System.out.println("2. Crear teléfono");
            System.out.println("3. Buscar teléfono por número");
            System.out.println("4. Editar teléfono");
            System.out.println("5. Eliminar teléfono");
            System.out.println("0. Volver");
            System.out.print("Opción: ");
            String option = scanner.nextLine();

            switch (option) {
                case "1":
                    phoneList();
                    break;
                case "2":
                    phoneCreate();
                    break;
                case "3":
                    phoneFind();
                    break;
                case "4":
                    phoneEdit();
                    break;
                case "5":
                    phoneDelete();
                    break;
                case "0":
                    back = true;
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }
    }

    private PhoneUseCase getPhoneUseCase() {
        System.out.print("Base de datos (MARIA/MONGO): ");
        String db = scanner.nextLine().trim().toUpperCase();
        if (db.equals(DatabaseOption.MARIA.toString())) {
            return new PhoneUseCase(phoneOutputPortMaria);
        } else {
            return new PhoneUseCase(phoneOutputPortMongo);
        }
    }

    private void phoneList() {
        PhoneUseCase useCase = getPhoneUseCase();
        List<Phone> phones = useCase.findAll();
        System.out.println("\nTeléfonos:");
        phones.forEach(System.out::println);
    }

    private void phoneCreate() {
        PhoneUseCase useCase = getPhoneUseCase();

        System.out.print("Número: ");
        String number = scanner.nextLine();
        System.out.print("Compañía: ");
        String company = scanner.nextLine();
        System.out.print("Identificación del dueño: ");
        Integer idOwner = Integer.parseInt(scanner.nextLine());

        Person owner = new Person(idOwner);
        Phone phone = new Phone(number, company, owner);

        Phone created = useCase.create(phone);
        System.out.println("Teléfono creado: " + created);
    }

    private void phoneFind() {
        PhoneUseCase useCase = getPhoneUseCase();

        System.out.print("Número a buscar: ");
        String number = scanner.nextLine();

        try {
            Phone phone = useCase.findOne(number);
            System.out.println("Encontrado: " + phone);
        } catch (Exception e) {
            System.out.println("No se encontró el teléfono: " + e.getMessage());
        }
    }

    private void phoneEdit() {
        PhoneUseCase useCase = getPhoneUseCase();

        System.out.print("Número del teléfono a editar: ");
        String number = scanner.nextLine();

        System.out.println("Nuevos datos:");
        System.out.print("Compañía: ");
        String company = scanner.nextLine();
        System.out.print("Identificación del dueño: ");
        Integer idOwner = Integer.parseInt(scanner.nextLine());

        Person owner = new Person(idOwner);
        Phone phone = new Phone(number, company, owner);

        try {
            Phone edited = useCase.edit(number, phone);
            System.out.println("Teléfono editado: " + edited);
        } catch (Exception e) {
            System.out.println("Error al editar teléfono: " + e.getMessage());
        }
    }

    private void phoneDelete() {
        PhoneUseCase useCase = getPhoneUseCase();

        System.out.print("Número del teléfono a eliminar: ");
        String number = scanner.nextLine();

        try {
            Boolean deleted = useCase.drop(number);
            System.out.println(deleted ? "Teléfono eliminado." : "No se pudo eliminar.");
        } catch (Exception e) {
            System.out.println("Error al eliminar teléfono: " + e.getMessage());
        }
    }

    // =======================
    //   MENÚ PROFESSION
    // =======================

    private void menuProfession() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Menú Profession ---");
            System.out.println("1. Listar profesiones");
            System.out.println("2. Crear profesión");
            System.out.println("3. Buscar profesión");
            System.out.println("4. Editar profesión");
            System.out.println("5. Eliminar profesión");
            System.out.println("0. Volver");
            System.out.print("Opción: ");
            String option = scanner.nextLine();

            switch (option) {
                case "1":
                    professionList();
                    break;
                case "2":
                    professionCreate();
                    break;
                case "3":
                    professionFind();
                    break;
                case "4":
                    professionEdit();
                    break;
                case "5":
                    professionDelete();
                    break;
                case "0":
                    back = true;
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }
    }

    private ProfessionUseCase getProfessionUseCase() {
        System.out.print("Base de datos (MARIA/MONGO): ");
        String db = scanner.nextLine().trim().toUpperCase();
        if (db.equals(DatabaseOption.MARIA.toString())) {
            return new ProfessionUseCase(professionOutputPortMaria);
        } else {
            return new ProfessionUseCase(professionOutputPortMongo);
        }
    }

    private void professionList() {
        ProfessionUseCase useCase = getProfessionUseCase();
        List<Profession> professions = useCase.findAll();
        System.out.println("\nProfesiones:");
        professions.forEach(System.out::println);
    }

    private void professionCreate() {
        ProfessionUseCase useCase = getProfessionUseCase();

        System.out.print("Identificación asociada (identification en Profession): ");
        Integer identification = Integer.parseInt(scanner.nextLine());
        System.out.print("Nombre de la profesión: ");
        String name = scanner.nextLine();
        System.out.print("Descripción: ");
        String description = scanner.nextLine();

        Profession profession = new Profession(identification, name);
        profession.setDescription(description);

        Profession created = useCase.create(profession);
        System.out.println("Profesión creada: " + created);
    }

    private void professionFind() {
        ProfessionUseCase useCase = getProfessionUseCase();

        System.out.print("Identificación (identification en Profession): ");
        Integer identification = Integer.parseInt(scanner.nextLine());
        System.out.print("Nombre de la profesión: ");
        String name = scanner.nextLine();

        try {
            Profession p = useCase.findOne(identification, name);
            System.out.println("Encontrada: " + p);
        } catch (Exception e) {
            System.out.println("No se encontró la profesión: " + e.getMessage());
        }
    }

    private void professionEdit() {
        ProfessionUseCase useCase = getProfessionUseCase();

        System.out.print("Identificación (identification en Profession): ");
        Integer identification = Integer.parseInt(scanner.nextLine());
        System.out.print("Nombre de la profesión: ");
        String name = scanner.nextLine();

        System.out.println("Nuevos datos:");
        System.out.print("Nueva descripción: ");
        String description = scanner.nextLine();

        Profession profession = new Profession(identification, name);
        profession.setDescription(description);

        try {
            Profession edited = useCase.edit(identification, name, profession);
            System.out.println("Profesión editada: " + edited);
        } catch (Exception e) {
            System.out.println("Error al editar profesión: " + e.getMessage());
        }
    }

    private void professionDelete() {
        ProfessionUseCase useCase = getProfessionUseCase();

        System.out.print("Identificación (identification en Profession): ");
        Integer identification = Integer.parseInt(scanner.nextLine());
        System.out.print("Nombre de la profesión: ");
        String name = scanner.nextLine();

        try {
            Boolean deleted = useCase.drop(identification, name);
            System.out.println(deleted ? "Profesión eliminada." : "No se pudo eliminar.");
        } catch (Exception e) {
            System.out.println("Error al eliminar profesión: " + e.getMessage());
        }
    }

    // =======================
    //   MENÚ STUDY
    // =======================

    private void menuStudy() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Menú Study ---");
            System.out.println("1. Listar estudios");
            System.out.println("2. Crear estudio");
            System.out.println("3. Buscar estudio");
            System.out.println("4. Editar estudio");
            System.out.println("5. Eliminar estudio");
            System.out.println("0. Volver");
            System.out.print("Opción: ");
            String option = scanner.nextLine();

            switch (option) {
                case "1":
                    studyList();
                    break;
                case "2":
                    studyCreate();
                    break;
                case "3":
                    studyFind();
                    break;
                case "4":
                    studyEdit();
                    break;
                case "5":
                    studyDelete();
                    break;
                case "0":
                    back = true;
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }
    }

    private StudyUseCase getStudyUseCase() {
        System.out.print("Base de datos (MARIA/MONGO): ");
        String db = scanner.nextLine().trim().toUpperCase();
        if (db.equals(DatabaseOption.MARIA.toString())) {
            return new StudyUseCase(studyOutputPortMaria);
        } else {
            return new StudyUseCase(studyOutputPortMongo);
        }
    }

    private void studyList() {
        StudyUseCase useCase = getStudyUseCase();
        List<Study> studies = useCase.findAll();
        System.out.println("\nEstudios:");
        studies.forEach(System.out::println);
    }

    private void studyCreate() {
        StudyUseCase useCase = getStudyUseCase();

        System.out.print("ID de la profesión (idProf / identification en Profession): ");
        Integer idProf = Integer.parseInt(scanner.nextLine());
        System.out.print("CC de la persona (ccPer / identification en Person): ");
        Integer ccPer = Integer.parseInt(scanner.nextLine());

        System.out.print("Fecha de graduación (yyyy-MM-dd): ");
        String dateStr = scanner.nextLine();
        LocalDate graduationDate = dateStr.isEmpty() ? null : LocalDate.parse(dateStr);

        System.out.print("Nombre de la universidad: ");
        String universityName = scanner.nextLine();

        Person person = new Person(ccPer);
        Profession profession = new Profession(idProf, null);

        Study study = new Study(person, profession);
        study.setGraduationDate(graduationDate);
        study.setUniversityName(universityName);

        Study created = useCase.create(study);
        System.out.println("Estudio creado: " + created);
    }

    private void studyFind() {
        StudyUseCase useCase = getStudyUseCase();

        System.out.print("ID de la profesión (idProf): ");
        Integer idProf = Integer.parseInt(scanner.nextLine());
        System.out.print("CC de la persona (ccPer): ");
        Integer ccPer = Integer.parseInt(scanner.nextLine());

        try {
            Study study = useCase.findOne(idProf, ccPer);
            System.out.println("Estudio encontrado: " + study);
        } catch (Exception e) {
            System.out.println("No se encontró el estudio: " + e.getMessage());
        }
    }

    private void studyEdit() {
        StudyUseCase useCase = getStudyUseCase();

        System.out.print("ID de la profesión (idProf): ");
        Integer idProf = Integer.parseInt(scanner.nextLine());
        System.out.print("CC de la persona (ccPer): ");
        Integer ccPer = Integer.parseInt(scanner.nextLine());

        System.out.println("Nuevos datos del estudio:");
        System.out.print("Fecha de graduación (yyyy-MM-dd): ");
        String dateStr = scanner.nextLine();
        LocalDate graduationDate = dateStr.isEmpty() ? null : LocalDate.parse(dateStr);

        System.out.print("Nombre de la universidad: ");
        String universityName = scanner.nextLine();

        Person person = new Person(ccPer);
        Profession profession = new Profession(idProf, null);

        Study study = new Study(person, profession);
        study.setGraduationDate(graduationDate);
        study.setUniversityName(universityName);

        try {
            Study edited = useCase.edit(idProf, ccPer, study);
            System.out.println("Estudio editado: " + edited);
        } catch (Exception e) {
            System.out.println("Error al editar estudio: " + e.getMessage());
        }
    }

    private void studyDelete() {
        StudyUseCase useCase = getStudyUseCase();

        System.out.print("ID de la profesión (idProf): ");
        Integer idProf = Integer.parseInt(scanner.nextLine());
        System.out.print("CC de la persona (ccPer): ");
        Integer ccPer = Integer.parseInt(scanner.nextLine());

        try {
            Boolean deleted = useCase.drop(idProf, ccPer);
            System.out.println(deleted ? "Estudio eliminado." : "No se pudo eliminar.");
        } catch (Exception e) {
            System.out.println("Error al eliminar estudio: " + e.getMessage());
        }
    }
}
