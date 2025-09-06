package ar.edu.utn.dds.k3003.repository;

import ar.edu.utn.dds.k3003.model.*;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Profile("test")
public class InMemoryFuenteRepo implements FuenteRepository {

    private final List<Fuente> fuentes;
    //private int fuenteCounter; 

    public InMemoryFuenteRepo() {
		fuentes = new ArrayList<>();
		//fuenteCounter = 1;
	}
	@Override
	public Optional<Fuente> findById(String id) {
		return this.fuentes.stream()
        .filter(f -> String.valueOf(f.getId()).equals(id))
        .findFirst();
		//return this.fuentes.stream().filter(x -> x.getNombre().equals(id)).findFirst();
	}

	@Override
	public Fuente save(Fuente fuente) {
		this.fuentes.add(fuente);
		return fuente;
	}

	@Override
	public List<Fuente> findAll() {
		return new ArrayList<>(fuentes);
	}
	public void clear() {
	    fuentes.clear();
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
