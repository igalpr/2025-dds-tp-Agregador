package ar.edu.utn.dds.k3003.client;


import java.util.List;

import ar.edu.utn.dds.k3003.facades.dtos.*;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;;

public interface FuenteRetroFitClient{
	@GET("/collection")
	Call<List<ColeccionDTO>> getCollecciones();
	
	@GET("/collection/{id}/hechos")
	Call<List<HechoDTO>> getHechosPorColleccion(@Path("id") String id);

}