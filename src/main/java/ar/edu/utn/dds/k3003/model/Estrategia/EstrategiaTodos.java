package ar.edu.utn.dds.k3003.model.Estrategia;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import ar.edu.utn.dds.k3003.model.Hecho;

public class EstrategiaTodos implements EstrategiaConsenso {
    @Override
    public List<Hecho> filtrar(List<Hecho> hechos, List<String> listaFuentes) {
        Map<String, Hecho> hechosUnicos = hechos.stream()
            .collect(Collectors.toMap(
                Hecho::getTitulo,
                Function.identity(),
                (existente, nuevo) -> existente));
        return new ArrayList<>(hechosUnicos.values());
    }
}