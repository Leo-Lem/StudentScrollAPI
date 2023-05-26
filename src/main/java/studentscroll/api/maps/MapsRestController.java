package studentscroll.api.maps;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import studentscroll.api.maps.dto.APIKeyResponse;

@Tag(name = "Maps")
@SecurityRequirement(name = "token")
@RestController
@RequestMapping("/maps")
public class MapsRestController {

  @Value("${googlemaps.apikey}")
  private String apiKey;

  @Operation(summary = "Retrieve the Google Maps API key.")
  @ApiResponse(responseCode = "200", description = "Returning the API key.")
  @GetMapping
  public APIKeyResponse read() {
    return new APIKeyResponse(apiKey);
  }

}
