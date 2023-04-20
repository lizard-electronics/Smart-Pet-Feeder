/*
	Smart Pet Feeder, Populate ERROS
    
    Desenvolvido por:
    - Eduardo Silva, 202001449
    - Manuel Lagarto, 202002147
    - Rui colaço, 199100317
*/

-- -----------------------------------------------------

use projeto;

/* nao é possivel inserir estes valores na tabela utilizador porque a formataçao dos valores para inserir na tabela utilizador esta errada, 
faltam os dados para a password para intoduzir os dados na tabela utilizador desta maneira. */
INSERT INTO Utilizador VALUES(null,'Eduardo',1,'911111111','edu@email.com');

-- -----------------------------------------------------

/*  nao é possivel mudar o utilizador porque o ID de destino esta a ser utilizado */
update Utilizador set ID_Utilizador =1
where ID_Utilizador=5 ;

-- -----------------------------------------------------

/* nao é possivel remover a tabela porque existem chaves estrangeiras desta tabela associadas a outras tabelas, 
é preciso remover as outras tabelas primeiro para depois ser removida esta tabela. */
drop table Comedouro;

-- -----------------------------------------------------

/* nao é possivel alterar a foreign key porque nao existe nenhum ID_horarios na tabela utilizador_comedouro */
alter table utilizador_comedouro add foreign key (ID_horarios) references horarios(ID_horarios);

-- -----------------------------------------------------

/* nao é possivel inserir estes dados na tabela pois nao existe nenhum comedouro com o ID igual a 12 */
INSERT INTO Utilizador_Comedouro VALUES(2,12);