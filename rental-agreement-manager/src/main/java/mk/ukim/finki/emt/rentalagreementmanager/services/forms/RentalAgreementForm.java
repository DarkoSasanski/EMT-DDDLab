package mk.ukim.finki.emt.rentalagreementmanager.services.forms;

import lombok.Data;
import lombok.NonNull;
import mk.ukim.finki.emt.rentalagreementmanager.domain.valueobjects.CustomerId;
import mk.ukim.finki.emt.rentalagreementmanager.domain.valueobjects.LocationId;
import mk.ukim.finki.emt.rentalagreementmanager.domain.valueobjects.VehicleId;
import mk.ukim.finki.emt.sharedkernel.domain.financial.Currency;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class RentalAgreementForm {
    @NotNull
    VehicleId vehicleId;
    @NotNull
    LocationId locationId;
    @NotNull
    CustomerId customerId;
    @NotNull
    LocalDate rentalEndDate;
    @NotNull
    LocalDate rentalReserved;
    @NotNull
    Currency currency;
    @NotNull
    int penaltyCoef;
}
