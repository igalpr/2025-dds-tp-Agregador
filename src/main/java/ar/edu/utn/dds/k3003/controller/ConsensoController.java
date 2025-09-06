package ar.edu.utn.dds.k3003.controller;

import ar.edu.utn.dds.k3003.app.Fachada;
import ar.edu.utn.dds.k3003.facades.dtos.ConsensosEnum;
import ar.edu.utn.dds.k3003.model.ConsensoRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/consenso")
public class ConsensoController {

    private final Fachada fachada;

    @Autowired
    public ConsensoController(Fachada fachadaAgregador) {
        this.fachada = fachadaAgregador;
    }

    @PatchMapping
    public ResponseEntity<Void> actualizarEstrategiaConsenso(@RequestBody ConsensoRequest request) {
        
        fachada.setConsensoStrategy(request.getTipo(), request.getColeccionId());
        return ResponseEntity.ok().build();
    }
}
