package mk.ukim.finki.emt.rentalagreementmanager.domain.valueobjects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import mk.ukim.finki.emt.sharedkernel.domain.financial.Currency;
import mk.ukim.finki.emt.sharedkernel.domain.financial.Money;

@Getter
public class Vehicle {
    private final VehicleId id;
    private final Money price;
    private final int fuel;
    private final LocationId locationId;

    public Vehicle() {
        id=VehicleId.randomId(VehicleId.class);
        price = new Money(Currency.MKD, 0);
        fuel=0;
        locationId=LocationId.randomId(LocationId.class);
    }

    @JsonCreator
    public Vehicle(@JsonProperty("id") VehicleId id, @JsonProperty("money") Money price,@JsonProperty("fuel") int fuel, @JsonProperty("locationId") LocationId locationId) {
        this.id=id;
        this.price = price;
        this.fuel = fuel;
        this.locationId=locationId;
    }
}
