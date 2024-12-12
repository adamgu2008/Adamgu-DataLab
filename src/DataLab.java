
import core.data.*;
import java.util.ArrayList;

public class DataLab {
    public static void main(String[] args){
        DataSource ds = DataSource.connect("D:\\JavaWork\\DataLabProject\\src\\transportDelays.csv");
        ds.load();
        ds.printUsageString();

        ArrayList<RouteDelay> delays = ds.fetchList(RouteDelay.class, "route_id","trip_date","delay_minutes");

        ArrayList<RouteSum> summaries = new ArrayList<>();

        for(RouteDelay delay : delays){
            boolean exists = false;
            for(RouteSum summary : summaries){
                if(summary.route_id.equals(delay.route_id)){
                    summary.totalDelay += delay.delay_minutes;
                    exists = true;
                    break;
                }
            }
            if(!exists){
                summaries.add(new RouteSum(delay.route_id,delay.delay_minutes));
            }
        }
        int delayMax = 0;
        for(RouteSum summary : summaries){
            if(summary.totalDelay > delayMax)
                delayMax = summary.totalDelay;
        }

        System.out.println("Routes with highest delay(" + delayMax + " minutes): ");
        for(RouteSum summary : summaries){
            if(summary.totalDelay == delayMax)
                System.out.println(summary.route_id);
        }
    }
}
class RouteDelay {
    int delay_minutes;
    String route_id;
    String trip_date;

    public RouteDelay(String route, String date, int delay) {
        this.route_id = route;
        this.trip_date = date;
        this.delay_minutes = delay;
    }
}
class RouteSum {
    String route_id;
    int totalDelay;

    public RouteSum(String route, int totalDelay) {
        this.route_id = route;
        this.totalDelay = totalDelay;
    }
}