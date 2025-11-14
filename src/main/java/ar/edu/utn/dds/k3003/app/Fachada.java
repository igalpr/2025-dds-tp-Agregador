package ar.edu.utn.dds.k3003.app;

import ar.edu.utn.dds.k3003.client.*;
import ar.edu.utn.dds.k3003.model.*;
import ar.edu.utn.dds.k3003.repository.FuenteRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import ar.edu.utn.dds.k3003.model.Fachada.*;
import ar.edu.utn.dds.k3003.facades.dtos.FuenteDTO;
import ar.edu.utn.dds.k3003.facades.dtos.HechoDTO;
import ar.edu.utn.dds.k3003.mapper.*;
import java.security.InvalidParameterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import io.micrometer.core.instrument.Timer;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

@Slf4j
@Service
@Transactional
public class Fachada implements FachadaAgregador {
	private Agregador agregador = new Agregador();
	private FuenteRepository fuenteRepository;
	private ObjectMapper objectMapper;
	private final Counter fuentesCreadas;
    private final Counter erroresDominio;
    private final Timer   tiempoAltaFuente;
	private static final Logger logger = LoggerFactory.getLogger(Fachada.class);


	@Autowired
	  public Fachada(FuenteRepository fuenteRepository,ObjectMapper objectMapper,MeterRegistry meterRegistry) {
	     logger.info("Initializing Fachada with FuenteRepository: {}", fuenteRepository.getClass().getSimpleName());
	    this.fuenteRepository = fuenteRepository;
	    this.objectMapper = objectMapper;
	    this.fuentesCreadas = Counter.builder("agregador.fuentes.creadas")
                .description("Cantidad de fuentes creadas").register(meterRegistry);

        this.erroresDominio = Counter.builder("agregador.errores")
                .description("Errores de negocio en Agregador").register(meterRegistry);

        this.tiempoAltaFuente = Timer.builder("agregador.hechos.alta.tiempo")
                .description("Tiempo de alta de fuente").register(meterRegistry);
	  }

    @Override
    public FuenteDTO agregar(FuenteDTO fuente) {
    	return tiempoAltaFuente.record(() -> {
    	    if (fuente == null) {
    	        erroresDominio.increment();
    	        throw new InvalidParameterException("No se puede agregar una fuente nula");
    	    }
    	    FuenteDTO nuevaFuenteDto = new FuenteDTO(String.valueOf(fuenteRepository.findAll().size() + 1), fuente.nombre(), fuente.endpoint());
    	    Fuente nuevaFuente = new Fuente(nuevaFuenteDto.id(), fuente.nombre(), fuente.endpoint());
    	    fuenteRepository.save(nuevaFuente);
    	    agregador.agregarFuente(nuevaFuente);
    	    var proxy = new FuenteProxy(objectMapper, nuevaFuente.getEndpoint());
    	    this.addFachadaFuentes(nuevaFuente.getId(), proxy);
    	    fuentesCreadas.increment();
    	    return nuevaFuenteDto;
    	});

    }

    @Override  @Transactional(readOnly = true)
    public List<FuenteDTO> fuentes() {
        return Collections.unmodifiableList(
        		fuenteRepository.findAll().stream()
                        .map(FuenteMapper::toDTO)
                        .toList()
        );    
    }

    @Override  @Transactional(readOnly = true)
    public FuenteDTO buscarFuenteXId(String fuenteId) throws NoSuchElementException {
        return buscarFuentePorIdEnLista(fuenteId)
                .orElseThrow(() -> new NoSuchElementException("Fuente no encontrada por id " + fuenteId));
    }

    private Optional<FuenteDTO> buscarFuentePorIdEnLista(String fuenteId) {
        return fuenteRepository.findById(fuenteId).map(f -> new FuenteDTO(String.valueOf(f.getId()), f.getNombre(), f.getEndpoint()));
    }

    @Override  @Transactional(readOnly = true)
    public List<HechoDTO> hechos(String coleccionId) throws NoSuchElementException {
        List<Fuente> fuentes = fuenteRepository.findAll();
        agregador.setListFuentes(fuentes);
        
        for(Fuente fuente : fuentes) {
        	if(!agregador.getFachadaFuentes().containsKey(fuente.getId())) {
        		var proxy = new FuenteProxy(objectMapper, fuente.getEndpoint());
        		agregador.agregarFachadaAFuente(fuente.getId(), proxy);
                logger.info(fuente.getEndpoint());
        	}else
        	{
        		logger.info("La fuente " + fuente.getId()+ " Ya se encuentra en la lista");
        	}
        }
        
        List<Hecho> hechosFiltrados = agregador.obtenerHechosPorColeccion(coleccionId);
        if(hechosFiltrados == null || hechosFiltrados.isEmpty()) {
        	throw new NoSuchElementException("No se encontraron hechos para la coleccion : "+ coleccionId);
        }
        Collections.sort(hechosFiltrados, Comparator.comparing(Hecho::getId));

        return mappearHechoADTO(hechosFiltrados);
    }
    public List<HechoDTO> mappearHechoADTO(List<Hecho> hechos) {
        return hechos.stream()
                .map(HechoMapper::toDTO)
                .toList();
    }
    @Override
    public void addFachadaFuentes(String fuenteId, FachadaFuente fuente) {
        agregador.agregarFachadaAFuente(fuenteId, fuente);
    }
    @Override
    public void setConsensoStrategy(ConsensosEnum tipoConsenso, String coleccionId) throws InvalidParameterException {
        agregador.configurarConsenso(tipoConsenso, coleccionId);
    }

	
}
