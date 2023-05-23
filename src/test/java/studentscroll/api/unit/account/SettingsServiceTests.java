package studentscroll.api.unit.account;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import lombok.val;
import studentscroll.api.account.data.Settings;
import studentscroll.api.account.data.Account;
import studentscroll.api.account.data.AccountRepository;
import studentscroll.api.account.services.SettingsService;
import studentscroll.api.utils.TestUtils;

public class SettingsServiceTests {

  @Mock
  private AccountRepository repo;

  @InjectMocks
  private SettingsService service;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void givenIsAuthenticated_whenReadingSettings_thenReturnsCorrectSettings() {
    val settings = TestUtils.getSettings();
    val student = TestUtils.getStudent(1L).setSettings(settings);

    assertEquals(settings, service.read(student));
  }

  @Test
  public void givenIsAuthenticated_whenUpdatingSettings_thenReturnsCorrectSettings() {
    val settings = TestUtils.getSettings();
    val student = TestUtils.getStudent(1L).setSettings(settings);

    TestUtils.authenticate(student);

    when(repo.save(any(Account.class)))
        .thenAnswer(i -> i.getArguments()[0]);

    Settings newSettings = service.update(
        student,
        Optional.of("DARK"),
        Optional.empty());

    assertNotEquals("LIGHT", newSettings.getTheme());
    assertEquals("DE", newSettings.getLocale());
  }

}
