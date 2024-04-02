create database library;

use library;

CREATE TABLE ebook (
                       id INT NOT NULL AUTO_INCREMENT,
                       title VARCHAR(225) NOT NULL,
                       author VARCHAR(225) NOT NULL,
                       genre VARCHAR(225),
                       link VARCHAR(225) NOT NULL,
                       PRIMARY KEY (id)
);

CREATE TABLE location (
                          id INT NOT NULL AUTO_INCREMENT,
                          shelf_name VARCHAR(225) NOT NULL,
                          PRIMARY KEY (id)
);

CREATE TABLE reader (
                        id INT NOT NULL AUTO_INCREMENT,
                        deleted BIT(1),
                        first_name VARCHAR(225) NOT NULL,
                        surname VARCHAR(225) NOT NULL,
                        email VARCHAR(225) NOT NULL,
                        address VARCHAR(225),
                        phone_number VARCHAR(225),
                        PRIMARY KEY (id)
);

CREATE TABLE user (
                      id INT NOT NULL AUTO_INCREMENT,
                      username VARCHAR(225) NOT NULL,
                      password VARCHAR(225) NOT NULL,
                      authorities VARCHAR(225) NOT NULL,
                      reader_id INT,
                      PRIMARY KEY (id),
                      CONSTRAINT reader_id
                          FOREIGN KEY (reader_id)
                              REFERENCES reader (id)
                              ON DELETE SET NULL
);

CREATE TABLE reservation (
                             id INT NOT NULL AUTO_INCREMENT,
                             create_date DATETIME,
                             last_modified DATETIME,
                             deleted BIT(1),
                             id_reader INT,
                             PRIMARY KEY (id),
                             CONSTRAINT id_reader
                                 FOREIGN KEY (id_reader)
                                     REFERENCES reader (id)
);

CREATE TABLE book (
                      isbn INT NOT NULL AUTO_INCREMENT,
                      title VARCHAR(225) NOT NULL,
                      author VARCHAR(225) NOT NULL,
                      genre VARCHAR(225) NOT NULL,
                      copies INT NOT NULL,
                      copies_available INT NOT NULL,
                      location_id INT,
                      PRIMARY KEY (isbn),
                      CONSTRAINT location_id
                          FOREIGN KEY (location_id)
                              REFERENCES location (id)
);

CREATE TABLE book_reservation (
                                  id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                  create_date DATETIME,
                                  last_modified DATETIME,
                                  return_date DATETIME,
                                  deleted BIT(1),
                                  changed INT,
                                  status VARCHAR(225),
                                  book_id INT NOT NULL,
                                  reservation_id INT NOT NULL,
                                  CONSTRAINT book_id
                                      FOREIGN KEY (book_id)
                                          REFERENCES book (isbn),
                                  CONSTRAINT reservation_id
                                      FOREIGN KEY (reservation_id)
                                          REFERENCES reservation (id)
);