package mk.ukim.finki.emt.rentalagreementmanager.domain.valueobjects;

import mk.ukim.finki.emt.sharedkernel.domain.base.DomainObjectId;

import javax.persistence.Embeddable;

@Embeddable
public class CustomerId extends DomainObjectId {
    protected CustomerId(){
        super(randomId(CustomerId.class).getId());
    }
    public CustomerId(String uuid)
    {
        super(uuid);
    }
}
