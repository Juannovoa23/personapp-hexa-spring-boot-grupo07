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

import co.edu.javeriana.as.personapp.adapter.PhoneInputAdapterRest;
import co.edu.javeriana.as.personapp.model.request.PhoneRequest;
import co.edu.javeriana.as.personapp.model.response.PhoneResponse;
import co.edu.javeriana.as.personapp.model.response.Response;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/phone")
public class PhoneControllerV1 {

    @Autowired
    private PhoneInputAdapterRest phoneInputAdapterRest;

    @ResponseBody
    @GetMapping(path = "/{database}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PhoneResponse> phones(@PathVariable String database) {
        log.info("Into phones REST API");
        return phoneInputAdapterRest.historial(database.toUpperCase());
    }

    @ResponseBody
    @PostMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public PhoneResponse crearPhone(@RequestBody PhoneRequest request) {
        log.info("Into crearPhone REST API");
        return phoneInputAdapterRest.crearPhone(request);
    }

    @GetMapping(path = "/{database}/{number}", produces = MediaType.APPLICATION_JSON_VALUE)
    public PhoneResponse obtenerPhone(@PathVariable String database, @PathVariable String number) {
        log.info("Into obtenerPhone REST API");
        return phoneInputAdapterRest.buscarPhone(database.toUpperCase(), number);
    }

    @PutMapping(path = "/{database}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public PhoneResponse editarPhone(@PathVariable String database, @RequestBody PhoneRequest request) {
        log.info("Into editarPhone REST API");
        return phoneInputAdapterRest.editarPhone(database.toUpperCase(), request);
    }

    @DeleteMapping(path = "/{database}/{number}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response eliminarPhone(@PathVariable String database, @PathVariable String number) {
        log.info("Into eliminarPhone REST API");
        return phoneInputAdapterRest.eliminarPhone(database.toUpperCase(), number);
    }
}
