package ar.edu.utn.dds.k3003.mapper;

import ar.edu.utn.dds.k3003.model.Fuente;
import ar.edu.utn.dds.k3003.facades.dtos.FuenteDTO;

public class FuenteMapper {
    public static FuenteDTO toDTO(Fuente fuente) {
        if (fuente == null) {
            return null; 
        }
        return new FuenteDTO(String.valueOf(fuente.getId()), fuente.getNombre(), fuente.getEndpoint());
    }
    public static Fuente toEntity(FuenteDTO fuenteDTO) {
        if (fuenteDTO == null) {
            return null; 
        }
        return new Fuente(Integer.parseInt(fuenteDTO.id()), fuenteDTO.nombre(), fuenteDTO.endpoint());
    }
}
