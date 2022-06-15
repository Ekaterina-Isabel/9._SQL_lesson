package ru.netology;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
// класс модель, поля соответствуют полям запроса
public class User {
    private int id;
    private String login;
    private String password;
}
