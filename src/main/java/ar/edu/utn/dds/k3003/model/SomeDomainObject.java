package ar.edu.utn.dds.k3003.model;

import ar.edu.utn.dds.k3003.model.exceptions.SomeDomainException;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Objects;

@Data
@AllArgsConstructor
public class SomeDomainObject {
    private String anAttribute;
    private Long otherAttribute;
    public SomeDomainObject(String string, long l) {
    	anAttribute =string;
    	otherAttribute =l;
		}
    public String getAnAttribute() {
    	return anAttribute;
    }
    public Long getOtherAttribute() {
    	return otherAttribute;
    }

    public SomeDomainObject sum(SomeDomainObject other) {
        if (Objects.isNull(other.getAnAttribute())) {
            throw new SomeDomainException("anAttribute is null", other);
        }
        return new SomeDomainObject(
                anAttribute + other.getAnAttribute(), otherAttribute + other.getOtherAttribute());
    }
}
