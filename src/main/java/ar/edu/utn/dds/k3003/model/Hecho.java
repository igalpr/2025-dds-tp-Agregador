package ar.edu.utn.dds.k3003.model;

import ar.edu.utn.dds.k3003.facades.dtos.CategoriaHechoEnum;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Hecho {

    private String id;
    private String nombreColeccion;
    private String titulo;
    private List<String> etiquetas;
    private CategoriaHechoEnum categoria;
    private String ubicacion;
    private LocalDateTime fecha;
    private String origen;
    public Hecho() {
        // No-argument constructor
    }
    public Hecho(String id, String nombreColeccion, String titulo, List<String> etiquetas,
                 CategoriaHechoEnum categoria, String ubicacion, LocalDateTime fecha, String origen) {
        this.id = id;
        this.nombreColeccion = nombreColeccion;
        this.titulo = titulo;
        this.etiquetas = etiquetas;
        this.categoria = categoria;
        this.ubicacion = ubicacion;
        this.fecha = fecha;
        this.origen = origen;
    }

    // Getters y setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombreColeccion() {
        return nombreColeccion;
    }

    public void setNombreColeccion(String nombreColeccion) {
        this.nombreColeccion = nombreColeccion;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public List<String> getEtiquetas() {
        return etiquetas;
    }

    public void setEtiquetas(List<String> etiquetas) {
        this.etiquetas = etiquetas;
    }

    public CategoriaHechoEnum getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaHechoEnum categoria) {
        this.categoria = categoria;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Hecho hecho = (Hecho) o;

        return Objects.equals(id, hecho.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}

