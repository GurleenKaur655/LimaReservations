package nbcc.common.viewmodel;

public class NavViewModel {

    private final String username;

    private final boolean showLoginLink;
    private final boolean showLogoutButton;
    private final boolean showRegisterLink;
    private final boolean showUserName;
    private final boolean showTables;
    private final boolean showEvent;
    private final boolean showSeating;
    private final boolean showMenu;


    public NavViewModel(boolean isLoggedIn, String username) {

        this.username = username;
        this.showUserName = username != null && !username.isBlank();

        this.showLoginLink = !isLoggedIn;
        this.showLogoutButton = isLoggedIn;
        this.showRegisterLink = !isLoggedIn;
        this.showSeating = isLoggedIn;
        this.showTables = isLoggedIn;
        this.showEvent = isLoggedIn;
        this.showMenu = isLoggedIn;
    }

    public boolean isShowUserName() {
        return showUserName;
    }

    public String getUsername() {
        return username;
    }

    public boolean isShowLoginLink() {
        return showLoginLink;
    }

    public boolean isShowLogoutButton() {
        return showLogoutButton;
    }

    public boolean isShowRegisterLink() {
        return showRegisterLink;
    }

    public boolean isShowTables() {
        return showTables;
    }

    public boolean isShowEvent() {
    return showEvent; }

    public boolean isShowSeating() {
        return showSeating;
    }

    public boolean isShowMenu() {
        return showMenu;
    }
}
