package ar.edu.utn.dds.k3003.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity  
public class Fuente {
    
	@Id
	private int id;
    private String nombre;
    private String endpoint;

    // Constructor
    public Fuente() {
        this.id = 1;
        this.nombre = "";
        this.endpoint = "";
    }
    public Fuente(int id, String nombre, String endpoint) {
        this.id = id;
        this.nombre = nombre;
        this.endpoint = endpoint;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }
}
