create database CASA_DEI_GIOCHI;
use CASA_DEI_GIOCHI;


create table ACCESSORI (
     tematica varchar(100) not null,
     id_categoria_accessori char(36) primary key );

create table ADMIN (
     id_admin varchar(50) not null unique,
     mail varchar(254) primary key,
     password varchar(50) not null);

create table ADMIN_CREA (
     id_evento char(50) references EVENTO(id_evento),
     mail char(254) references ADMIN(mail),
     primary key (id_evento, mail));

create table ADMIN_PARTECIPA (
     id_evento char(50) references EVENTO(id_evento),
     mail char(254) references ADMIN(mail),
     primary key (id_evento, mail));

create table AGGIUNGERE (
     bar_code int references ARTICOLO(bar_code),
     id_carrello char(50) references CARRELLO(id_carrello),
     carico int not null,
     primary key (bar_code, id_carrello));

create table ARTICOLO (
     bar_code int primary key,
     id_categoria_carte char(50),
     id_categoria_ruolo char(50),
     id_categoria_scatola char(50),
     id_categoria_letteratura char(50),
     id_categoria_modellismo char(50),
     id_categoria_gadget char(50),
     id_categoria_accessori char(50),
     nome varchar(100) not null,
     descrizione text not null,
     prezzo_listino int not null,
     prezzo_vendita int not null,
     disponibilità int not null,
     vendibile boolean not null);

create table CARRELLO (
     id_carrello char(50) primary key,
     data date not null,
     totale int not null,
     id_utente char(50) not null references CLIENTE(id_utente));

create table CARTE_COLLEZIONABILI (
     gioco varchar(100) not null,
     espansione varchar(100) not null,
     formato varchar(100) not null,
     id_categoria_carte char(50) primary key);

create table CLIENTE (
     id_utente char(50) primary key,
     mail varchar(254) not null unique,
     password varchar(50) not null,
     nome char(20) not null,
     cognome char(20) not null,
     data_di_nascita date not null,
     indirizzo_1 char(100) not null,
     indirizzo_2 char(100),
     indirizzo_3 char(100));

create table CLIENTE_CREA (
     id_evento char(50) not null references EVENTO(id_evento),
     id_utente char(50) not null references CLIENTE(id_utente),
     primary key (id_evento, id_utente));

create table CLIENTE_PARTECIPA (
     id_utente char(50) not null references CLIENTE(id_utente),
     id_evento char(50) not null references EVENTO(id_evento),
     primary key (id_utente, id_evento));

create table EVENTO (
     id_evento char(50) primary key,
     num_partecipanti int not null,
     data_ora_inizio datetime not null,
     data_ora_fine datetime not null,
     visibilità enum('pubblico', 'privato') not null,
     nome char(30),
     presentazione text,
     max_partecipanti int);

create table FORMARE (
     bar_code int not null references ARTICOLO(bar_code),
     id_richiesta char(50) not null references RICHIESTA(id_richiesta),
     Qta int not null,
     primary key (bar_code, id_richiesta ));

create table FORNITORE (
     p_iva char(11) not null primary key,
     dominio varchar(254) not null unique);

create table GADGET_E_GIOCATTOLI (
     franchise varchar(100) not null,
     id_categoria_gadget char(50) primary key);

create table GIOCHI_DI_RUOLO (
     tipo varchar(100) not null,
     id_categoria_ruolo char(50) primary key);

create table GIOCHI_IN_SCATOLA (
     linea varchar(100) not null,
     meccanica varchar(100) not null,
     min_giocatori int not null,
     max_giocatori int not null,
     tempo_medio int not null,
     id_categoria_scatola char(50) primary key);


create table MODELLISMO (
     marca varchar(100) not null,
     id_categoria_modellismo char(50) primary key);

create table ORDINE (
     id_ordine char(50) primary key,
     somma int not null,
     data date not null,
     mail varchar(254) not null references ADMIN(mail));

create table RICHIESTA (
     id_richiesta char(50) primary key,
     totale int not null,
     p_iva char(11) not null references FORNITORE(p_iva),
     id_ordine char(50) not null references ORDINE(id_ordine));

create table TAVOLO (
     numero int primary key,
     capienza int not null);

create table UTILIZZARE (
     id_evento char(50) not null references EVENTO(id_evento),
     bar_code char(1) not null references ARTICOLO(bar_code),
     numero char(1) not null references TAVOLO(numero),
     responsabile char(20) not null,
     telefono int not null,
     primary key (id_evento, bar_code, numero));


