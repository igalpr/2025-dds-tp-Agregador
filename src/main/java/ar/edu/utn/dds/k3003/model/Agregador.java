package ar.edu.utn.dds.k3003.model;

import ar.edu.utn.dds.k3003.client.*;
import ar.edu.utn.dds.k3003.model.*;
import ar.edu.utn.dds.k3003.repository.FuenteRepository;
import ar.edu.utn.dds.k3003.repository.InMemoryFuenteRepo;
import lombok.Data;
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

@Data
public class Agregador {
	private final Map<String, FachadaFuente> ListaFachadaFuente = new HashMap<>();
	private final Map<String, ConsensosEnum> consensoPorColeccion = new HashMap<>();
	private List<Fuente> listaFuentes= new ArrayList();  
	private static final Logger logger = LoggerFactory.getLogger(Agregador.class);
	public Agregador() { 
    }

    public Fuente agregarFuente(Fuente fuente) {
        listaFuentes.add(fuente);
        return fuente;
    }

    public Boolean HasFachadaFuente(String Id) {
    	return ListaFachadaFuente.containsKey(Id);
    }
    private Fuente buscarFuentePorIdEnLista(String fuenteId) {
        return listaFuentes.stream()
                .filter(f -> String.valueOf(f.getId()).equals(fuenteId))
                .findFirst().orElse(null);
    }

    
    public List<Hecho> hechos(String coleccionId) throws NoSuchElementException {
        if (ListaFachadaFuente.isEmpty()) {
            throw new NoSuchElementException("No hay fuentes cargadas");
        }

        Map<String, Integer> conteoHechos = new HashMap<>();
        Map<String, Hecho> hechosUnicos = new HashMap<>();

        
    	List<Hecho> hechos = getHechosFromFuentesByCollection(coleccionId);
            for (Hecho hecho : hechos) {
                String titulo = hecho.getTitulo();
                conteoHechos.put(titulo, conteoHechos.getOrDefault(titulo, 0) + 1);
                hechosUnicos.putIfAbsent(titulo, hecho);
            }
        

        List<Hecho> hechosFiltrados;

        if (ListaFachadaFuente.size() == 1) {
            hechosFiltrados = new ArrayList<>(hechosUnicos.values());
        } else {
            ConsensosEnum consenso =consensoPorColeccion.getOrDefault(coleccionId, ConsensosEnum.TODOS);

            switch (consenso) {
                case TODOS:
                    hechosFiltrados = new ArrayList<>(hechosUnicos.values());
                    break;
                case AL_MENOS_2:
                    hechosFiltrados = hechosUnicos.values().stream()
                            .filter(h -> conteoHechos.getOrDefault(h.getTitulo(), 0) >= 2)
                            .toList();
                    break;
                default:
                    hechosFiltrados = new ArrayList<>(hechosUnicos.values());
            }
        }

        return hechosFiltrados;
    }
    private List<Hecho> getHechosFromFuentesByCollection(String coleccionId){
    	List<Hecho> hechos = new ArrayList<>();
    	for (FachadaFuente fuente : ListaFachadaFuente.values()) {
        	List<HechoDTO> hechosDTOS = fuente.buscarHechosXColeccion(coleccionId); 
        	hechos.addAll(hechosDTOS.stream()
                    .map(HechoMapper::toEntity)
                    .toList());
    	}
    	return hechos;
    }
    public void addFachadaFuentes(String fuenteId, FachadaFuente fuente) {
        if (buscarFuentePorIdEnLista(fuenteId)== null) {
            throw new NoSuchElementException("La fuente DTO con id " + fuenteId + " no existe para mapear FachadaFuente");
        }
        ListaFachadaFuente.put(fuenteId, fuente);
    }

    public void setConsensoStrategy(ConsensosEnum tipoConsenso, String coleccionId) throws InvalidParameterException {
        if (tipoConsenso == null || coleccionId == null || coleccionId.isEmpty()) {
            throw new InvalidParameterException("Tipo de consenso y coleccionId no pueden ser nulos o vacíos");
        }
        consensoPorColeccion.put(coleccionId, tipoConsenso);
    }

	public List<Fuente> getListaFuentes() {
		return listaFuentes;
	}

	public void setListaFuentes(List<Fuente> listaFuentes) {
		this.listaFuentes = listaFuentes;
	}

	public static Logger getLogger() {
		return logger;
	}
}
