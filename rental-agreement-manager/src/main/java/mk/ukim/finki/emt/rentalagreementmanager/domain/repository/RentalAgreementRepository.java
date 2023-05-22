package mk.ukim.finki.emt.rentalagreementmanager.domain.repository;

import mk.ukim.finki.emt.rentalagreementmanager.domain.models.RentalAgreement;
import mk.ukim.finki.emt.rentalagreementmanager.domain.models.RentalAgreementId;
import mk.ukim.finki.emt.rentalagreementmanager.domain.valueobjects.VehicleId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.*;

public interface RentalAgreementRepository extends JpaRepository<RentalAgreement, RentalAgreementId> {
    boolean existsRentalAgreementsByVehicleIdAndRentalReservedBetween(VehicleId vehicleId, LocalDate from, LocalDate to);
    boolean existsRentalAgreementsByVehicleIdAndRentalStartDateBetween(VehicleId vehicleId, LocalDate from, LocalDate to);
    boolean existsRentalAgreementsByVehicleIdAndRentalEndDateBetween(VehicleId vehicleId, LocalDate from, LocalDate to);
    List<RentalAgreement> findAllByVehicleId(VehicleId vehicleId);
}
