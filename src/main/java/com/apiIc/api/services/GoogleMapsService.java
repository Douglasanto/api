package com.apiIc.api.services;

import com.apiIc.api.dto.GeocodingRequestDTO;
import com.apiIc.api.dto.LocalizacaoDTO;
import com.apiIc.api.dto.NearbyPlaceDTO;
import com.apiIc.api.services.execptions.ResourceNotFoundException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.HttpStatus;

import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class GoogleMapsService {

    @Value("${google.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<NearbyPlaceDTO> buscarLocaisProximos(double latitude, double longitude, String tipo) {
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl("https://maps.googleapis.com/maps/api/place/nearbysearch/json")
                .queryParam("location", latitude + "," + longitude)
                .queryParam("radius", 10000)
                .queryParam("key", apiKey);

        if (tipo != null && !tipo.isBlank()) {
            builder.queryParam("type", tipo);
        }

        URI uri = builder.build(true).toUri();
        try {
            ResponseEntity<String> resp = restTemplate.getForEntity(uri, String.class);
            JsonNode root = objectMapper.readTree(resp.getBody());
            String status = root.path("status").asText();

            if ("ZERO_RESULTS".equals(status)) {
                throw new ResourceNotFoundException("Nenhum local encontrado para os parâmetros informados.");
            }
            if (!"OK".equals(status)) {
                String msg = root.path("error_message").asText("Erro ao consultar Google Places");
                throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, msg);
            }

            List<NearbyPlaceDTO> resultado = new ArrayList<>();
            for (Iterator<JsonNode> it = root.path("results").elements(); it.hasNext(); ) {
                JsonNode place = it.next();
                String name = place.path("name").asText(null);
                String address = place.path("vicinity").asText(null);
                if (address == null || address.isBlank()) {
                    address = place.path("formatted_address").asText("");
                }
                JsonNode location = place.path("geometry").path("location");
                Double lat = location.path("lat").isNumber() ? location.path("lat").asDouble() : null;
                Double lng = location.path("lng").isNumber() ? location.path("lng").asDouble() : null;

                resultado.add(new NearbyPlaceDTO(name, address, lat, lng));
            }
            return resultado;
        } catch (RestClientException e) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Falha ao acessar Google Places", e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao processar resposta do Google Places", e);
        }
    }

    public LocalizacaoDTO geocodificarEndereco(GeocodingRequestDTO req) {
        String enderecoCompleto = String.format("%s, %s, %s - %s, %s",
                n(req.getRua()), n(req.getNumero()), n(req.getCidade()), n(req.getEstado()), n(req.getCep()));

        return geocodeAddress(enderecoCompleto);
    }

    public LocalizacaoDTO getCoordinatesFromAddress(String enderecoCompleto) {
        return geocodeAddress(enderecoCompleto);
    }

    private LocalizacaoDTO geocodeAddress(String enderecoCompleto) {
        URI uri = UriComponentsBuilder
                .fromHttpUrl("https://maps.googleapis.com/maps/api/geocode/json")
                .queryParam("address", enderecoCompleto)
                .queryParam("key", apiKey)
                .build(true)
                .toUri();

        try {
            ResponseEntity<String> resp = restTemplate.getForEntity(uri, String.class);
            JsonNode root = objectMapper.readTree(resp.getBody());
            String status = root.path("status").asText();

            if ("ZERO_RESULTS".equals(status)) {
                throw new ResourceNotFoundException("Endereço não encontrado.");
            }
            if (!"OK".equals(status)) {
                String msg = root.path("error_message").asText("Erro ao consultar Google Geocoding");
                throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, msg);
            }

            JsonNode first = root.path("results").get(0);
            if (first == null || first.isMissingNode()) {
                throw new ResourceNotFoundException("Endereço não encontrado.");
            }
            JsonNode location = first.path("geometry").path("location");
            double lat = location.path("lat").asDouble();
            double lng = location.path("lng").asDouble();
            return new LocalizacaoDTO(lat, lng);
        } catch (RestClientException e) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Falha ao acessar Google Geocoding", e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao processar resposta do Google Geocoding", e);
        }
    }

    private String n(String s) { return s == null ? "" : s.trim(); }
}
