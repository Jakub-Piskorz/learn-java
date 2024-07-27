create table game(
    id int auto_increment,
    name varchar(255) NOT NULL,
    slug varchar(255) NOT NULL,
    description varchar(1023) NOT NULL,
    publisher varchar(255) NOT NULL
);



--insert into game(name, slug, description, publisher)
--    values('Spring boot simulator', 'spring-sim', 'I don''t know, some made-up game.', 'Jakub Piskorz IT Solutions');