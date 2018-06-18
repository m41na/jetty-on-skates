DROP TABLE IF EXISTS tbl_users;
CREATE TABLE tbl_users (
    username VARCHAR(45) NOT NULL,
    password VARCHAR(128) NULL,
    firstname VARCHAR(45) NOT NULL,
    lastname VARCHAR(45) NULL,
    email VARCHAR(64) NULL,
    address VARCHAR(64) NULL,
    phone VARCHAR(12) NULL,
    PRIMARY KEY(username)
);
