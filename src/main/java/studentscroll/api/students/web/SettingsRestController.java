package studentscroll.api.students.web;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import studentscroll.api.students.services.SettingsService;
import studentscroll.api.students.web.dto.*;

@Tag(name = "Settings", description = "Everything related to a student's settings.")
@RestController
@RequestMapping("/students/{studentId}/settings")
public class SettingsRestController {

  @Autowired
  private SettingsService SettingsService;

  @Operation(summary = "Find settings of the student.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Found the settings."),
      @ApiResponse(responseCode = "404", description = "Student does not exist.", content = @Content) })
  @SecurityRequirement(name = "token")
  @GetMapping
  public SettingsResponse readSettings(
      @PathVariable Long studentId) throws EntityNotFoundException {
    return new SettingsResponse(SettingsService.read(studentId));
  }

  @Operation(summary = "Update settings of the student.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Updated the settings."),
      @ApiResponse(responseCode = "404", description = "Student does not exist.", content = @Content) })
  @SecurityRequirement(name = "token")
  @PutMapping
  public SettingsResponse updateSettings(
      @PathVariable Long studentId, @RequestBody UpdateSettingsRequest request) throws EntityNotFoundException {
    return new SettingsResponse(SettingsService.update(
        studentId,
        Optional.ofNullable(request.getNewTheme()),
        Optional.ofNullable(request.getNewLocale()),
        Optional.ofNullable(request.getNewIsLocated())));
  }
}
