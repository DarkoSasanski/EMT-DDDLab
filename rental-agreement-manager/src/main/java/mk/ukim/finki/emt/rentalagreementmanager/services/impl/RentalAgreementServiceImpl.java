package mk.ukim.finki.emt.rentalagreementmanager.services.impl;

import lombok.AllArgsConstructor;
import mk.ukim.finki.emt.rentalagreementmanager.domain.models.RentalAgreement;
import mk.ukim.finki.emt.rentalagreementmanager.domain.models.RentalAgreementId;
import mk.ukim.finki.emt.rentalagreementmanager.domain.models.RentalAgreementStatus;
import mk.ukim.finki.emt.rentalagreementmanager.domain.repository.RentalAgreementRepository;
import mk.ukim.finki.emt.rentalagreementmanager.domain.valueobjects.Vehicle;
import mk.ukim.finki.emt.rentalagreementmanager.domain.valueobjects.VehicleId;
import mk.ukim.finki.emt.rentalagreementmanager.services.RentalAgreementService;
import mk.ukim.finki.emt.rentalagreementmanager.services.forms.RentalAgreementForm;
import mk.ukim.finki.emt.rentalagreementmanager.xport.client.VehicleClient;
import mk.ukim.finki.emt.sharedkernel.domain.financial.Money;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Objects;

@Service
@AllArgsConstructor
@Transactional
public class RentalAgreementServiceImpl implements RentalAgreementService {
    private final RentalAgreementRepository rentalAgreementRepository;
    private final VehicleClient vehicleClient;
    private final Validator validator;
    @Override
    public Optional<RentalAgreement> findById(RentalAgreementId id) {
        return rentalAgreementRepository.findById(id);
    }

    @Override
    public List<RentalAgreement> findAll() {
        return rentalAgreementRepository.findAll();
    }

    @Override
    public List<RentalAgreement> findAllByVehicle(VehicleId vehicleId) {
        return rentalAgreementRepository.findAllByVehicleId(vehicleId);
    }

    @Override
    public Optional<RentalAgreement> reserveAVehicle(RentalAgreementForm form) {
        Objects.requireNonNull(form, "It mustn't be null");
        var check = validator.validate(form);
        if(checkAvailabilityOfVehicle(form.getVehicleId(), form.getRentalReserved(), form.getRentalEndDate()))
        {
            throw new IllegalArgumentException("Vehicle not available");
        }
        // TODO: check for customer data if we implement the customerManagement bounded context
        if(check.size()>0)
        {
            throw new IllegalArgumentException("All fields are required");
        }
        RentalAgreement agreement = new RentalAgreement(form.getVehicleId(), form.getLocationId(), form.getCustomerId(),
                form.getRentalEndDate(), form.getRentalReserved(), form.getCurrency(), form.getPenaltyCoef());
        rentalAgreementRepository.saveAndFlush(agreement);
        return Optional.of(agreement);
    }

    @Override
    public Optional<RentalAgreement> pickUpVehicle(RentalAgreementId id) {
        RentalAgreement agreement = findById(id).orElseThrow(()-> new IllegalArgumentException("Not found agreement"));
        Vehicle vehicle = vehicleClient.findById(agreement.getVehicleId());
        agreement.pickUpVehicle(vehicle);
        return Optional.of(rentalAgreementRepository.saveAndFlush(agreement));
    }

    @Override
    public Optional<RentalAgreement> cancelVehicle(RentalAgreementId id) {
        RentalAgreement agreement = findById(id).orElseThrow(()-> new IllegalArgumentException("Not found agreement"));
        agreement.cancelAgreement();
        return Optional.of(rentalAgreementRepository.saveAndFlush(agreement));
    }

    @Override
    public Optional<RentalAgreement> returnVehicle(RentalAgreementId id) {
        RentalAgreement agreement = findById(id).orElseThrow(()-> new IllegalArgumentException("Not found agreement"));
        Vehicle vehicle = vehicleClient.findById(agreement.getVehicleId());
        agreement.returnVehicle(vehicle);
        return Optional.of(rentalAgreementRepository.saveAndFlush(agreement));
    }

    @Override
    public Optional<Money> getInvoice(RentalAgreementId id) {
        RentalAgreement agreement = findById(id).orElseThrow(()-> new IllegalArgumentException("Not found agreement"));
        Vehicle vehicle = vehicleClient.findById(agreement.getVehicleId());
        return Optional.ofNullable(agreement.getTotalPrice(vehicle));
    }

    @Override
    public boolean checkAvailabilityOfVehicle(VehicleId id, LocalDate from, LocalDate to) {
        List<RentalAgreementStatus> statuses = List.of(RentalAgreementStatus.CANCELLED, RentalAgreementStatus.CLOSED);
        return rentalAgreementRepository.existsRentalAgreementsByVehicleIdAndRentalReservedBetweenAndRentalAgreementStatusNotIn(id, from, to, statuses)
                || rentalAgreementRepository.existsRentalAgreementsByVehicleIdAndRentalStartDateBetweenAndRentalAgreementStatusNotIn(id, from, to, statuses)
                || rentalAgreementRepository.existsRentalAgreementsByVehicleIdAndRentalEndDateBetweenAndRentalAgreementStatusNotIn(id, from, to, statuses);
    }
}
