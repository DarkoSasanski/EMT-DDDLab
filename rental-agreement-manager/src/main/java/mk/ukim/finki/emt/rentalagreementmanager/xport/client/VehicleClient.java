package mk.ukim.finki.emt.rentalagreementmanager.xport.client;

import mk.ukim.finki.emt.rentalagreementmanager.domain.valueobjects.Vehicle;
import mk.ukim.finki.emt.rentalagreementmanager.domain.valueobjects.VehicleId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.List;

@Service
public class VehicleClient {

    private final RestTemplate restTemplate;
    private final String serverUrl;

    public VehicleClient(@Value("${app.vehicle-manager.url}") String serverUrl) {
        this.serverUrl = serverUrl;
        this.restTemplate = new RestTemplate();
        var requestFactory = new SimpleClientHttpRequestFactory();
        this.restTemplate.setRequestFactory(requestFactory);
    }

    private UriComponentsBuilder uri() {
        return UriComponentsBuilder.fromUriString(this.serverUrl);
    }

    public List<Vehicle> findAll() {
        try {
            return restTemplate.exchange(uri().path("/api/vehicle").build().toUri(), HttpMethod.GET,null, new ParameterizedTypeReference<List<Vehicle>>() {
            }).getBody();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public Vehicle findById(VehicleId vehicleId) {
        try {
            URI uri = uri().path("/api/vehicle/{id}").buildAndExpand(vehicleId.getId()).toUri();
            ResponseEntity<Vehicle> response = restTemplate.exchange(uri, HttpMethod.GET, null, Vehicle.class);
            return response.getBody();
        } catch (Exception e) {
            return null; // or throw an exception, depending on your error handling strategy
        }
    }
}
