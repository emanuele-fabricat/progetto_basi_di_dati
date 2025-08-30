create database CASA_DEI_GIOCHI;
use CASA_DEI_GIOCHI;


create table ACCESSORI (
     tematica varchar(100) not null,
     id_articolo char(36) primary key references ARTICOLO(id_articolo));

create table ADMIN (
     id_admin char(36) not null unique,
     mail varchar(254) primary key,
     password varchar(50) not null);

create table ADMIN_CREA (
     id_evento char(36) references EVENTO(id_evento),
     mail varchar(254) references ADMIN(mail),
     primary key (id_evento, mail));

create table ADMIN_PARTECIPA (
     id_evento char(36) references EVENTO(id_evento),
     mail varchar(254) references ADMIN(mail),
     primary key (id_evento, mail));

create table AGGIUNGERE (
     id_articolo char(36) references ARTICOLO(id_articolo),
     id_carrello char(36) references CARRELLO(id_carrello),
     carico int not null,
     primary key (id_articolo, id_carrello));

create table ARTICOLO (
     id_articolo char(36) primary key,
     nome varchar(100) not null,
     descrizione text not null,
     prezzo_listino int not null,
     prezzo_vendita int not null,
     disponibilità int not null,
     tipologia ENUM('CARTE_COLLEZIONABILI', 'GIOCHI_DI_RUOLO', 'GIOCHI_IN_SCATOLA', 'LIBIR_E_FUMETTI', 'MODELLISMO', 'GADGET_E_GIOCATTOLI', 'ACCESSORI') not null);

create table CARRELLO (
     id_carrello char(36) primary key,
     data date not null,
     totale_carrello int not null,
     id_utente char(36) not null references CLIENTE(id_utente));

create table CARTE_COLLEZIONABILI (
     gioco varchar(100) not null,
     espansione varchar(100) not null,
     formato varchar(100) not null,
     id_articolo char(36) primary key references ARTICOLO(id_articolo));

create table CLIENTE (
     id_utente char(36) primary key references PRODOTTO(id_prodotto),
     mail varchar(254) not null unique,
     password varchar(36) not null,
     nome varchar(20) not null,
     cognome varchar(20) not null,
     data_di_nascita date not null,
     indirizzo_1 varchar(100) not null,
     indirizzo_2 varchar(100),
     indirizzo_3 varchar(100));

create table CLIENTE_CREA (
     id_evento char(36) references EVENTO(id_evento),
     id_utente char(36) references CLIENTE(id_utente),
     primary key (id_evento, id_utente));

create table CLIENTE_PARTECIPA (
     id_utente char(36) references CLIENTE(id_utente),
     id_evento char(36) references EVENTO(id_evento),
     primary key (id_utente, id_evento));
     
create table COMPORRE (
	 id_prodotto char(36) references PRODOTTO(id_prodotto),
     id_ordine char(36) references ORDINE(id_ordine),
     qta int not null,
     primary key (id_prodotto, id_ordine));

create table EVENTO (
     id_evento char(36) primary key,
     num_partecipanti int not null,
     data_ora_inizio datetime not null,
     data_ora_fine datetime not null,
     visibilità enum('pubblico', 'privato') not null,
     nome varchar(30),
     presentazione text,
     max_partecipanti int);

create table FORNITORE (
     p_iva char(11) not null primary key,
     dominio varchar(254) not null unique, 
     nomimnativo varchar(100) not null);

create table GADGET_E_GIOCATTOLI (
     franchise varchar(100) not null,
     id_articolo char(36) primary key references ARTCOLO(id_articolo));

create table GIOCHI_DI_RUOLO (
     tipo varchar(100) not null,
     id_articolo char(36) primary key references ARTCOLO(id_articolo));

create table GIOCHI_IN_SCATOLA (
     linea varchar(100) not null,
     meccanica varchar(100) not null,
     min_giocatori int not null,
     max_giocatori int not null,
     tempo_medio int not null,
     id_articolo char(36) primary key references ARTCOLO(id_articolo));

create table MODELLISMO (
     marca varchar(100) not null,
     id_articolo char(36) primary key references ARTCOLO(id_articolo));

create table ORDINE (
     id_ordine char(36) primary key,
     somma int not null,
     data date not null,
     mail varchar(254) not null references ADMIN(mail));

create table PRODOTTO (
	 id_prodotto char(36) primary key,
     nome_prodotto varchar(100) not null,
     costo int not null,
     p_iva char(11) not null references FORNITORE(p_iva));
     

create table TAVOLO (
     numero int auto_increment primary key,
     capienza int not null);

create table UTILIZZARE (
     id_evento char(36) not null references EVENTO(id_evento),
     id_articolo char(36) not null references ARTICOLO(id_articolo),
     numero int not null references TAVOLO(numero),
     responsabile char(20) not null,
     telefono int not null,
     primary key (id_evento, id_articolo, numero));


