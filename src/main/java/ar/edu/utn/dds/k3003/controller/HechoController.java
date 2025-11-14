package ar.edu.utn.dds.k3003.controller;
import ar.edu.utn.dds.k3003.facades.dtos.HechoDTO;
import ar.edu.utn.dds.k3003.model.ErrorResponse;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import ar.edu.utn.dds.k3003.app.Fachada;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HechoController {
	private final Fachada fachadaAgregador;
	private static final Logger logger = LoggerFactory.getLogger(Fachada.class);
	private final Counter hechosRequestBuscandorCounter;
	private final Counter ServerErrorCounter;

    @Autowired
    public HechoController(Fachada fachadaAgregador, MeterRegistry meterRegistry) {
        this.fachadaAgregador = fachadaAgregador;
        this.hechosRequestBuscandorCounter = meterRegistry.counter("hechos.buscador.requests");
        this.ServerErrorCounter = meterRegistry.counter("coleccion.server.errors");
    }
    @Timed(value = "hechos.buscador.get", description = "Time taken to return all hechos for search")
    @GetMapping(value= "/hechos",produces ="application/json")
    public ResponseEntity<?> getHechos(){
    	hechosRequestBuscandorCounter.increment();
    	try {
    		List<HechoDTO> hechos = fachadaAgregador.hechos();
            return ResponseEntity.ok(hechos);
        } catch (Exception e) {
        	ServerErrorCounter.increment();
        	logger.error(e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(new ErrorResponse("SERVER_ERROR", 
                         "Error al procesar la solicitud"));
        }
    }
}



