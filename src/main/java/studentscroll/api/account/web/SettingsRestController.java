package studentscroll.api.account.web;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import studentscroll.api.account.services.SettingsService;
import studentscroll.api.account.web.dto.SettingsResponse;
import studentscroll.api.account.web.dto.UpdateSettingsRequest;

@Tag(name = "Settings", description = "Everything related to your settings.")
@RestController
@RequestMapping("/account/settings")
public class SettingsRestController {

  @Autowired
  private SettingsService SettingsService;

  @Operation(summary = "Find your settings.")
  @ApiResponse(responseCode = "200", description = "Found your settings.")
  @SecurityRequirement(name = "token")
  @GetMapping
  public SettingsResponse read() throws EntityNotFoundException {
    return new SettingsResponse(SettingsService.read());
  }

  @Operation(summary = "Update your settings.")
  @ApiResponse(responseCode = "200", description = "Updated your settings.")
  @SecurityRequirement(name = "token")
  @PutMapping
  public SettingsResponse update(
      @RequestBody UpdateSettingsRequest request) throws EntityNotFoundException {
    return new SettingsResponse(SettingsService.update(
        Optional.ofNullable(request.getNewTheme()),
        Optional.ofNullable(request.getNewLocale())));
  }
}
