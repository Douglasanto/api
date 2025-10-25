package com.apiIc.api.controllers;

import com.apiIc.api.dto.GeocodingRequestDTO;
import com.apiIc.api.dto.LocalizacaoDTO;
import com.apiIc.api.dto.NearbyPlaceDTO;
import com.apiIc.api.services.GoogleMapsService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GeolocationController {

    private final GoogleMapsService googleMapsService;

    public GeolocationController(GoogleMapsService googleMapsService) {
        this.googleMapsService = googleMapsService;
    }

    @GetMapping("/locais-proximos")
    public ResponseEntity<List<NearbyPlaceDTO>> buscarLocaisProximos(
            @RequestParam("latitude") double latitude,
            @RequestParam("longitude") double longitude,
            @RequestParam(value = "tipo", required = false) String tipo
    ) {
        List<NearbyPlaceDTO> locais = googleMapsService.buscarLocaisProximos(latitude, longitude, tipo);
        return ResponseEntity.ok(locais);
    }

    @PostMapping("/geolocalizacao")
    public ResponseEntity<LocalizacaoDTO> geolocalizar(@Valid @RequestBody GeocodingRequestDTO request) {
        LocalizacaoDTO coords = googleMapsService.geocodificarEndereco(request);
        return ResponseEntity.ok(coords);
    }
}
