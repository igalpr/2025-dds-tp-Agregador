package ar.edu.utn.dds.k3003.client;

import ar.edu.utn.dds.k3003.facades.FachadaFuente;
import ar.edu.utn.dds.k3003.facades.FachadaProcesadorPdI;
import ar.edu.utn.dds.k3003.facades.dtos.*;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class FuenteProxy implements FachadaFuente {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String BASE_URL = "https://two025-tp-entrega-2-juan9772-1.onrender.com";

    public List<HechoDTO> buscarHechosXColeccion(String s) throws NoSuchElementException {
    	String url = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .path("/api/hechos/{parametro1}")
                .buildAndExpand(s)
                .toUriString();


        ResponseEntity<List<HechoDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<HechoDTO>>() {}
        );
        List<HechoDTO> hechos = response.getBody();
        if (hechos == null || hechos.isEmpty()) {
            throw new NoSuchElementException("No se encontraron hechos para la colección: " + s);
        }

        return hechos;
    }


    @Override
    public ColeccionDTO agregar(ColeccionDTO coleccionDTO) {
        return null;
    }

    @Override
    public ColeccionDTO buscarColeccionXId(String s) throws NoSuchElementException {
        return null;
    }

    @Override
    public HechoDTO agregar(HechoDTO hechoDTO) {
        return null;
    }

    @Override
    public HechoDTO buscarHechoXId(String s) throws NoSuchElementException {
        return null;
    }

    @Override
    public void setProcesadorPdI(FachadaProcesadorPdI fachadaProcesadorPdI) {

    }

    @Override
    public PdIDTO agregar(PdIDTO pdIDTO) throws IllegalStateException {
        return null;
    }

    @Override
    public List<ColeccionDTO> colecciones() {
        return List.of();
    }
}
