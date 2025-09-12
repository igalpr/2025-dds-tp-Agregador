package ar.edu.utn.dds.k3003.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import ar.edu.utn.dds.k3003.facades.dtos.ConsensosEnum;

public class ConsensoRequest {
    private ConsensosEnum tipo;
    @JsonProperty("coleccion")
    private String coleccion;
    // Getters y Setters
    public ConsensosEnum getTipo() {
        return tipo;
    }

    public void setTipo(ConsensosEnum tipo) {
        this.tipo = tipo;
    }
    public String getColeccionId() {
		return coleccion;
	}

	public void setColeccionId(String coleccion) {
		this.coleccion = coleccion;
	}

	@Override
	public String toString() {
		return "ConsensoRequest{" +
				"tipo=" + tipo +
				", coleccion='" + coleccion + '\'' +
				'}';
	}
}
