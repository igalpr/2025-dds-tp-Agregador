package ar.edi.itn.dds.k3003.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ar.edu.utn.dds.k3003.model.Fachada.*;
import ar.edu.utn.dds.k3003.app.Fachada;
import ar.edu.utn.dds.k3003.facades.dtos.FuenteDTO;
import ar.edu.utn.dds.k3003.facades.dtos.HechoDTO;
import ar.edu.utn.dds.k3003.model.ConsensosEnum;
import ar.edu.utn.dds.k3003.repository.InMemoryFuenteRepo;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AgregadorTest {

    private Fachada agregador;

    @Mock
    private FachadaFuente fachadaFuenteMock1;

    @Mock
    private FachadaFuente fachadaFuenteMock2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        agregador = new Fachada(new InMemoryFuenteRepo(), null, null);
    }

    @Test
    @DisplayName("Agregar FuenteDTO y retorna FuenteDTO con id generado")
    void testAgregarFuente() {
        FuenteDTO fuenteSinId = new FuenteDTO("", "Fuente1", "endpoint1");
        FuenteDTO fuenteAgregada = agregador.agregar(fuenteSinId);

        assertNotNull(fuenteAgregada);
        assertNotNull(fuenteAgregada.id());
        assertEquals("Fuente1", fuenteAgregada.nombre());
        assertEquals("endpoint1", fuenteAgregada.endpoint());
    }
    @Test
    @DisplayName("Agregar FuenteDTO y retorna FuenteDTO con el id enviado")
    void testAgregarFuenteConId() {
    	Random random = new Random();
    	int idRandom = random.nextInt(10 - 1) + 1;
        FuenteDTO fuenteSinId = new FuenteDTO(String.valueOf(idRandom), "Fuente1", "endpoint1");
        FuenteDTO fuenteAgregada = agregador.agregar(fuenteSinId);

        assertNotNull(fuenteAgregada);
        assertNotNull(fuenteAgregada.id());
        assertEquals("Fuente1", fuenteAgregada.nombre());
        assertEquals("endpoint1", fuenteAgregada.endpoint());
    }

    @Test
    @DisplayName("Funcion Lista de fuenta devuelve las fuentes agregadadas")
    void testFuentes() {
        FuenteDTO f1 = agregador.agregar(new FuenteDTO("", "Fuente1", "endpoint1"));
        FuenteDTO f2 = agregador.agregar(new FuenteDTO("", "Fuente2", "endpoint2"));

        List<FuenteDTO> fuentes = agregador.fuentes();

        assertEquals(2, fuentes.size());
        assertTrue(fuentes.contains(f1));
        assertTrue(fuentes.contains(f2));
    }

    @Test
    @DisplayName("Buscar fuente por id retorna la fuente correcta")
    void testBuscarFuenteXId() {
        FuenteDTO fuente = agregador.agregar(new FuenteDTO("", "FuenteBuscada", "endpoint"));

        FuenteDTO encontrada = agregador.buscarFuenteXId(fuente.id());

        assertNotNull(encontrada);
        assertEquals(fuente.id(), encontrada.id());
        assertEquals("FuenteBuscada", encontrada.nombre());
    }

    @Test
    @DisplayName("Buscar fuente por id inexistente lanza NoSuchElementException")
    void testBuscarFuenteXIdNoExiste() {
        assertThrows(NoSuchElementException.class,
                () -> agregador.buscarFuenteXId("id-inexistente"));
    }

    @Test
    @DisplayName("Agregar FachadaFuente y recuperar todo los hechos con los dos mecanismos de consenso")
    void testAddFachadaFuentesYHechos() {
        FuenteDTO fuenteDTO1 = agregador.agregar(new FuenteDTO("", "FuenteMock1", "endpoint1"));
        FuenteDTO fuenteDTO2 = agregador.agregar(new FuenteDTO("", "FuenteMock2", "endpoint2"));

        agregador.addFachadaFuentes(fuenteDTO1.id(), fachadaFuenteMock1);
        agregador.addFachadaFuentes(fuenteDTO2.id(), fachadaFuenteMock2);

        List<HechoDTO> hechosFuente1 = List.of(
                new HechoDTO("1", "col1", "Titulo A"),
                new HechoDTO("2", "col1", "Titulo B")
        );

        List<HechoDTO> hechosFuente2 = List.of(
                new HechoDTO("3", "col1", "Titulo C"),
                new HechoDTO("2", "col1", "Titulo B") // overlap for consenso
        );

        when(fachadaFuenteMock1.buscarHechosXColeccion("col1")).thenReturn(hechosFuente1);
        when(fachadaFuenteMock2.buscarHechosXColeccion("col1")).thenReturn(hechosFuente2);

        List<HechoDTO> hechosTodos = agregador.hechos("col1");

        assertEquals(3, hechosTodos.size());
        assertTrue(hechosTodos.stream().anyMatch(h -> h.titulo().equals("Titulo A")));
        assertTrue(hechosTodos.stream().anyMatch(h -> h.titulo().equals("Titulo B")));
        assertTrue(hechosTodos.stream().anyMatch(h -> h.titulo().equals("Titulo C")));

        agregador.setConsensoStrategy(ConsensosEnum.AL_MENOS_2, "col1");
        List<HechoDTO> hechosAlMenos2 = agregador.hechos("col1");

        assertEquals(1, hechosAlMenos2.size());
        assertEquals("Titulo B", hechosAlMenos2.get(0).titulo());
    }

    @Test
    @DisplayName("Agregar FachadaFuente con fuenteId no existente lanza NoSuchElementException")
    void testAddFachadaFuentesFuenteIdInexistente() {
        assertThrows(NoSuchElementException.class, () ->
                agregador.addFachadaFuentes("idInexistente", fachadaFuenteMock1));
    }

    @Test
    @DisplayName("SetConsensoStrategy controla que los parametros sean validos")
    void testSetConsensoStrategyInvalidParams() {
        assertThrows(InvalidParameterException.class, () ->
                agregador.setConsensoStrategy(null, "col1"));

        assertThrows(InvalidParameterException.class, () ->
                agregador.setConsensoStrategy(ConsensosEnum.TODOS, null));

        assertThrows(InvalidParameterException.class, () ->
                agregador.setConsensoStrategy(ConsensosEnum.TODOS, ""));
    }

    @Test
    @DisplayName("Buscar hechos sin fuentes manejadas lanza NoSuchElementException")
    void testHechosSinFuentes() {
    	setUp();
        assertThrows(NoSuchElementException.class, () ->
                agregador.hechos("col1"));
        }

}

