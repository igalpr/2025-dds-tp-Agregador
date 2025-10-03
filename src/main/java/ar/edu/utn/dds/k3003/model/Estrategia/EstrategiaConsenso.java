package ar.edu.utn.dds.k3003.model.Estrategia;

import java.util.*;

import ar.edu.utn.dds.k3003.model.Hecho;
public interface EstrategiaConsenso {
    List<Hecho> filtrar(List<Hecho> hechos, List<String> listaFuentes);
}
