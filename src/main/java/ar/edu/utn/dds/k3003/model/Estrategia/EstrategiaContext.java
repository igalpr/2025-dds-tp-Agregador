package ar.edu.utn.dds.k3003.model.Estrategia;

import ar.edu.utn.dds.k3003.model.Hecho;
import java.util.List;

public class EstrategiaContext {
    private EstrategiaConsenso estrategia;

    public void setEstrategia(EstrategiaConsenso estrategia) {
        this.estrategia = estrategia;
    }

    public List<Hecho> filtrar(List<Hecho> hechos, List<String> fuenteIds) {
        if (estrategia == null) {
            throw new IllegalStateException("No strategy set");
        }
        return estrategia.filtrar(hechos, fuenteIds);
    }
}
