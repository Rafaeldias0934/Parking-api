-- Insere usuários
insert into users(username, password, role)
values ('admin@gmail.com', '$2a$12$RLpfTQK6PwTVlRxja/N7oOFoz5TWvS/9QL6P3aHhBrEwxTj3f0eHu', 'ROLE_ADMIN');

insert into users(username, password, role)
values ('leafar@gmail.com', '$2a$12$dLfEmdLUetxkl0eTNEzMNu7KNFMb8bmLWmTXWS/R4cERL8FTKFJfe', 'ROLE_CLIENT');

insert into users(username, password, role)
values ('rralmeida@gmail.com', '$2a$12$dLfEmdLUetxkl0eTNEzMNu7KNFMb8bmLWmTXWS/R4cERL8FTKFJfe', 'ROLE_CLIENT');

-- Insere clientes, assumindo que IDs são auto gerados, então vamos pegar o ID pelo username
insert into clients(id, name, cpf, user_id) values (1, 'Rafael Santos', '00992999090', (select id from users where username = 'leafar@gmail.com'));
insert into clients(id, name, cpf, user_id) values (2, 'Roberto Santos', '99106729010', (select id from users where username = 'rralmeida@gmail.com'));

-- Insere vagas de estacionamento
insert into parking_spots(id, code, status) values (1, 'A-01', 'OCCUPIED');
insert into parking_spots(id, code, status) values (2, 'A-02', 'OCCUPIED');
insert into parking_spots(id, code, status) values (3, 'A-03', 'AVAILABLE');
insert into parking_spots(id, code, status) values (4, 'A-04', 'AVAILABLE');

-- Insere registros de estacionamento dos clientes
insert into clients_spots(receipt_number, plate, brand, model, color, entry_date, id_client, id_parking_spots)
values ('20250311-100000', 'FIT-8021', 'FIAT', 'YAMAHA', 'RED', '2025-03-11 09:57:00', 2, 1);

insert into clients_spots(receipt_number, plate, brand, model, color, entry_date, id_client, id_parking_spots)
values ('20250311-095300', 'FIT-8021', 'FIAT', 'YAMAHA', 'RED', '2025-03-11 09:57:00', 1, 2);
