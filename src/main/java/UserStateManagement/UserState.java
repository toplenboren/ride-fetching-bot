package UserStateManagement;

import jodd.json.JsonSerializer;

import java.util.ArrayList;
import java.util.List;

public class UserState {

    private String state;
    private String startPoint = "";
    private List<Integer> tramRoutes = new ArrayList<>();

    UserState() {
        this.state = "INIT";
    }

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

    public void setTramRoutes(List<Integer> tramRoutes) {
        this.tramRoutes = tramRoutes;
    }

    public List<Integer> getTramRoutes() {
        return this.tramRoutes;
    }

    public String viewReadableTramRoutes() {
        if (this.tramRoutes.isEmpty()) {
            return "";
        }
        return this.tramRoutes.toString();
    }

    public String[] viewTramRoutesAsStrings() {
        List<String> result = new ArrayList<>();
        for (int number : this.tramRoutes) {
            result.add(String.valueOf(number));
        }
        return result.toArray(new String[0]);
    }

    public boolean isReadyToFetch() {
        return !this.startPoint.equals("") &&
                !this.tramRoutes.isEmpty();
    }

    public String serialize() {
        return JsonSerializer.create().deep(true).serialize(this);
    }
}
