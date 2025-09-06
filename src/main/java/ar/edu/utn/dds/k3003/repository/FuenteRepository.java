package ar.edu.utn.dds.k3003.repository;

import ar.edu.utn.dds.k3003.model.*;

import java.util.List;
import java.util.Optional;

public interface FuenteRepository {

  Optional<Fuente> findById(String id);
  Fuente save(Fuente col);
  List<Fuente> findAll();
}
