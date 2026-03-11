package nbcc.auth.service;

public interface LoginService {

    boolean isLoggedIn();

    boolean isLoggedOut();

    String getCurrentUsername();

}