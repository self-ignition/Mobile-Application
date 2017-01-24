DROP TABLE step;
DROP TABLE ingredient;
DROP TABLE recipe;
DROP TABLE author;

CREATE TABLE author(
    name varchar(255) PRIMARY KEY
    );

CREATE TABLE recipe(
    title varchar(255) PRIMARY KEY,
    preptime varchar(255),
    cooktime varchar(255),
    author varchar(255) NOT NULL, 
    FOREIGN KEY recipe(author) REFERENCES author(name)
    );

CREATE TABLE step(
    step int not null AUTO_INCREMENT,
    title varchar(255),
    information text,
    PRIMARY KEY (step, title),
    FOREIGN KEY step(title) REFERENCES recipe(title)
    );

CREATE TABLE ingredient(
    num int not null AUTO_INCREMENT,
    title varchar(255),
    information text,
    PRIMARY KEY (num, title),
    FOREIGN KEY ingredient(title) REFERENCES recipe(title)
    );