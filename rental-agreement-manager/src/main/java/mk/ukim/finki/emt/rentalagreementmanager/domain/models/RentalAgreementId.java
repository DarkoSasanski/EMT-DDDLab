package mk.ukim.finki.emt.rentalagreementmanager.domain.models;

import lombok.NonNull;
import mk.ukim.finki.emt.sharedkernel.domain.base.DomainObjectId;


public class RentalAgreementId extends DomainObjectId {
    protected RentalAgreementId(){
        super(randomId(RentalAgreementId.class).getId());
    }
    public RentalAgreementId(@NonNull String uuid)
    {
        super(uuid);
    }
    public RentalAgreementId of(String uuid)
    {
        return new RentalAgreementId(uuid);
    }
}
