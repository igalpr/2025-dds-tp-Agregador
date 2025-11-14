package ar.edu.utn.dds.k3003.client;


import java.util.List;

import ar.edu.utn.dds.k3003.facades.dtos.*;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;;

public interface FuenteRetroFitClient{
	@GET("/colecciones")
	Call<List<ColeccionDTO>> getCollecciones();
	
	@GET("/coleccion/{nombre}/hechos")
	Call<List<HechoDTO>> getHechosPorColleccion(@Path("nombre") String id);
	
	@GET("/hechos/sin-solicitudes")
	Call<List<HechoDTO>> getHechosSinSolicitud();
	
	@POST("/busqueda")
	Call<List<HechoDTO>> busqueda();
}