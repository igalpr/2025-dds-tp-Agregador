package ar.edu.utn.dds.k3003.model.Estrategia;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import ar.edu.utn.dds.k3003.model.Hecho;

public class EstrategiaAlMenos2 implements EstrategiaConsenso {
	 @Override
	    public List<Hecho> filtrar(List<Hecho> hechos, List<String> listaFuentes) {
	        if (listaFuentes.size() == 1) {
	            Map<String, Hecho> hechosUnicos = hechos.stream()
	                .collect(Collectors.toMap(
	                    Hecho::getTitulo,
	                    Function.identity(),
	                    (existente, nuevo) -> existente));
	            return new ArrayList<>(hechosUnicos.values());
	        } else {
	            Set<String> titulosRepetidos = hechos.stream()
	                .collect(Collectors.groupingBy(Hecho::getTitulo,
	                    Collectors.mapping(Hecho::getOrigen, Collectors.toSet())))
	                .entrySet().stream()
	                .filter(e -> e.getValue().size() >= 2)
	                .map(Map.Entry::getKey)
	                .collect(Collectors.toSet());
	            return hechos.stream()
	                .filter(h -> titulosRepetidos.contains(h.getTitulo()))
	                .collect(Collectors.toMap(
	                    Hecho::getTitulo, Function.identity(),
	                    (h1, h2) -> h1))
	                .values().stream().collect(Collectors.toList());
	        }
	    }
}
