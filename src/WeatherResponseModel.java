import java.util.HashMap;

public class WeatherResponseModel {
    static class Source {
        public float temp;
    }

    static class ForecastItem {
        public float temp_avg;
    }

    static class Forecast {
        public String date;
        public HashMap<DayPart, ForecastItem> parts = new HashMap<>();
    }

    public Forecast[] forecasts;
    public Source fact;
}
