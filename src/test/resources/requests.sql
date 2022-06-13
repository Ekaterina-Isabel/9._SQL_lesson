INSERT INTO users (id, login, password)
VALUES (1, "vasya", "password");

INSERT INTO users (id, login, password)
VALUES (2, "petya", "password");

INSERT INTO cards (id, user_id, number, balance_in_kopecks)
VALUES (1, 1, "5559000000000001", 1000000),
       (2, 1, "5559000000000002", 1000000);

INSERT INTO card_transactions(source, target, amount_in_kopecks)
VALUES ("5559000000000001", "5559000000000002", 10000);

UPDATE cards
SET balance_in_kopecks = balance_in_kopecks - 10000
WHERE number = "5559000000000001";

UPDATE cards
SET balance_in_kopecks = balance_in_kopecks + 10000
WHERE number = "5559000000000002";

DELETE FROM auth_code WHERE created < NOW() - INTERVAL 5 MINUTE;

delete from users where id=1;

select * from users;
select * from cards;
select * from card_transactions;

-- выборки всех столбцов и всех строк из таблицы users (осторожно на больших таблицах)
SELECT * FROM users;
-- выборка только определенных столбцов
SELECT id, login FROM users;
-- выборка по условию
SELECT balance_in_kopecks FROM cards WHERE number = "5559000000000002";
-- вычисляемые столбцы
SELECT balance_in_kopecks / 100 AS balance_in_rub FROM cards
WHERE number = "5559000000000002";

-- группируем по всей таблице
SELECT max(cards.balance_in_kopecks) FROM cards;
-- сначала фильтруем по user_id, потом группируем
SELECT sum(balance_in_kopecks) FROM cards WHERE user_id = 1;

-- группируем по user_id (количество карт каждого пользователя)*
-- важно: будут посчитаны карты в привязке к пользователю (т.е. petya не отобразится)
SELECT count(*) FROM cards GROUP BY user_id;