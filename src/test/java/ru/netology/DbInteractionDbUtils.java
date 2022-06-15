package ru.netology;

import com.github.javafaker.Faker;
import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.List;

public class DbInteractionDbUtils {
    @BeforeEach
    @SneakyThrows
    void setUp() {
        var faker = new Faker();
        QueryRunner runner = new QueryRunner();
        var dataSQL = "INSERT INTO users(login, password) VALUES (?, ?);";

        try (
                // создаем подключение
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/app-db", "user", "pass");
        ) {
            // обычная вставка
            // выполяем запросы через QueryRunner, update - выполяем запрос INSERT,
            // передаем Connection, шаблон запроса и то количество параметров сколько ? знаков в шаблоне; 2 раза выполяем INSERT
            // update - выполняет запросы на изменение, т.е. INSERT, UPDATE
            runner.update(conn, dataSQL, faker.name().username(), "pass1");
            runner.update(conn, dataSQL, faker.name().username(), "pass2");
        }
    }

    @Test
    @SneakyThrows
    void stubTest() {
        var countSQL = "SELECT COUNT(*) FROM users;";
        var usersSQL = "SELECT * FROM users;";
        var runner = new QueryRunner();

        try (
                // создаем Connection
                Connection conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/app-db", "user", "pass");
        ) {
            // query - выполняет запрос и возвращает нам какой-то результат, т.е. SELECT
            // conn, countSQL, new ScalarHandler<>() - есть 3 типа результатов, которые мы можем получить
            // ScalarHandler - единичное значение (количество, строка, )
            // запрос на количество users
            Long count = runner.query(conn, countSQL, new ScalarHandler<>());
            System.out.println(count);

            // BeanHandler - получаем объект пользователь
            var first = runner.query(conn, usersSQL, new BeanHandler<>(User.class));
            System.out.println(first);

            // BeanListHandler - получаем список пользователей из БД
            List<User> all = runner.query(conn, usersSQL, new BeanListHandler<>(User.class));
            System.out.println(all);
        }
    }
}
