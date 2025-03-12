insert into USERS(id, username, password, role)
    values (100, 'rldias@gmail.com', '$2a$12$dLfEmdLUetxkl0eTNEzMNu7KNFMb8bmLWmTXWS/R4cERL8FTKFJfe', 'ROLE_ADMIN');
insert into USERS(id, username, password, role)
    values (101, 'leafar@gmail.com', '$2a$12$dLfEmdLUetxkl0eTNEzMNu7KNFMb8bmLWmTXWS/R4cERL8FTKFJfe', 'ROLE_CLIENT');
insert into USERS(id, username, password, role)
    values (102, 'rralmeida@gmail.com', '$2a$12$dLfEmdLUetxkl0eTNEzMNu7KNFMb8bmLWmTXWS/R4cERL8FTKFJfe', 'ROLE_CLIENT');
insert into USERS(id, username, password, role)
    values (103, 'rnferreira@gmail.com', '$2a$12$dLfEmdLUetxkl0eTNEzMNu7KNFMb8bmLWmTXWS/R4cERL8FTKFJfe', 'ROLE_CLIENT');
insert into USERS(id, username, password, role)
    values (104, 'tiguan@gmail.com', '$2a$12$dLfEmdLUetxkl0eTNEzMNu7KNFMb8bmLWmTXWS/R4cERL8FTKFJfe', 'ROLE_CLIENT');


insert into CLIENTS (id, name, cpf, user_id) values (5, 'Rafael Santos', '00992999090', 101);
insert into CLIENTS (id, name, cpf, user_id) values (6, 'Roberto Santos', '99106729010', 102);

insert into parking_spots(id, code, status) values (100, 'A-01', 'AVAILABLE');
insert into parking_spots(id, code, status) values (200, 'A-02', 'AVAILABLE');
insert into parking_spots(id, code, status) values (300, 'A-03', 'OCCUPIED');
insert into parking_spots(id, code, status) values (400, 'A-04', 'AVAILABLE');


insert into ClientSpot(receipt_number, plate, brand, model, color, entry_date, id_client, id_parking_spots)
    values ('20250311-095300', 'HON-4079', 'HONDA', 'GREEN', '2025-03-11 09:57:00', 5 , 100);
insert into ClientSpot(receipt_number, plate, brand, model, color, entry_date, id_client, id_parking_spots)
     values ('20250311-095300', 'FIT-8021', 'FIAT', 'RED', '2025-03-11 09:57:00', 6 , 200);
insert into ClientSpot(receipt_number, plate, brand, model, color, entry_date, id_client, id_parking_spots)
    values ('20250311-095300', 'HON-4079', 'HONDA', 'GREEN', '2025-03-11 09:57:00', 5 , 300);