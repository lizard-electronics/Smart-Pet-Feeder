/*
	Smart Pet Feeder, Populate
    
    Desenvolvido por:
    - Eduardo Silva, 202001449
    - Manuel Lagarto, 202002147
    - Rui colaço, 199100317
*/

-- -----------------------------------------------------

use projeto;

INSERT INTO Utilizador VALUES(null,'Eduardo','123',1,'911111111','edu@email.com');
INSERT INTO Utilizador VALUES(null,'Manuel','123',2,'922222222','Manuel@email.com');
INSERT INTO Utilizador VALUES(null,'Rui','123',1,'933333333','Rui@email.com');
INSERT INTO Utilizador VALUES(null,'Guilherme','123',1,'944444444','Guilherme@email.com');
INSERT INTO Utilizador VALUES(null,'Joao','123',1,'955555555','joao@email.com');
INSERT INTO Utilizador VALUES(null,'Célia Morais','123',1,'966666666','celia@email.com');
INSERT INTO Utilizador VALUES(null,'Nascimento Augusto','123',1,'977777777','nascimento@email.com');
INSERT INTO Utilizador VALUES(null,'Paulo Viegas','123',2,'988888888','paulo@email.com');
INSERT INTO Utilizador VALUES(null,'Florinda Simões','123',1,'999999999','Florinda@email.com');
INSERT INTO Utilizador VALUES(null,'Isabel Espada','123',1,'900000000','Isabel@email.com');
INSERT INTO Utilizador VALUES(null,'António Dias','123',2,'912345678','Antonio@email.com');
INSERT INTO Utilizador VALUES(null,'José António','123',1,'923456789','joser@email.com');
INSERT INTO Utilizador VALUES(null,'Joaquim Pires Lopes','123',1,'934567890','joaquim@email.com');
INSERT INTO Utilizador VALUES(null,'Ana Maria Fonseca','123',1,'945678901','ana@email.com');
INSERT INTO Utilizador VALUES(null,'Paula Antunes','123',2,'956789012','paula@email.com');
INSERT INTO Utilizador VALUES(null,'Joana Ramalho Silva','123',1,'967890123','joana@email.com');
INSERT INTO Utilizador VALUES(null,'Rui Manuel Silva','123',2,'978901234','rui@email.com');
INSERT INTO Utilizador VALUES(null,'João Paulo Santos','123',1,'989012345','joao@email.com');
INSERT INTO Utilizador VALUES(null,'Cristina Fernandes Lopes','123',1,'990123456','cristina@email.com');
INSERT INTO Utilizador VALUES(null,'Miguel Pinto Leite','123',2,'901234567','miguel@email.com');


INSERT INTO Comedouro VALUES(null,'comedouro 1',1,'sala',2);
INSERT INTO Comedouro VALUES(null,'comedouro 2',2,'cozinha',2);
INSERT INTO Comedouro VALUES(null,'comedouro 3',0,'quarto',1);
INSERT INTO Comedouro VALUES(null,'comedouro 4',3,'cozinha',0);
INSERT INTO Comedouro VALUES(null,'comedouro 5',0,'sala',3);
INSERT INTO Comedouro VALUES(null,'comedouro 6',4,'cozinha',4);
INSERT INTO Comedouro VALUES(null,'comedouro 7',1,'sala',2);
INSERT INTO Comedouro VALUES(null,'comedouro 8',1,'cozinha',2);
INSERT INTO Comedouro VALUES(null,'comedouro 9',2,'sala',1);
INSERT INTO Comedouro VALUES(null,'comedouro 10',1,'sala',2);


INSERT INTO Utilizador_Comedouro VALUES(1,1);
INSERT INTO Utilizador_Comedouro VALUES(1,2);
INSERT INTO Utilizador_Comedouro VALUES(1,3);
INSERT INTO Utilizador_Comedouro VALUES(2,1);
INSERT INTO Utilizador_Comedouro VALUES(2,2);
INSERT INTO Utilizador_Comedouro VALUES(2,3);
INSERT INTO Utilizador_Comedouro VALUES(3,1);
INSERT INTO Utilizador_Comedouro VALUES(3,2);
INSERT INTO Utilizador_Comedouro VALUES(3,3);
INSERT INTO Utilizador_Comedouro VALUES(4,1);
INSERT INTO Utilizador_Comedouro VALUES(4,2);
INSERT INTO Utilizador_Comedouro VALUES(4,3);
INSERT INTO Utilizador_Comedouro VALUES(5,4);
INSERT INTO Utilizador_Comedouro VALUES(5,5);
INSERT INTO Utilizador_Comedouro VALUES(6,6);
INSERT INTO Utilizador_Comedouro VALUES(7,7);
INSERT INTO Utilizador_Comedouro VALUES(8,8);
INSERT INTO Utilizador_Comedouro VALUES(8,9);
INSERT INTO Utilizador_Comedouro VALUES(9,8);
INSERT INTO Utilizador_Comedouro VALUES(9,9);
INSERT INTO Utilizador_Comedouro VALUES(10,10);


INSERT INTO Deposito VALUES(null,2,0,1);
INSERT INTO Deposito VALUES(null,2,1,2);
INSERT INTO Deposito VALUES(null,3,0,3);
INSERT INTO Deposito VALUES(null,2,0,4);
INSERT INTO Deposito VALUES(null,2,1,5);
INSERT INTO Deposito VALUES(null,3,1,6);
INSERT INTO Deposito VALUES(null,2,0,7);
INSERT INTO Deposito VALUES(null,3,0,8);
INSERT INTO Deposito VALUES(null,3,0,9);
INSERT INTO Deposito VALUES(null,2,1,10);


INSERT INTO Historico VALUES(null,current_timestamp(),'S1_agua',1);
INSERT INTO Historico VALUES(null,current_timestamp(),'S2_racao',1);
INSERT INTO Historico VALUES(null,'2021-06-05 15:10:05','S1_agua',1);
INSERT INTO Historico VALUES(null,'2021-06-06 15:30:05','S1_agua',1);
INSERT INTO Historico VALUES(null,'2022-12-29 13:10:00','S1_agua',3);
INSERT INTO Historico VALUES(null,'2022-12-29 13:10:00','S2_racao',2);
INSERT INTO Historico VALUES(null,'2022-12-29 15:10:00','S1_agua',0);
INSERT INTO Historico VALUES(null,'2022-12-29 15:10:05','S2_racao',0);
INSERT INTO Historico VALUES(null,'2022-12-29 15:20:15','S1_agua',4);
INSERT INTO Historico VALUES(null,'2022-12-29 15:20:05','S1_racao',3);
INSERT INTO Historico VALUES(null,'2022-12-29 13:10:20','S1_agua',4);
INSERT INTO Historico VALUES(null,'2022-12-29 13:10:25','S2_racao',1);
INSERT INTO Historico VALUES(null,'2022-12-29 15:12:00','S1_agua',5);
INSERT INTO Historico VALUES(null,'2022-12-29 15:12:05','S2_racao',1);
INSERT INTO Historico VALUES(null,'2022-12-29 15:20:15','S1_agua',4);
INSERT INTO Historico VALUES(null,'2022-12-29 15:20:45','S1_racao',2);
INSERT INTO Historico VALUES(null,'2022-12-29 15:20:00','S1_agua',0);
INSERT INTO Historico VALUES(null,'2022-12-29 15:50:05','S2_racao',0);
INSERT INTO Historico VALUES(null,'2022-12-29 15:50:15','S1_agua',4);
INSERT INTO Historico VALUES(null,'2022-12-29 15:20:05','S1_racao',3);


INSERT INTO Historico_Comedouro VALUES(1,1);
INSERT INTO Historico_Comedouro VALUES(2,1);
INSERT INTO Historico_Comedouro VALUES(3,1);
INSERT INTO Historico_Comedouro VALUES(4,1);
INSERT INTO Historico_Comedouro VALUES(5,1);
INSERT INTO Historico_Comedouro VALUES(6,1);
INSERT INTO Historico_Comedouro VALUES(7,2);
INSERT INTO Historico_Comedouro VALUES(8,2);
INSERT INTO Historico_Comedouro VALUES(9,2);
INSERT INTO Historico_Comedouro VALUES(10,2);
INSERT INTO Historico_Comedouro VALUES(11,3);
INSERT INTO Historico_Comedouro VALUES(12,3);
INSERT INTO Historico_Comedouro VALUES(13,4);
INSERT INTO Historico_Comedouro VALUES(14,4);
INSERT INTO Historico_Comedouro VALUES(15,5);
INSERT INTO Historico_Comedouro VALUES(16,6);
INSERT INTO Historico_Comedouro VALUES(17,6);
INSERT INTO Historico_Comedouro VALUES(18,7);
INSERT INTO Historico_Comedouro VALUES(19,8);
INSERT INTO Historico_Comedouro VALUES(20,9);


INSERT INTO Horarios VALUES(null,'20:00:00',3,1);
INSERT INTO Horarios VALUES(null,'08:00:00',2,1);
INSERT INTO Horarios VALUES(null,'21:00:00',1,2);
INSERT INTO Horarios VALUES(null,'15:00:00',3,2);
INSERT INTO Horarios VALUES(null,'12:00:00',2,3);
INSERT INTO Horarios VALUES(null,'10:00:00',1,4);
INSERT INTO Horarios VALUES(null,'15:00:00',3,5);
INSERT INTO Horarios VALUES(null,'09:00:00',3,5);
INSERT INTO Horarios VALUES(null,'13:00:00',2,6);
INSERT INTO Horarios VALUES(null,'22:00:00',1,7);
INSERT INTO Horarios VALUES(null,'10:00:00',1,8);
INSERT INTO Horarios VALUES(null,'15:00:00',2,8);
INSERT INTO Horarios VALUES(null,'18:00:00',3,8);
INSERT INTO Horarios VALUES(null,'19:00:00',2,9);
INSERT INTO Horarios VALUES(null,'12:00:00',1,10);
INSERT INTO Horarios VALUES(null,'20:00:00',1,10);


INSERT INTO Animal VALUES(null,'Max','cao',30,9,1);
INSERT INTO Animal VALUES(null,'chewbaca','cao',40,12,2);
INSERT INTO Animal VALUES(null,'rex','gato',20,5,3);
INSERT INTO Animal VALUES(null,'bethoven','cao',45,13,4);
INSERT INTO Animal VALUES(null,'lassie','cao',25,4,5);
INSERT INTO Animal VALUES(null,'miro','gato',5,5,6);
INSERT INTO Animal VALUES(null,'tareco','gato',10,2,7);
INSERT INTO Animal VALUES(null,'asdrubal','cao',15,5,8);
INSERT INTO Animal VALUES(null,'atila','cao',11,2,9);
INSERT INTO Animal VALUES(null,'loki','cao',22,1,10);


INSERT INTO Mensagens VALUES(null,1,'tampa aberta','a tampa esta aberta, por favor feche a tampa');
INSERT INTO Mensagens VALUES(null,1,'racao a acabar','a racao esta a acabar, encha o deposito');
INSERT INTO Mensagens VALUES(null,3,'Deposito vazio','o deposito esta vazio, por favor encha o deposito');


INSERT INTO Mensagens_Comedouro VALUES(1,1);
INSERT INTO Mensagens_Comedouro VALUES(2,1);
INSERT INTO Mensagens_Comedouro VALUES(3,1);
INSERT INTO Mensagens_Comedouro VALUES(2,2);
INSERT INTO Mensagens_Comedouro VALUES(2,3);
INSERT INTO Mensagens_Comedouro VALUES(3,3);