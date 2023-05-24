package mk.ukim.finki.emt.rentalagreementmanager.domain.repository;

import mk.ukim.finki.emt.rentalagreementmanager.domain.models.RentalAgreement;
import mk.ukim.finki.emt.rentalagreementmanager.domain.models.RentalAgreementId;
import mk.ukim.finki.emt.rentalagreementmanager.domain.models.RentalAgreementStatus;
import mk.ukim.finki.emt.rentalagreementmanager.domain.valueobjects.VehicleId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.*;

public interface RentalAgreementRepository extends JpaRepository<RentalAgreement, RentalAgreementId> {
    boolean existsRentalAgreementsByVehicleIdAndRentalReservedBetweenAndRentalAgreementStatusNotIn(VehicleId vehicleId, LocalDate from, LocalDate to, List<RentalAgreementStatus> statuses);
    boolean existsRentalAgreementsByVehicleIdAndRentalStartDateBetweenAndRentalAgreementStatusNotIn(VehicleId vehicleId, LocalDate from, LocalDate to, List<RentalAgreementStatus> statuses);
    boolean existsRentalAgreementsByVehicleIdAndRentalEndDateBetweenAndRentalAgreementStatusNotIn(VehicleId vehicleId, LocalDate from, LocalDate to, List<RentalAgreementStatus> statuses);
    List<RentalAgreement> findAllByVehicleId(VehicleId vehicleId);
}
