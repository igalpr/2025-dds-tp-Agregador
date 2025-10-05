package ar.edu.utn.dds.k3003.controller;

import ar.edu.utn.dds.k3003.facades.dtos.HechoDTO;
import ar.edu.utn.dds.k3003.model.ErrorResponse;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import ar.edu.utn.dds.k3003.app.Fachada;
import java.util.List;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/coleccion")
public class CollectionController {
	private final Fachada fachadaAgregador;
	private static final Logger logger = LoggerFactory.getLogger(Fachada.class);
	private final Counter hechosRequestCounter;
	private final Counter ColeccionNotFoundCounter;
	private final Counter ServerErrorCounter;

    @Autowired
    public CollectionController(Fachada fachadaAgregador, MeterRegistry meterRegistry) {
        this.fachadaAgregador = fachadaAgregador;
        this.hechosRequestCounter = meterRegistry.counter("coleccion.hechos.requests");
		this.ColeccionNotFoundCounter = meterRegistry.counter("coleccion.notfound.errors");
		this.ServerErrorCounter = meterRegistry.counter("coleccion.server.errors");
    }
    @Timed(value = "hechos.get", description = "Time taken to return all hechos by collection")
    @GetMapping(value = "/{nombre}/hechos",produces = "application/json")
    public ResponseEntity<?> getHechos(@PathVariable String nombre){
    	hechosRequestCounter.increment();
    	try {
            List<HechoDTO> hechos = fachadaAgregador.hechos(nombre);
            return ResponseEntity.ok(hechos);
        } catch (NoSuchElementException e) {
        	ColeccionNotFoundCounter.increment();
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("COLLECTION_NOT_FOUND", 
                         "No se encontro la coleccion en las fuentes disponibles"));
        } catch (Exception e) {
        	ServerErrorCounter.increment();
        	logger.error(e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(new ErrorResponse("SERVER_ERROR", 
                         "Error al procesar la solicitud"));
        }    
    }	
}



