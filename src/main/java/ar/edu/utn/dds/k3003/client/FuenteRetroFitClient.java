package ar.edu.utn.dds.k3003.client;


import ar.edu.utn.dds.k3003.facades.dtos.*;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.path;

public interface FuenteRetroFitClient{
	@GET("/collection")
	Call<List<CollectionDTO>> getCollecciones();
	
	@GET("/collection/{id}/hechos")
	Call<List<HechosDTO>> getHechosPorColleccion(@Path("id") String id);

}