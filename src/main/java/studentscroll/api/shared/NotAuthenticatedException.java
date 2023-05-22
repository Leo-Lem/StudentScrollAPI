package studentscroll.api.shared;

public class NotAuthenticatedException extends Throwable {
  public NotAuthenticatedException(String message) {
    super(message);
  }
}
