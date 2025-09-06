package ar.edu.utn.dds.k3003.app;

import ar.edu.utn.dds.k3003.client.*;
import ar.edu.utn.dds.k3003.model.*;
import ar.edu.utn.dds.k3003.repository.FuenteRepository;
import ar.edu.utn.dds.k3003.repository.InMemoryFuenteRepo;
import ar.edu.utn.dds.k3003.facades.FachadaAgregador;
import ar.edu.utn.dds.k3003.facades.FachadaFuente;
import ar.edu.utn.dds.k3003.facades.dtos.*;
import ar.edu.utn.dds.k3003.mapper.*;
import java.security.InvalidParameterException;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class Fachada implements FachadaAgregador {
	private Agregador agregador = new Agregador();
	private FuenteRepository fuenteRepository;
	private static final Logger logger = LoggerFactory.getLogger(Fachada.class);
	public Fachada() {
		
		this.fuenteRepository = new InMemoryFuenteRepo();
    }

	@Autowired
	  public Fachada(FuenteRepository fuenteRepository) {
	     logger.info("Initializing Fachada with FuenteRepository: {}", fuenteRepository.getClass().getSimpleName());
	    this.fuenteRepository = fuenteRepository;
	  }

    @Override
    public FuenteDTO agregar(FuenteDTO fuente) {
        if (fuente == null) {
            throw new InvalidParameterException("No se puede agregar una fuente nula");
        }
        FuenteDTO nuevaFuenteDto = new FuenteDTO(String.valueOf(fuenteRepository.findAll().size()+1), fuente.nombre(), fuente.endpoint());
        Fuente nuevaFuente = new Fuente(Integer.valueOf(nuevaFuenteDto.id()), fuente.nombre(), fuente.endpoint());
        fuenteRepository.save(nuevaFuente);
        agregador.agregarFuente(nuevaFuente);
        return nuevaFuenteDto;
    }

    @Override
    public List<FuenteDTO> fuentes() {
        return Collections.unmodifiableList(
        		fuenteRepository.findAll().stream()
                        .map(FuenteMapper::toDTO)
                        .collect(Collectors.toList())
        );    
    }

    @Override
    public FuenteDTO buscarFuenteXId(String fuenteId) throws NoSuchElementException {
        return buscarFuentePorIdEnLista(fuenteId)
                .orElseThrow(() -> new NoSuchElementException("Fuente no encontrada por id " + fuenteId));
    }

    private Optional<FuenteDTO> buscarFuentePorIdEnLista(String fuenteId) {
        return fuenteRepository.findById(fuenteId).map(f -> new FuenteDTO(String.valueOf(f.getId()), f.getNombre(), f.getEndpoint()));
    }

    @Override
    public List<HechoDTO> hechos(String coleccionId) throws NoSuchElementException {
        List<Fuente> fuentes = fuenteRepository.findAll();
        
        List<Hecho> hechosFiltrados = agregador.hechos(coleccionId);

        return mappearHechoADTO(hechosFiltrados);
    }
    public List<HechoDTO> mappearHechoADTO(List<Hecho> hechos) {
        return hechos.stream()
                .map(HechoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void addFachadaFuentes(String fuenteId, FachadaFuente fuente) {
        agregador.addFachadaFuentes(fuenteId, fuente);
    }

    @Override
    public void setConsensoStrategy(ConsensosEnum tipoConsenso, String coleccionId) throws InvalidParameterException {
        agregador.setConsensoStrategy(tipoConsenso, coleccionId);
    }
}
