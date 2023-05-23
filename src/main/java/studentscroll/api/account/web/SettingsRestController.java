package studentscroll.api.account.web;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.val;
import studentscroll.api.account.data.Account;
import studentscroll.api.account.services.SettingsService;
import studentscroll.api.account.web.dto.SettingsResponse;
import studentscroll.api.account.web.dto.UpdateSettingsRequest;
import studentscroll.api.shared.NotAuthenticatedException;

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
  public SettingsResponse read() throws NotAuthenticatedException {
    return new SettingsResponse(SettingsService.read(getCurrentStudent()));
  }

  @Operation(summary = "Update your settings.")
  @ApiResponse(responseCode = "200", description = "Updated your settings.")
  @SecurityRequirement(name = "token")
  @PutMapping
  public SettingsResponse update(
      @RequestBody UpdateSettingsRequest request) throws NotAuthenticatedException {
    return new SettingsResponse(SettingsService.update(
        getCurrentStudent(),
        Optional.ofNullable(request.getNewTheme()),
        Optional.ofNullable(request.getNewLocale())));
  }

  private Account getCurrentStudent() throws NotAuthenticatedException {
    val student = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    if (student == null)
      throw new NotAuthenticatedException("You are not logged in.");

    return student;
  }

}
