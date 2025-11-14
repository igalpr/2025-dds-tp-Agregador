package ar.edu.utn.dds.k3003.controller;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
	private final Counter hechosRequestBuscandorCounter;
	private final Counter ColeccionNotFoundCounter;
	private final Counter ServerErrorCounter;

    @Autowired
    public CollectionController(Fachada fachadaAgregador, MeterRegistry meterRegistry) {
        this.fachadaAgregador = fachadaAgregador;
        this.hechosRequestBuscandorCounter = meterRegistry.counter("coleccion.hechos.buscador.requests");
        this.hechosRequestCounter = meterRegistry.counter("coleccion.hechos.requests");
		this.ColeccionNotFoundCounter = meterRegistry.counter("coleccion.notfound.errors");
		this.ServerErrorCounter = meterRegistry.counter("coleccion.server.errors");
    }
    @Timed(value = "hechos.get", description = "Time taken to return all hechos by collection")
    @GetMapping(value = "/{nombre}/hechos",produces = "application/json")
    public ResponseEntity<?> getHechosByColection(@PathVariable String nombre, @PageableDefault(page = 0, size = 20) Pageable pageable){
    	hechosRequestCounter.increment();
    	try {
            List<HechoDTO> hechos = fachadaAgregador.hechos(nombre);
            int start = (int) pageable.getOffset();
            int end = Math.min(start + pageable.getPageSize(), hechos.size());
            List<HechoDTO> subList = hechos.subList(start, end);
            Page<HechoDTO> hechosPage = new PageImpl<>(subList, pageable, hechos.size());
            List<HechoDTO> content = hechosPage.getContent();
            return ResponseEntity.ok(content);

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
    @GetMapping(value= "/hechos",produces ="application/json")
    public ResponseEntity<?> getHechos(){
    	hechosRequestBuscandorCounter.increment();
    	try {
    		List<HechoDTO> hechos = fachadaAgregador.hechos();
            return ResponseEntity.ok(hechos);
    	}catch (NoSuchElementException e) {
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



