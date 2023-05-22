package mk.ukim.finki.emt.rentalagreementmanager.domain.valueobjects;

import mk.ukim.finki.emt.sharedkernel.domain.base.DomainObjectId;

import javax.persistence.Embeddable;

@Embeddable
public class VehicleId extends DomainObjectId {
    protected VehicleId()
    {
        super(randomId(VehicleId.class).getId());
    }

    public VehicleId(String uuid)
    {
        super(uuid);
    }
}
