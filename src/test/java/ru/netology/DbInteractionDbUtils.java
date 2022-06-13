package ru.netology;

import com.github.javafaker.Faker;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;

public class DbInteractionDbUtils {
    @BeforeEach
    @SneakyThrows
    void setUp() {
        var faker = new Faker();
        // запрос пишется в строковой переменной, ? - шаблон запроса, можно подставлять значения
        String dataSQL = "INSERT INTO users(login, password) VALUES (?, ?);";

        // подставляем значения в шаблон запроса вместо ? и обновляем
        try (
                // в метод getConnection передаем 3 параметра: строка из проперти,
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/app-db", "user", "pass");
                // PreparedStatement - подготовленное состояние, загружаем в метод наш шаблон ничего не подставляя, т.е. подготовили его
                PreparedStatement preparedStatement = conn.prepareStatement(dataSQL);
        ) {
            // устанавливаем вместо первого ? какое-то значение
            preparedStatement.setString(1, faker.name().username());
            // устанавливаем вместо второго ? какое-то значение
            preparedStatement.setString(2, "password");
            preparedStatement.executeUpdate();      // выполяняем Update

            preparedStatement.setString(1, faker.name().username());
            preparedStatement.setString(2, "password");
            preparedStatement.executeUpdate();
        }
    }

    @Test
    @SneakyThrows
    void stubTest() {
        // верни количество записей в таблице users
        String countSQL = "SELECT COUNT(*) FROM users;";
        String cardsSQL = "SELECT id, number, balance_in_kopecks FROM cards WHERE user_id = ?;";

        try (
                // создания Connection, т.е. подключение к БД
                Connection conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/app-db", "user", "pass"
                );
                // подготовка состояни: Statement - выполнение не параметризованного запроса
                Statement statement = conn.createStatement();
                // в PreparedStatement загружаем шаблон запроса по знаком ?
                PreparedStatement preparedStatement = conn.prepareStatement(cardsSQL);
        ) {
            // ResultSet - специальная сущность для работы с БД, для обработки результатов запросов SELECT
            // передаем обычный не параметризированные запрос
            try (ResultSet rs = statement.executeQuery(countSQL)) {
                // понятие курсор - при обработке строк запросов в Java есть собстенный курсор, который она двигает
                // метод next - типа цикла for, проверяет есть ли строка, перед которой стоит курсор?
                // если существует, то возвращает true и переносит курсор на эту строку для обработки значений и т.д. пока не закончятся строки
                if (rs.next()) {        // проверка, сущестует ли строчка? удачно ли выполнился запрос
                    // выборка значения по индексу столбца (нумерация с 1)
                    var count = rs.getInt(1);       // получить значение из поля 1
                    // TODO: использовать
                    System.out.println(count);      // вывод в консоль
                }
            }

            preparedStatement.setInt(1, 1);
            try (var rs = preparedStatement.executeQuery()) {
                // while - цикл, т.к. мы знаем, что будет несколько строк, не одна
                // пока будет следующа строчка цикл будет проходить и обрабатывать поля
                while (rs.next()) {
                    // последовательно берем значения из полей: id, number, balance_in_kopecks таблицы cards
                    var id = rs.getInt("id");
                    var number = rs.getString("number");
                    var balanceInKopecks = rs.getInt("balance_in_kopecks");
                    // TODO: сложить все в список
                    // полученные значения вывести в консоль
                    System.out.println(id + " " + balanceInKopecks);
                }
            }
        }
    }
}
