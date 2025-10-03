package ar.edu.utn.dds.k3003.model.Estrategia;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import ar.edu.utn.dds.k3003.model.Fachada.*;
import ar.edu.utn.dds.k3003.facades.dtos.HechoDTO;
import ar.edu.utn.dds.k3003.model.Hecho;

public class EstrategiaEstricta implements EstrategiaConsenso {
    private Map<String, FachadaFuente> fachadaFuentes;
    public EstrategiaEstricta(Map<String, FachadaFuente> fachadaFuentes) {
        this.fachadaFuentes = fachadaFuentes;
    }
    @Override
    public List<Hecho> filtrar(List<Hecho> hechos, List<String> listaFuentesIds) {
        List<Hecho> hechosSinSolicitud = getHechosSinSolicitud(listaFuentesIds);
        Set<String> titulosSinSolicitud = hechosSinSolicitud.stream()
            .map(Hecho::getTitulo)
            .collect(Collectors.toSet());
        Map<String, Hecho> hechosUnicos = hechos.stream()
            .collect(Collectors.toMap(
                Hecho::getTitulo,
                Function.identity(),
                (existente, nuevo) -> existente));
        return hechosUnicos.values().stream()
            .filter(h -> titulosSinSolicitud.contains(h.getTitulo()))
            .toList();
    }
    private List<Hecho> getHechosSinSolicitud(List<String> listaFuentesIds) {
        List<Hecho> hechosSinSolicitud = new ArrayList<>();
        for (String fuenteId : listaFuentesIds) {
            FachadaFuente fachada = fachadaFuentes.get(fuenteId);
            if (fachada != null) {
                try {
                    List<HechoDTO> hechosDTO = fachada.getHechosSinSolicitud();
                    hechosSinSolicitud.addAll(
                        hechosDTO.stream()
                            .map(dto -> {
                                Hecho hecho = new Hecho(dto.titulo(), dto.id(), dto.nombreColeccion());
                                hecho.setOrigen(fuenteId);
                                return hecho;
                            }).toList());
                } catch (NoSuchElementException e) {
                    // Ignorar y continuar con la siguiente fuente
                    continue;
                }
            }
        }
        return hechosSinSolicitud;
    }


}
