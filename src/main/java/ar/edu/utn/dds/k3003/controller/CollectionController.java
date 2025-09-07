package ar.edu.utn.dds.k3003.controller;

import ar.edu.utn.dds.k3003.facades.dtos.HechoDTO;
import ar.edu.utn.dds.k3003.model.ErrorResponse;
import ar.edu.utn.dds.k3003.app.Fachada;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CollectionController {
	private final Fachada fachadaAgregador;
	
    @Autowired
    public CollectionController(Fachada fachadaAgregador) {
        this.fachadaAgregador = fachadaAgregador;
    }
    @GetMapping("/{nombre}/hechos")
    public ResponseEntity<?> getHechos(@PathVariable String nombre){
    	try {
            List<HechoDTO> hechos = fachadaAgregador.hechos(nombre);
            return ResponseEntity.ok(hechos);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("COLLECTION_NOT_FOUND", 
                         "No se encontraron fuentes disponibles"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ErrorResponse("SERVER_ERROR", 
                         "Error al procesar la solicitud"));
        }    
    }	
}



