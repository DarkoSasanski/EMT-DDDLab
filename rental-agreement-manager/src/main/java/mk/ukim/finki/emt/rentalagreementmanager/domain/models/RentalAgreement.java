package mk.ukim.finki.emt.rentalagreementmanager.domain.models;

import lombok.Getter;
import lombok.NonNull;
import mk.ukim.finki.emt.rentalagreementmanager.domain.valueobjects.CustomerId;
import mk.ukim.finki.emt.rentalagreementmanager.domain.valueobjects.LocationId;
import mk.ukim.finki.emt.rentalagreementmanager.domain.valueobjects.Vehicle;
import mk.ukim.finki.emt.rentalagreementmanager.domain.valueobjects.VehicleId;
import mk.ukim.finki.emt.sharedkernel.domain.base.AbstractEntity;
import mk.ukim.finki.emt.sharedkernel.domain.financial.Currency;
import mk.ukim.finki.emt.sharedkernel.domain.financial.Money;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static java.lang.Math.abs;

@Entity
@Table(name = "rental_agreements")
@Getter
public class RentalAgreement extends AbstractEntity<RentalAgreementId> {
    @AttributeOverride(name = "id", column = @Column(name = "vehicle_id"))
    private VehicleId vehicleId;
    @AttributeOverride(name = "id", column = @Column(name = "location_id"))
    private LocationId locationId;
    @AttributeOverride(name = "id", column = @Column(name = "customer_id"))
    private CustomerId customerId;
    private LocalDate rentalReserved;
    private LocalDate carReturned;
    private LocalDate rentalStartDate;
    private LocalDate rentalEndDate;
    @AttributeOverrides({
            @AttributeOverride(name="amount", column = @Column(name="coef_amount")),
            @AttributeOverride(name="currency", column = @Column(name="coef_currency"))
    })
    private Money penaltyCoef;
    @AttributeOverrides({
            @AttributeOverride(name="amount", column = @Column(name="penalty_amount")),
            @AttributeOverride(name="currency", column = @Column(name="penalty_currency"))
    })
    private Money penalty;
    private int startFuel;
    private int returnFuel;
    @Enumerated(value = EnumType.STRING)
    private RentalAgreementStatus rentalAgreementStatus;
    @Enumerated(value = EnumType.STRING)
    private Currency currency;
    private RentalAgreement(){
        super(RentalAgreementId.randomId(RentalAgreementId.class));
    }

    public RentalAgreement(VehicleId vehicleId, LocationId locationId, CustomerId customerId,
                           LocalDate rentalEndDate, LocalDate rentalReserved, Currency currency, int penaltyCoef) {
        this.vehicleId = vehicleId;
        this.locationId = locationId;
        this.customerId = customerId;
        this.rentalEndDate = rentalEndDate;
        this.rentalReserved = rentalReserved;
        this.currency = currency;
        this.penalty = new Money(currency, 0);
        this.penaltyCoef = new Money(currency, penaltyCoef);
    }

    public void pickUpVehicle(Vehicle vehicle)
    {
        if(!rentalAgreementStatus.equals(RentalAgreementStatus.RESERVED))
        {
            throw new IllegalArgumentException();
        }
        if(vehicle.getLocationId().equals(locationId))
        {
            throw new IllegalArgumentException("Vehicle is parked at another location");
        }
        rentalAgreementStatus = RentalAgreementStatus.ACTIVE;
        rentalStartDate = LocalDate.now();
        if(rentalReserved.isBefore(rentalStartDate))
            penalty=penalty.add(penaltyCoef.multiply((int) ChronoUnit.DAYS.between(rentalReserved, rentalStartDate)));
        startFuel = vehicle.getFuel();
    }

    public void cancelAgreement()
    {
        if(!rentalAgreementStatus.equals(RentalAgreementStatus.RESERVED))
        {
            throw new IllegalArgumentException();
        }
        rentalAgreementStatus=RentalAgreementStatus.CANCELLED;
        if(ChronoUnit.DAYS.between(LocalDate.now(), rentalReserved)<=2)
        {
            penalty=penalty.add(penaltyCoef.multiply((int) ChronoUnit.DAYS.between(LocalDate.now(), rentalReserved)));
        }
    }

    public void returnVehicle(Vehicle vehicle)
    {
        if(!rentalAgreementStatus.equals(RentalAgreementStatus.ACTIVE))
        {
            throw new IllegalArgumentException();
        }
        rentalAgreementStatus = RentalAgreementStatus.CLOSED;
        returnFuel = vehicle.getFuel();
        carReturned = LocalDate.now();
        if(ChronoUnit.DAYS.between(LocalDate.now(), rentalEndDate)!=0)
        {
            penalty=penalty.add(penaltyCoef.multiply((int) abs(ChronoUnit.DAYS.between(LocalDate.now(), rentalEndDate))));
        }
        if(returnFuel<startFuel)
        {
            penalty = penalty.add(penaltyCoef.multiply(startFuel-returnFuel));
        }
    }

    public Money getTotalPrice(Vehicle vehicle)
    {
        if(rentalAgreementStatus.equals(RentalAgreementStatus.CLOSED))
        {
            Money total = vehicle.getPrice();
            total = total.multiply((int) ChronoUnit.DAYS.between(rentalStartDate, carReturned));
            total = total.add(penalty);
            return total;
        }
        else if(rentalAgreementStatus.equals(RentalAgreementStatus.CANCELLED))
        {
            Money total = new Money(currency, 0);
            total = total.add(penalty);
            return total;
        }
        throw new IllegalArgumentException();
    }
}
