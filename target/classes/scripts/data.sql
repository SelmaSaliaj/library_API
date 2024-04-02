use library;

INSERT INTO ebook(id, title, author, link)
VALUES (1,"Organizimi dhe arkitektura e kompjuterave","Agim Cami",
        "https://books.google.al/books?id=Mif_AwAAQBAJ&printsec=frontcover&source=gbs_ge_summary_r&cad=0#v=onepage&q&f=true"),
       (2,"Arkitektura e kompjuterave","Agim Cami",
        "https://books.google.al/books?id=UQNsAwAAQBAJ&printsec=frontcover&source=gbs_ge_summary_r&cad=0#v=onepage&q&f=true");

INSERT INTO location(id, shelf_name)
VALUES (1,"drama"),
       (2,"classics"),
       (3,"thriller"),
       (4,"adventure"),
       (5,"fantasy"),
       (6,"manga"),
       (7,"romance"),
       (8,"sci fi"),
       (9,"history"),
       (10,"psychology"),
       (11,"children books"),
       (12,"mystery"),
       (13,"murder");

INSERT INTO reader(id, deleted, first_name, surname, email, address, phone_number)
VALUES (1, false, "Ana", "Bekteshi", "anabekteshi@gmail.com","Rruga 'Sami Frasheri'","0692732511"),
       (2, false, "Emiraldo", "Cakaj", "emiraldocakaj@gmail.com","Rruga 'Qemal Stafa'","0672261543"),
       (3, false, "Enkeleda", "Kalliri", "enkeledakalliri@gmail.com","Rruga 'Tasin Tafaj'","0683214692"),
       (4, false, "Klaudja", "Hoxha", "klaudjahoxha@gmail.com","Rruga 'Marie Logoreci'","0692772422"),
       (5, false, "Eraldo", "Hoxha", "eraldohoxha@gmail.com","Rruga 'Marie Logoreci'","0684059319"),
       (6, false, "Serxho", "Gjoni","serxhogjoni@gmail.com","Rruga 'Qemal Stafa'","0673162511"),
       (7, false, "Rosilda", "Agolli", "rosildaagolli@gmail.com","Rruga 'Sami Frasheri'","0694077402");

INSERT INTO user
VALUES (1,"anabekteshi","$2a$10$KFzLYHuebPxa2q/HAoSYRulNScaRmA1ErImOV0R4mUULWAkTek2Gm","USER",1),
       (2,"emiraldocakaj","$2a$10$KFzLYHuebPxa2q/HAoSYRulNScaRmA1ErImOV0R4mUULWAkTek2Gm","USER",2),
       (3,"enkeledakalliri","$2a$10$KFzLYHuebPxa2q/HAoSYRulNScaRmA1ErImOV0R4mUULWAkTek2Gm","USER",3),
       (4,"klaudjahoxha","$2a$10$KFzLYHuebPxa2q/HAoSYRulNScaRmA1ErImOV0R4mUULWAkTek2Gm","USER",4),
       (5,"eraldohoxha","$2a$10$KFzLYHuebPxa2q/HAoSYRulNScaRmA1ErImOV0R4mUULWAkTek2Gm","USER",5),
       (6,"serxhogjoni","$2a$10$KFzLYHuebPxa2q/HAoSYRulNScaRmA1ErImOV0R4mUULWAkTek2Gm","USER",6),
       (7,"rosildaagolli","$2a$10$KFzLYHuebPxa2q/HAoSYRulNScaRmA1ErImOV0R4mUULWAkTek2Gm","USER",7);

INSERT INTO user(id, username, password, authorities)
VALUES (8,"leonardomezini","$2a$10$4KZkw2qHxG4uanOF5Vpcxu.UP5a1244p/9uHuOHrYzs2fdtmKVYl2","ADMIN"),
       (9,"antonelamalaj","$2a$10$4KZkw2qHxG4uanOF5Vpcxu.UP5a1244p/9uHuOHrYzs2fdtmKVYl2","ADMIN");

INSERT INTO reservation(id, create_date, last_modified, deleted, id_reader)
VALUES (1,'2023-04-16','2023-04-16',false,1),
       (2,'2023-08-10','2023-08-12',false,1),
       (3,'2023-11-06','2023-11-06',false,3),
       (4,'2023-11-12','2023-11-12',false,2),
       (5,'2023-11-12','2023-11-16',false,5),
       (6,'2023-11-12','2023-11-12',false,3);

INSERT INTO book(isbn, title, author, genre, copies, copies_available, location_id)
VALUES (1,"Sepse te dua","Guillaume Musso", "mistery, romance, psychology" ,2, 1 , 12),
       (2,"Kokoro", "Natsume Soseki", "romance", 1, 0, 7),
       (3,"Dy vjet pushime","Zhyl Vern","kids book, adventure", 3, 2, 11),
       (4,"Princi i lumtur","Oscar Wilde","kids book", 2, 2, 11),
       (5,"Krim dhe ndeshkim","Fjodor Dostojevskij", "murder, mystery, psychology",4, 2, 13);

INSERT INTO book_reservation(id, create_date, last_modified, return_date, deleted, status, book_id, reservation_id,changed)
VALUES (1,'2023-04-16','2023-04-16','2023-04-26',false, "RETURNED", 4, 1, 1),
       (2,'2023-04-16','2023-04-16','2023-04-26',false, "RETURNED", 2, 1, 0),
       (3,'2023-04-16','2023-04-16','2023-04-26',false, "RETURNED", 1, 1, 1),
       (4,'2023-08-10','2023-08-10','2023-08-20',false, "RETURNED", 4, 2, 0),
       (5,'2023-08-10','2023-08-12','2023-08-19',false, "RETURNED", 2, 2, 0),
       (6,'2023-11-06','2023-11-06','2023-11-12',false, "RETURNED", 3, 3, 0),
       (7,'2023-11-12','2023-11-12','2023-11-19',false, "RETURNED", 1, 4, 1),
       (8,'2023-11-12','2023-11-12','2023-11-26',false, "RESERVED", 2, 5, 0),
       (9,'2023-11-12','2023-11-12','2023-11-26',false, "RESERVED", 3, 5, 0),
       (10,'2023-11-12','2023-11-16','2023-11-30',false, "RETURNED", 5, 5, 3),
       (11,'2023-11-12','2023-11-12','2023-11-26',false, "POSTPONED", 1, 6, 2),
       (12,'2023-11-12','2023-11-12','2023-11-26',false, "RESERVED", 5, 6, 0);
