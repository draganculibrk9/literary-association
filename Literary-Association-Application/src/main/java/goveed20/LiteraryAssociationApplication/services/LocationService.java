package goveed20.LiteraryAssociationApplication.services;

import goveed20.LiteraryAssociationApplication.model.Location;
import goveed20.LiteraryAssociationApplication.utils.Coordinate;
import goveed20.LiteraryAssociationApplication.utils.Coordinates;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class LocationService {
    @Value("${geocoding-api.key}")
    private String geocodingApiKey;

    @Value("${geocoding-api.url}")
    private String url;

    private final RestTemplate restTemplate = new RestTemplate();

    public Location createLocation(String country, String city) {
        Coordinate coordinate = doGeocoding(country, city);

        return Location.builder()
                .country(country)
                .city(city)
                .latitude(coordinate.getLatitude())
                .longitude(coordinate.getLongitude())
                .build();
    }

    private Coordinate doGeocoding(String country, String city) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("access_key", geocodingApiKey)
                .queryParam("query", String.format("%s, %s", city, country))
                .queryParam("limit", 1);

        try {
            ResponseEntity<Coordinates> coordinate = restTemplate.getForEntity(builder.toUriString(), Coordinates.class);
            return coordinate.getBody().getData().get(0);
        } catch (Exception e) {
            return new Coordinate(45.0, 45.0);
        }
    }


}
