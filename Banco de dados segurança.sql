	create database Banco_de_usuarios;
	use Banco_de_usuarios;

	create table usuario(
	id int not null auto_increment primary key,
	nome varchar(100) not null,
	email varchar(100) not null,
	senha text(100) not null,
	telefone varchar(15) not null);

	insert into usuario values (null, "Wendel", "wendel@gmail.com", "Senha1234", "40028922");

	select * from usuario;
    SELECT id, nome, email, senha_criptografada FROM usuario;
