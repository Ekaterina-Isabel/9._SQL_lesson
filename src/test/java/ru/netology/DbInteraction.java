package ru.netology;

import com.github.javafaker.Faker;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class DbInteraction {
    @BeforeEach
    @SneakyThrows
    void setUp() {
        var faker = new Faker();
        // запрос пишется в строковой переменной, ? - шаблон запроса, можно подставлять значения
        String dataSQL = "INSERT INTO users(login, password) VALUES (?, ?);";

        // подставляем значения в шаблон запроса вместо ? и обновляем
        try (
                // в метод getConnection передаем 3 параметра: строка из проперти,
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/app-db", "app-user", "pass");
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
        var countSQL = "SELECT COUNT(*) FROM users;";
        var cardsSQL = "SELECT id, number, balance_in_kopecks FROM cards WHERE user_id = ?;";

        try (
                var conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/app", "app", "pass"
                );
                var countStmt = conn.createStatement();
                var cardsStmt = conn.prepareStatement(cardsSQL);
                ) {
            try (var rs = countStmt.executeQuery(countSQL)) {
                if (rs.next()) {
                    // выборка значения по индексу столбца (нумерация с 1)
                    var count = rs.getInt(1);
                    // TODO: использовать
                    System.out.println(count);
                }
            }

            cardsStmt.setInt(1, 1);
            try (var rs = cardsStmt.executeQuery()) {
                while (rs.next()) {
                    var id = rs.getInt("id");
                    var number = rs.getString("number");
                    var balanceInKopecks = rs.getInt("balance_in_kopecks");
                    // TODO: сложить все в список
                    System.out.println(id + " " + balanceInKopecks);
                }
            }
        }
    }
}
