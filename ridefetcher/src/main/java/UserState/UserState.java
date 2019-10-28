package UserState;

public class UserState {

    private String state;

    UserState(String state) {
        this.state = state;
    }

    void setState(String newState) {
        this.state = newState;
    }

    public String getState() {
        return this.state;
    }
}
