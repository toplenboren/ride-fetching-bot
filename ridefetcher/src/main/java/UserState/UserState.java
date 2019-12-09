package UserState;

import java.util.ArrayList;
import java.util.List;

public class UserState {

    private String state;
    private String startPoint = "";
    private String finishPoint = "";
    private List<Integer> tramRoutes = new ArrayList<>();

    UserState(String state) {
        this.state = state;
    }

    public void setState(String newState) {
        this.state = newState;
    }
    public String getState() {
        return this.state;
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }

    public String getStartPoint() {
        return this.startPoint;
    }

    public void setFinishPoint(String finishPoint) {
        this.finishPoint = finishPoint;
    }

    public String getFinishPoint() {
        return this.finishPoint;
    }


    public void setTramRoutes(List<Integer> tramRoutes) {
        this.tramRoutes = tramRoutes;
    }

    public String getReadableTramRoutes() {
        if (this.tramRoutes.isEmpty()) {
            return "";
        }
        return this.tramRoutes.toString();
    }

    public String[] getTramRoutesAsStrings() {
        List<String> result = new ArrayList<>();
        for (int number : this.tramRoutes) {
            result.add(String.valueOf(number));
        }
        return result.toArray(new String[0]);
    }

    public boolean isReadyToFetch() {
        return !this.startPoint.equals("") &&
//               !this.finishPoint.equals("") &&
                !this.tramRoutes.isEmpty();
    }
}
