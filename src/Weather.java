import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Locale;

public class Weather {
    private final String entrypointUrl = "https://api.weather.yandex.ru/v2/forecast";
    private final String apiKey;
    private final double lat;
    private final double lon;
    private String rawResponse;
    private WeatherResponseModel responseModel;

    public Weather(String apiKey, double lat, double lon) {
        this.apiKey = apiKey;
        this.lat = lat;
        this.lon = lon;

        this.requestData();
    }

    private URI getApiURI() throws URISyntaxException {
        return new URI(String.format(Locale.US, "%s?lat=%f&lon=%f", this.entrypointUrl, this.lat, this.lon));
    }

    private HttpRequest createRequest() throws URISyntaxException {
        return HttpRequest.newBuilder()
                .uri(this.getApiURI())
                .header("X-Yandex-Weather-Key", this.apiKey)
                .GET()
                .build();
    }

    private void requestData() {
        try {
            HttpRequest request = this.createRequest();

            HttpResponse<String> response = HttpClient.newBuilder()
                    .build()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            this.proceedResponse(response.body());
        } catch (IOException | InterruptedException | URISyntaxException e) {
            System.out.println("Что-то пошло не так...");
        }
    }

    private void proceedResponse(String jsonString) {
        System.out.println(jsonString);
        this.rawResponse = jsonString;
        this.responseModel = new Gson().fromJson(jsonString, WeatherResponseModel.class);
    }

    public void printRawResponse() {
        System.out.printf("Ответ API: %s\n", this.rawResponse);
    }

    public void printTemperature()  {
        System.out.printf(Locale.US, "Сейчас за окошком: %s\n", this.responseModel.fact.temp);
    }

    public void printAverageTemp(DayPart dayPart, int limit)  {
        float avg = 0;
        int count = this.responseModel.forecasts.length;
        int safeLimit = Math.min(limit, count);

        for (int i = 0; i < safeLimit; i++) {
            WeatherResponseModel.Forecast forecast = this.responseModel.forecasts[i];

            avg += forecast.parts.get(DayPart.day).temp_avg;
        }

        System.out.printf(Locale.US, "Средняя дневная температура (%s) за %d д: %.1f\n", dayPart, limit, avg / limit);
    }
}
