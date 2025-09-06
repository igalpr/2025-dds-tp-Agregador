package ar.edu.utn.dds.k3003.repository;

import ar.edu.utn.dds.k3003.model.Hecho;

import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Profile("!test")
public interface JpaHechoRepository extends JpaRepository<Hecho, String>, HechoRepository {

	// JpaRepository already provides methods like findById, findAll, save, etc.
	// You can define custom query methods here if needed.
	
	// Example of a custom query method:
	// List<Hecho> findByTitulo(String titulo);
	
	// If you need to clear the repository, you can use the deleteAll method provided by JpaRepository.


} 