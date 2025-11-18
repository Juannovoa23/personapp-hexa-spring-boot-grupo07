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

import co.edu.javeriana.as.personapp.adapter.StudyInputAdapterRest;
import co.edu.javeriana.as.personapp.model.request.StudyRequest;
import co.edu.javeriana.as.personapp.model.response.Response;
import co.edu.javeriana.as.personapp.model.response.StudyResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/study")
public class StudyControllerV1 {

    @Autowired
    private StudyInputAdapterRest studyInputAdapterRest;

    @ResponseBody
    @GetMapping(path = "/{database}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<StudyResponse> studies(@PathVariable String database) {
        log.info("Into studies REST API");
        return studyInputAdapterRest.historial(database.toUpperCase());
    }

    @ResponseBody
    @PostMapping(path = "",
                 produces = MediaType.APPLICATION_JSON_VALUE,
                 consumes = MediaType.APPLICATION_JSON_VALUE)
    public StudyResponse crearStudy(@RequestBody StudyRequest request) {
        log.info("Into crearStudy REST API");
        return studyInputAdapterRest.crearStudy(request);
    }

    @ResponseBody
    @GetMapping(path = "/{database}/{idProf}/{ccPer}",
                produces = MediaType.APPLICATION_JSON_VALUE)
    public StudyResponse obtenerStudy(@PathVariable String database,
                                      @PathVariable Integer idProf,
                                      @PathVariable Integer ccPer) {
        log.info("Into obtenerStudy REST API");
        return studyInputAdapterRest.buscarStudy(database.toUpperCase(), idProf, ccPer);
    }

    @ResponseBody
    @PutMapping(path = "/{database}",
                produces = MediaType.APPLICATION_JSON_VALUE,
                consumes = MediaType.APPLICATION_JSON_VALUE)
    public StudyResponse editarStudy(@PathVariable String database,
                                     @RequestBody StudyRequest request) {
        log.info("Into editarStudy REST API");
        return studyInputAdapterRest.editarStudy(database.toUpperCase(), request);
    }

    @ResponseBody
    @DeleteMapping(path = "/{database}/{idProf}/{ccPer}",
                   produces = MediaType.APPLICATION_JSON_VALUE)
    public Response eliminarStudy(@PathVariable String database,
                                  @PathVariable Integer idProf,
                                  @PathVariable Integer ccPer) {
        log.info("Into eliminarStudy REST API");
        return studyInputAdapterRest.eliminarStudy(database.toUpperCase(), idProf, ccPer);
    }
}
