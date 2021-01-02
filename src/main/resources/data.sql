INSERT INTO institution  (name, description, archive) VALUES ('Fundacja "Dbam o Zdrowie"', 'Cel i misja: Pomoc dzieciom z ubogich rodzin.', false);
INSERT INTO institution  (name, description, archive) VALUES ('Fundacja "A kogo"', 'Cel i misja: Pomoc wybudzaniu dzieci ze śpiączki.', false);
INSERT INTO institution  (name, description, archive) VALUES ('Fundacja “Dla dzieci"', 'Cel i misja:Pomoc osobom znajdującym się w trudnej sytuacji życiowej.', false);
INSERT INTO institution  (name, description, archive) VALUES ('Fundacja “Bez domu”', 'Cel i misja: Pomoc dla osób nie posiadających miejsca zamieszkania', false);
INSERT INTO donation (quantity, received) VALUES ('5', false);
INSERT INTO donation (quantity, received) VALUES ('15', false);

INSERT INTO category (name) VALUES ('ubrania, które nadają się do ponownego użycia');
INSERT INTO category (name) VALUES ('ubrania do wyrzucenia');
INSERT INTO category (name) VALUES ('książki');
INSERT INTO category (name) VALUES ('zabawki');
INSERT INTO category (name) VALUES ('inne');

INSERT INTO role (id,name) VALUES (1, 'ROLE_USER'), (2, 'ROLE_ADMIN');
INSERT INTO user_role (user_id, role_id)  VALUES (1, 2);
