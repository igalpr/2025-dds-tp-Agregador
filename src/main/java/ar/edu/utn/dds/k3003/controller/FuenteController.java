package ar.edu.utn.dds.k3003.controller;

import ar.edu.utn.dds.k3003.facades.dtos.FuenteDTO;
import io.micrometer.core.annotation.Timed;
import ar.edu.utn.dds.k3003.app.Fachada;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fuentes")
public class FuenteController {
	private final Fachada fachadaAgregador;
	
    @Autowired
    public FuenteController(Fachada fachadaAgregador) {
        this.fachadaAgregador = fachadaAgregador;
    }
    @Timed(value = "fuentes.get", description = "Time taken to return all fuentes")
    @GetMapping(produces = "application/json")
    public ResponseEntity<List<FuenteDTO>> getFuentes(){
    	return ResponseEntity.ok(fachadaAgregador.fuentes());
    }
    @PostMapping(produces = "application/json")
    public ResponseEntity<FuenteDTO> crearFuente(@RequestBody FuenteDTO fuente) {
        return ResponseEntity.ok(fachadaAgregador.agregar(fuente));
    }
	
}



