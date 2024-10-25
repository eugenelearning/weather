import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Weather {
    private final String apiKey;
    private String rawResponse;
    private WeatherResponseModel responseModel;

    public Weather(String apiKey) {
        this.apiKey = apiKey;

        this.requestData();
    }

    private URI getApiURI() throws URISyntaxException {
        return new URI("https://api.weather.yandex.ru/v2/forecast?lat=52.37125&lon=4.89388");
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
        this.rawResponse = jsonString;
        this.responseModel = new Gson().fromJson(jsonString, WeatherResponseModel.class);
    }

    public void printRawResponse() {
        System.out.printf("Ответ API: %s\n", this.rawResponse);
    }

    public void printTemperature()  {
        System.out.printf("Сейчас за окошком: %s\n", this.responseModel.fact.temp);
    }

    public void printAverageTemp(DayPart dayPart, int limit)  {
        float avg = 0;
        int count = this.responseModel.forecasts.length;
        int safeLimit = Math.min(limit, count);

        for (int i = 0; i < safeLimit; i++) {
            WeatherResponseModel.Forecast forecast = this.responseModel.forecasts[i];

            avg += forecast.parts.get(DayPart.day).temp_avg;
        }

        System.out.printf("Средняя дневная температура (%s) за %d д: %.1f\n", dayPart, limit, avg / limit);
    }
}
