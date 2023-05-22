package mk.ukim.finki.emt.rentalagreementmanager.domain.valueobjects;

import mk.ukim.finki.emt.sharedkernel.domain.base.DomainObjectId;

import javax.persistence.Embeddable;

@Embeddable
public class LocationId extends DomainObjectId {
    protected LocationId(){
        super(randomId(LocationId.class).getId());
    }

    public LocationId(String uuid)
    {
        super(uuid);
    }
}
