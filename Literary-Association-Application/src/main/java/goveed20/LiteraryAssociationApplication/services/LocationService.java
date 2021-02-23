package goveed20.LiteraryAssociationApplication.services;

import goveed20.LiteraryAssociationApplication.model.Location;
import goveed20.LiteraryAssociationApplication.utils.Coordinate;
import goveed20.LiteraryAssociationApplication.utils.Coordinates;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class LocationService {
    @Value("${geocoding-api.key}")
    private String geocodingApiKey;

    @Value("${geocoding-api.url}")
    private String url;

    private final RestTemplate restTemplate = new RestTemplate();

    @SneakyThrows
    public Location createLocation(String country, String city) {
        Coordinate coordinate = doGeocoding(country, city);
        Thread.sleep(1000);

        return Location.builder()
                .country(country)
                .city(city)
                .latitude(coordinate.getLatitude())
                .longitude(coordinate.getLongitude())
                .build();
    }

    private Coordinate doGeocoding(String country, String city) {
        String requestUrl = String.format("%s?access_key=%s&query=%s&limit=1", url, geocodingApiKey, String.format("%s, %s", city, country));
        try {
            ResponseEntity<Coordinates> coordinate = restTemplate.getForEntity(requestUrl, Coordinates.class);
            return coordinate.getBody().getData().get(0);
        } catch (Exception e) {
            System.out.printf("Failed to get coordinates, setting default values for %s, %s%n", city, country);
            return new Coordinate(45.0, 45.0);
        }
    }


}
