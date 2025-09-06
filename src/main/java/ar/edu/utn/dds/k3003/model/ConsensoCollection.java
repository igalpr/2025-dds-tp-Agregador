package ar.edu.utn.dds.k3003.model;

import ar.edu.utn.dds.k3003.facades.dtos.ConsensosEnum;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity  
public class ConsensoCollection {
    
    @Id
    private String collectionId; 
    private ConsensosEnum consenso; 

    public ConsensoCollection() {
    }

    public ConsensoCollection(String collectionId, ConsensosEnum consenso) {
        this.collectionId = collectionId;
        this.consenso = consenso;
    }
    public String getCollectionId() {
		return collectionId;
	}
    public void setCollectionId(String collectionId) {
		this.collectionId = collectionId;
	}

	public ConsensosEnum getConsenso() {
		return consenso;
	}

	public void setConsenso(ConsensosEnum consenso) {
		this.consenso = consenso;
	}

}
