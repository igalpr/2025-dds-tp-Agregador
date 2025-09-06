package ar.edu.utn.dds.k3003.repository;

import ar.edu.utn.dds.k3003.model.*;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Profile("test")
public class InMemoryHechoRepo implements HechoRepository {

    private final List<Hecho> hechos;

    public InMemoryHechoRepo() {
    	hechos = new ArrayList<>();
		//fuenteCounter = 1;
	}
	//@Override
	public Optional<Hecho> findById(String titulo) {
		return this.hechos.stream()
        .filter(f -> String.valueOf(f.getTitulo()).equals(titulo))
        .findFirst();
		//return this.fuentes.stream().filter(x -> x.getNombre().equals(id)).findFirst();
	}

	public List<Hecho> findAll() {
		return new ArrayList<>(hechos);
	}
	public void clear() {
		hechos.clear();
	}
	/*public String generateId(String id) {
        String Id = id.isBlank() ? getNextFuenteId() : id;
        fuenteCounter++;
		return Id;
    }
	private String getNextFuenteId() {
        return String.valueOf(fuenteCounter);
    }*/
}
