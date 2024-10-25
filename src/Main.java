import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Нам нужен ваш апи ключ, одежда и мотоцикл...\n");
        Scanner input = new Scanner(System.in);
        String apiKey = input.nextLine();

        if (apiKey.trim().isEmpty()) {
            System.out.println("Без ключа не полетит :(");
            return;
        }

        Weather weather = new Weather(apiKey, 52.37125, 4.89388);

        weather.printTemperature();
        weather.printAverageTemp(DayPart.day, 3);
        weather.printRawResponse();
    }
}