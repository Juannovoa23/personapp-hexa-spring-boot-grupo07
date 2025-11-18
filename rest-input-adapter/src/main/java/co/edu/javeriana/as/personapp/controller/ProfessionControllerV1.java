package co.edu.javeriana.as.personapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import co.edu.javeriana.as.personapp.adapter.ProfessionInputAdapterRest;
import co.edu.javeriana.as.personapp.model.request.ProfessionRequest;
import co.edu.javeriana.as.personapp.model.response.ProfessionResponse;
import co.edu.javeriana.as.personapp.model.response.Response;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/profession")
public class ProfessionControllerV1 {

    @Autowired
    private ProfessionInputAdapterRest professionInputAdapterRest;

    /**
     * GET /api/v1/profession/{database}
     * Lista todas las profesiones en la BD indicada (MARIA o MONGO)
     */
    @ResponseBody
    @GetMapping(path = "/{database}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ProfessionResponse> professions(@PathVariable String database) {
        log.info("Into professions REST API");
        return professionInputAdapterRest.historial(database.toUpperCase());
    }

    /**
     * POST /api/v1/profession
     * Crea una profesión en la BD indicada en el body (campo database)
     */
    @ResponseBody
    @PostMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE,
                 consumes = MediaType.APPLICATION_JSON_VALUE)
    public ProfessionResponse crearProfession(@RequestBody ProfessionRequest request) {
        log.info("Into crearProfession REST API");
        return professionInputAdapterRest.crearProfession(request);
    }

    /**
     * GET /api/v1/profession/{database}/{identification}/{name}
     * Busca una profesión por identificación de persona y nombre
     */
    @ResponseBody
    @GetMapping(path = "/{database}/{identification}/{name}",
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ProfessionResponse obtenerProfession(@PathVariable String database,
                                                @PathVariable Integer identification,
                                                @PathVariable String name) {
        log.info("Into obtenerProfession REST API");
        return professionInputAdapterRest.buscarProfession(database.toUpperCase(),
                                                           identification, name);
    }

    /**
     * PUT /api/v1/profession/{database}
     * Edita una profesión existente. La PK viene en el body:
     *  - identification
     *  - name
     */
    @ResponseBody
    @PutMapping(path = "/{database}", produces = MediaType.APPLICATION_JSON_VALUE,
                consumes = MediaType.APPLICATION_JSON_VALUE)
    public ProfessionResponse editarProfession(@PathVariable String database,
                                               @RequestBody ProfessionRequest request) {
        log.info("Into editarProfession REST API");
        return professionInputAdapterRest.editarProfession(database.toUpperCase(), request);
    }

    /**
     * DELETE /api/v1/profession/{database}/{identification}/{name}
     * Elimina una profesión por identificación y nombre
     */
    @ResponseBody
    @DeleteMapping(path = "/{database}/{identification}/{name}",
                   produces = MediaType.APPLICATION_JSON_VALUE)
    public Response eliminarProfession(@PathVariable String database,
                                       @PathVariable Integer identification,
                                       @PathVariable String name) {
        log.info("Into eliminarProfession REST API");
        return professionInputAdapterRest.eliminarProfession(database.toUpperCase(),
                                                             identification, name);
    }
}
