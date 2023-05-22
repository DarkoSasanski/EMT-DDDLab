package mk.ukim.finki.emt.rentalagreementmanager.xport.rest;

import lombok.AllArgsConstructor;
import mk.ukim.finki.emt.rentalagreementmanager.domain.models.RentalAgreement;
import mk.ukim.finki.emt.rentalagreementmanager.domain.models.RentalAgreementId;
import mk.ukim.finki.emt.rentalagreementmanager.domain.valueobjects.VehicleId;
import mk.ukim.finki.emt.rentalagreementmanager.services.RentalAgreementService;
import mk.ukim.finki.emt.rentalagreementmanager.services.forms.RentalAgreementForm;
import mk.ukim.finki.emt.sharedkernel.domain.financial.Money;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/agreements")
@AllArgsConstructor
public class RentalAgreementsResource {
    private final RentalAgreementService rentalAgreementService;

    @GetMapping()
    public List<RentalAgreement> findAll()
    {
        return rentalAgreementService.findAll();
    }
    @GetMapping("/byVehicle")
    public List<RentalAgreement> findAllByVehicle(@RequestBody VehicleId vehicleId)
    {
        return rentalAgreementService.findAllByVehicle(vehicleId);
    }

    @GetMapping("/findAgreement")
    public ResponseEntity<RentalAgreement> findById(@RequestBody RentalAgreementId id)
    {
        return rentalAgreementService.findById(id)
                .map(a->ResponseEntity.ok().body(a))
                .orElseGet(()->ResponseEntity.badRequest().build());
    }

    @PostMapping("/reserveVehicle")
    public ResponseEntity<RentalAgreement> reserveAVehicle(@RequestBody RentalAgreementForm form)
    {
        return rentalAgreementService.reserveAVehicle(form)
                .map(a->ResponseEntity.ok().body(a))
                .orElseGet(()->ResponseEntity.badRequest().build());
    }

    @GetMapping("/pickUpVehicle")
    public ResponseEntity<RentalAgreement> pickUpVehicle(@RequestBody RentalAgreementId rentalAgreementId)
    {
        return rentalAgreementService.pickUpVehicle(rentalAgreementId)
                .map(a->ResponseEntity.ok().body(a))
                .orElseGet(()->ResponseEntity.badRequest().build());
    }

    @GetMapping("/cancelAgreement")
    public ResponseEntity<RentalAgreement> cancelAgreement(@RequestBody RentalAgreementId rentalAgreementId)
    {
        return rentalAgreementService.cancelVehicle(rentalAgreementId)
                .map(a->ResponseEntity.ok().body(a))
                .orElseGet(()->ResponseEntity.badRequest().build());
    }

    @GetMapping("/returnVehicle")
    public ResponseEntity<RentalAgreement> returnVehicle(@RequestBody RentalAgreementId rentalAgreementId)
    {
        return rentalAgreementService.returnVehicle(rentalAgreementId)
                .map(a->ResponseEntity.ok().body(a))
                .orElseGet(()->ResponseEntity.badRequest().build());
    }

    @GetMapping("/getInvoice")
    public ResponseEntity<Money> getInvoice(@RequestBody RentalAgreementId rentalAgreementId)
    {
        return rentalAgreementService.getInvoice(rentalAgreementId)
                .map(a->ResponseEntity.ok().body(a))
                .orElseGet(()->ResponseEntity.badRequest().build());
    }


}
