CREATE TABLE IF NOT EXISTS _user
(
    id         SERIAL PRIMARY KEY,
    email      varchar not null,
    first_name varchar not null,
    last_name  varchar not null,
    password   varchar not null,
    username   varchar not null,
    UNIQUE (email, username)
);

INSERT INTO _user
values (email: "example@example.com",
        first_name: "testFirstname",
        last_name: "testFirstname",
        password: "secretPassword",
        username: "testUser"
        );