package UserState;

public class UserState {

    private String state;
    private String startPoint;
    private String finishPoint;
    private int[] tramRoutes;

    UserState(String state) {
        this.state = state;
    }

    void setState(String newState) {
        this.state = newState;
    }
    public String getState() {
        return this.state;
    }

    void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }

    public String getStartPoint() {
        return this.startPoint;
    }

    void setFinishPoint(String finishPoint) {
        this.finishPoint = finishPoint;
    }

    public String getFinishPoint() {
        return this.finishPoint;
    }


    void setTramRoutes(int[] tramRoutes) {
        this.tramRoutes = tramRoutes;
    }

    public String getReadableTramRoutes() {
        return this.tramRoutes.toString();
    }
}
