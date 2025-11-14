package ar.edu.utn.dds.k3003.model;

import java.util.*;

import ar.edu.utn.dds.k3003.model.Fachada.*;
import ar.edu.utn.dds.k3003.facades.dtos.ColeccionDTO;
import ar.edu.utn.dds.k3003.facades.dtos.HechoDTO;
import ar.edu.utn.dds.k3003.mapper.HechoMapper;
import ar.edu.utn.dds.k3003.model.Estrategia.*;
import lombok.Data;

@Data
public class Agregador {

    private List<Fuente> listFuentes = new ArrayList<>();
    private Map<String, FachadaFuente> fachadaFuentes = new HashMap<>();
    private Map<String, ConsensosEnum> tipoConsensoXColeccion = new HashMap<>();
    private EstrategiaContext contexto = new EstrategiaContext();
    public Fuente agregarFuente(Fuente newFuente) {
        listFuentes.add(newFuente);
        return newFuente;
    }

    public void configurarConsenso(ar.edu.utn.dds.k3003.model.ConsensosEnum tipoConsenso, String nombreColeccion) {
        tipoConsensoXColeccion.put(nombreColeccion, tipoConsenso);
    }

    private List<Hecho> obtenerHechosDeTodasLasFuentes(String nombreColeccion) {
        List<Hecho> hechos = new ArrayList<>();

        for (Fuente fuente : listFuentes) {
            FachadaFuente fachada = fachadaFuentes.get(fuente.getId());
            if (fachada != null) {
                try {
                	if(ColeccionEnFuente(fachada,nombreColeccion)) {
                    List<HechoDTO> hechosDTO = fachada.buscarHechosXColeccion(nombreColeccion);
                    hechos.addAll(
                            hechosDTO.stream()
                                    .map(HechoMapper::toEntity)  // Usar la función toEntity para mapear directamente
                                    .toList());
                	}
                } catch (NoSuchElementException e) {
                }
            }
        }
        Collections.sort(hechos, Comparator.comparing(Hecho::getId));
        return hechos;
    }
    private boolean ColeccionEnFuente(FachadaFuente fachada , String coleccion) {
    	List<ColeccionDTO> colecciones = fachada.colecciones();
    	return colecciones.stream().anyMatch(dto -> dto.nombre().equals(coleccion));
    }
    public List<Hecho> obtenerHechosPorColeccion(String nombreColeccion) {
        ConsensosEnum estrategiaEnum = tipoConsensoXColeccion.containsKey(nombreColeccion) ? tipoConsensoXColeccion.get(nombreColeccion) : ConsensosEnum.TODOS;
        List<Hecho> hechos = obtenerHechosDeTodasLasFuentes(nombreColeccion);
        EstrategiaConsenso estrategia;
        switch (estrategiaEnum) {
        case TODOS:
            estrategia = new EstrategiaTodos();
            break;
        case ESTRICTO:
            estrategia = new EstrategiaEstricta(fachadaFuentes);
            break;
        case AL_MENOS_2:
            estrategia = new EstrategiaAlMenos2();
            break;
        default:
            throw new IllegalArgumentException("Estrategia no soportada: " + estrategiaEnum);
        }
        contexto.setEstrategia(estrategia);
        return contexto.filtrar(hechos, listFuentes.stream().map(Fuente::getId).toList());        
    }

    public void agregarFachadaAFuente(String fuenteId, FachadaFuente fuente) {
        Fuente existeFuente = listFuentes.stream()
                .filter(f -> f.getId().equals(fuenteId))
                .findAny()
                .orElse(null);

        if (existeFuente == null) {
            throw new NoSuchElementException("No se encontro la fuente");
        }
        fachadaFuentes.put(fuenteId, fuente);
        existeFuente.setFachadaFuente(fuente);
    }
}