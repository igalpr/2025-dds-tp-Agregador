package ar.edu.utn.dds.k3003.client;

import ar.edu.utn.dds.k3003.app.Fachada;
import ar.edu.utn.dds.k3003.model.Fachada.*;
import ar.edu.utn.dds.k3003.facades.FachadaProcesadorPdI;
import ar.edu.utn.dds.k3003.facades.dtos.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.*;

import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class FuenteProxy implements FachadaFuente {

  private final FuenteRetroFitClient service;
  private static final Logger logger = LoggerFactory.getLogger(Fachada.class);

  public FuenteProxy(ObjectMapper objectMapper, String baseUrl) {
   var retrofit =
        new Retrofit.Builder()
            .baseUrl(baseUrl.endsWith("/")? baseUrl : baseUrl+"/")
            .addConverterFactory(JacksonConverterFactory.create(objectMapper))
            .build();

    this.service = retrofit.create(FuenteRetroFitClient.class);
  }

  @Override
  public List<ColeccionDTO> colecciones() {
	try {
		var response = service.getCollecciones().execute();
		if(response.isSuccessful() && response.body() != null) {
			return response.body();
		}
		throw new RuntimeException("Error al obtener las colecciones , error " + response.code());

	}catch(Exception e) {
		logger.error(e.getMessage());
		throw new RuntimeException("Fallo la comunicacion con la fuente");
	}
  }
  @Override
  public List<HechoDTO> buscarHechosXColeccion(String coleccionId) throws NoSuchElementException {
	  try {
			var response = service.getHechosPorColleccion(coleccionId).execute();
			if(response.isSuccessful() && response.body() != null) {
				return response.body();
			}
			if(response.code() ==HttpStatus.NOT_FOUND_404) {
				throw new NotFoundException();
			}
			throw new RuntimeException("Error al obtener los hechos por coleccion , error " + response.code());

		}catch(NotFoundException e) {
			throw new NoSuchElementException("No se encontro la collecion en la fuente ");
		}
	  	catch(Exception e) {
			logger.error(e.getMessage());

			throw new RuntimeException("Fallo la comunicacion con la fuente");
		}
  }
  public List<HechoDTO> getHechosSinSolicitud() throws NoSuchElementException {
	  try {
			var response = service.getHechosSinSolicitud().execute();
			if(response.isSuccessful() && response.body() != null) {
				return response.body();
			}
			throw new RuntimeException("Error al obtener los hechos sin solicitud , error " + response.code());
  }catch(Exception e) {
		logger.error(e.getMessage());

		throw new RuntimeException("Fallo la comunicacion con la fuente");
	}
  }
  @Override
  public ColeccionDTO buscarColeccionXId(String coleccionId) throws NoSuchElementException {
	  return null;
  }

  @Override
  public ColeccionDTO agregar(ColeccionDTO coleccionDTO) {
	return null;
  }


  @Override
  public HechoDTO agregar(HechoDTO hechoDTO) {
	return null;
  }

  @Override
  public HechoDTO buscarHechoXId(String hechoId) throws NoSuchElementException {
	return null;
  }

  

  @Override
  public void setProcesadorPdI(FachadaProcesadorPdI procesador) {
	
  }

  @Override
  public PdIDTO agregar(PdIDTO pdIDTO) throws IllegalStateException {
	return null;
  }

  

}