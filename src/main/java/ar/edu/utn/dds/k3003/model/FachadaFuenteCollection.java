package ar.edu.utn.dds.k3003.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity  
public class FachadaFuenteCollection {
    
	@Id
	private String fuenteId;
    //private FachadaFuente fachadaFuente;

    // Constructor , FachadaFuente fachadaFuente)
    public FachadaFuenteCollection(String id) {
        this.fuenteId = id;
        //this.fachadaFuente = fachadaFuente;
    }

    // Getters y Setters
    public String getFuenteId() {
        return fuenteId;
    }

    public void setFuenteId(String id) {
        this.fuenteId = id;
    }

    /*public FachadaFuente getFachadaFuente() {
        return fachadaFuente;
    }

    public void setFachadaFuente(FachadaFuente fachadaFuente) {
        this.fachadaFuente = fachadaFuente;
    }*/
}
