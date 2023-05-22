package mk.ukim.finki.emt.rentalagreementmanager.services;

import mk.ukim.finki.emt.rentalagreementmanager.domain.models.RentalAgreement;
import mk.ukim.finki.emt.rentalagreementmanager.domain.models.RentalAgreementId;
import mk.ukim.finki.emt.rentalagreementmanager.domain.valueobjects.VehicleId;
import mk.ukim.finki.emt.rentalagreementmanager.services.forms.RentalAgreementForm;
import mk.ukim.finki.emt.sharedkernel.domain.financial.Money;

import java.time.LocalDate;
import java.util.*;

public interface RentalAgreementService {
    Optional<RentalAgreement> findById(RentalAgreementId id);
    List<RentalAgreement> findAll();
    List<RentalAgreement> findAllByVehicle(VehicleId vehicleId);
    Optional<RentalAgreement> reserveAVehicle(RentalAgreementForm form);
    Optional<RentalAgreement> pickUpVehicle(RentalAgreementId id);
    Optional<RentalAgreement> cancelVehicle(RentalAgreementId id);
    Optional<RentalAgreement> returnVehicle(RentalAgreementId id);
    Optional<Money> getInvoice(RentalAgreementId id);
    boolean checkAvailabilityOfVehicle(VehicleId id, LocalDate from, LocalDate to);


}
