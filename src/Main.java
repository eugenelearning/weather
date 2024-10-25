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

        Weather weather = new Weather(apiKey);

        weather.printTemperature();
        weather.printRawResponse();
        weather.printAverageTemp(DayPart.day, 3);
    }
}