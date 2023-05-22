package studentscroll.api.unit.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;

import org.junit.jupiter.api.Test;

import lombok.val;
import studentscroll.api.account.data.Account;
import studentscroll.api.security.JSONWebToken;

@SuppressWarnings("JavaUtilDate")
public class JSONWebTokenTests {

  @Test
  public void givenUserDetailsAreValid_whenGeneratingJWT_thenReturnsJWTWithCorrectEmail() {
    val email = "abc@xyz.com";
    val details = new Account(email, "");
    val token = JSONWebToken.generateFrom(details);

    assertEquals(email, token.getUsername());
  }

  @Test
  public void givenJWTIsCreated_whenGettingExpirationDate_thenDateReflectsExpirationInterval() {
    val details = new Account("xxx", "");
    val token = JSONWebToken.generateFrom(details);

    val expirationInterval = JSONWebToken.EXPIRES_AFTER_SECONDS;
    val expirationDate = token.getExpirationDate();

    // slight adjustment because current date will be later than JWT creation date
    assertTrue(expirationDate.after(new Date(System.currentTimeMillis() - 9999 + expirationInterval * 1000)));
  }

  @Test
  public void givenJWTIsValid_whenValidatingJWT_thenReturnsTrue() {
    val details = new Account("xxx", "");
    val token = JSONWebToken.generateFrom(details);

    assertTrue(token.validate(details));
  }

  @Test
  public void givenJWTIsInvalid_whenValidatingJWT_thenReturnsFalse() {
    val details = new Account("xxx", "");
    val otherDetails = new Account("yyy", "");
    val token = JSONWebToken.generateFrom(details);

    assertFalse(token.validate(otherDetails));
  }

}
