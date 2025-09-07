-- *********************************************
-- * Standard SQL generation                   
-- *--------------------------------------------
-- * DB-MAIN version: 11.0.2              
-- * Generator date: Sep 14 2021              
-- * Generation date: Mon May 19 09:12:53 2025 
-- * LUN file: C:\Users\lelef\Desktop\progetto_basi_di_dati\PROJECT.lun 
-- * Schema: canc/SQL 
-- ********************************************* 


-- Database Section
-- ________________ 

create database canc;


-- DBSpace Section
-- _______________


-- Tables Section
-- _____________ 

create table ACCESSORI (
     id_articolo char(1) not null,
     tematica char(1) not null,
     tipo char(1) not null,
     constraint FKART_ACC_ID primary key (id_articolo));

create table aderire (
     id_evento char(1) not null,
     IdUtente char(1) not null,
     constraint ID_aderire_ID primary key (id_evento, IdUtente));

create table ADMIN (
     IdUtente char(1) not null,
     constraint FKUTE_ADM_ID primary key (IdUtente));

create table ARTICOLO (
     id_articolo char(1) not null,
     nome char(1) not null,
     descrizione char(1) not null,
     prezzo char(1) not null,
     id_ordine char(1) not null,
     MODELLISMO char(1),
     LIBRI_E_FUMETTI char(1),
     GIOCHI_DI_RUOLO char(1),
     GIOCHI_IN_SCATOLA char(1),
     GADGET_E_GIOCATTOLI char(1),
     CARTE_COLLEZIONABILI char(1),
     ACCESSORI char(1),
     constraint ID_ARTICOLO_ID primary key (id_articolo));

create table CARTE_COLLEZIONABILI (
     id_articolo char(1) not null,
     gioco char(1) not null,
     collezione char(1) not null,
     formato char(1) not null,
     constraint FKART_CAR_ID primary key (id_articolo));

create table EVENTO (
     id_evento char(1) not null,
     nome char(1) not null,
     max_partecipanti -- Compound attribute -- not null,
     TORNEO char(1),
     EVENTO_DI_RUOLO char(1),
     constraint ID_EVENTO_ID primary key (id_evento));

create table EVENTO_DI_RUOLO (
     id_evento char(1) not null,
     sistema char(1) not null,
     multitavolo char(1) not null,
     constraint FKEVE_EVE_ID primary key (id_evento));

create table GADGET_E_GIOCATTOLI (
     id_articolo char(1) not null,
     franchise char(1) not null,
     tipo char(1) not null,
     constraint FKART_GAD_ID primary key (id_articolo));

create table GIOCHI_DI_RUOLO (
     id_articolo char(1) not null,
     sistema char(1) not null,
     tipo char(1) not null,
     constraint FKART_GIO_1_ID primary key (id_articolo));

create table GIOCHI_IN_SCATOLA (
     id_articolo char(1) not null,
     serie char(1) not null,
     meccanica char(1) not null,
     min_giocatori char(1) not null,
     max_giocatori char(1) not null,
     tempo_medio char(1) not null,
     constraint FKART_GIO_ID primary key (id_articolo));

create table LIBRI_E_FUMETTI (
     id_articolo char(1) not null,
     serie char(1) not null,
     tipo char(1) not null,
     categoria char(1) not null,
     autore char(1) not null,
     constraint FKART_LIB_ID primary key (id_articolo));

create table MODELLISMO (
     id_articolo char(1) not null,
     marca char(1) not null,
     tipo char(1) not null,
     constraint FKART_MOD_ID primary key (id_articolo));

create table ORDINE (
     id_ordine char(1) not null,
     data char(1) not null,
     totale char(1) not null,
     IdUtente char(1) not null,
     constraint ID_ORDINE_ID primary key (id_ordine));

create table PRENOTAZIONI (
     IdUtente char(1) not null,
     Data date not null,
     Num._Partecipanti char(1) not null,
     constraint ID_PRENOTAZIONI_ID primary key (IdUtente, Data));

create table PUBLIC (
     IdUtente char(1) not null,
     Nome char(1) not null,
     Cognome char(1) not null,
     Data_di_Nascita char(1) not null,
     constraint FKUTE_PUB_ID primary key (IdUtente));

create table R_2 (
     IdUtente char(1) not null,
     Data date not null,
     Numero char(1) not null,
     constraint ID_R_2_ID primary key (Numero, IdUtente, Data));

create table TAVOLI (
     Numero char(1) not null,
     Num._persone char(1) not null,
     constraint ID_TAVOLI_ID primary key (Numero));

create table TORNEO (
     _id_torneo char(1) not null,
     id_evento char(1) not null,
     gioco char(1) not null,
     constraint ID_TORNEO_ID primary key (_id_torneo),
     constraint FKEVE_TOR_ID unique (id_evento));

create table UTENTI (
     IdUtente char(1) not null,
     Mail char(1) not null,
     Password char(1) not null,
     PUBLIC char(1),
     ADMIN char(1),
     constraint ID_UTENTI_ID primary key (IdUtente),
     constraint SID_UTENTI_ID unique (Mail));

create table UTILIZZA (
     id_evento char(1) not null,
     Numero char(1) not null,
     constraint ID_UTILIZZA_ID primary key (Numero, id_evento));


-- Constraints Section
-- ___________________ 

alter table ACCESSORI add constraint FKART_ACC_FK
     foreign key (id_articolo)
     references ARTICOLO;

alter table aderire add constraint FKade_UTE_FK
     foreign key (IdUtente)
     references UTENTI;

alter table aderire add constraint FKade_EVE
     foreign key (id_evento)
     references EVENTO;

alter table ADMIN add constraint FKUTE_ADM_FK
     foreign key (IdUtente)
     references UTENTI;

alter table ARTICOLO add constraint EXTONE_ARTICOLO
     check((ACCESSORI is not null and GADGET_E_GIOCATTOLI is null and MODELLISMO is null and LIBRI_E_FUMETTI is null and GIOCHI_IN_SCATOLA is null and GIOCHI_DI_RUOLO is null and CARTE_COLLEZIONABILI is null)
           or (ACCESSORI is null and GADGET_E_GIOCATTOLI is not null and MODELLISMO is null and LIBRI_E_FUMETTI is null and GIOCHI_IN_SCATOLA is null and GIOCHI_DI_RUOLO is null and CARTE_COLLEZIONABILI is null)
           or (ACCESSORI is null and GADGET_E_GIOCATTOLI is null and MODELLISMO is not null and LIBRI_E_FUMETTI is null and GIOCHI_IN_SCATOLA is null and GIOCHI_DI_RUOLO is null and CARTE_COLLEZIONABILI is null)
           or (ACCESSORI is null and GADGET_E_GIOCATTOLI is null and MODELLISMO is null and LIBRI_E_FUMETTI is not null and GIOCHI_IN_SCATOLA is null and GIOCHI_DI_RUOLO is null and CARTE_COLLEZIONABILI is null)
           or (ACCESSORI is null and GADGET_E_GIOCATTOLI is null and MODELLISMO is null and LIBRI_E_FUMETTI is null and GIOCHI_IN_SCATOLA is not null and GIOCHI_DI_RUOLO is null and CARTE_COLLEZIONABILI is null)
           or (ACCESSORI is null and GADGET_E_GIOCATTOLI is null and MODELLISMO is null and LIBRI_E_FUMETTI is null and GIOCHI_IN_SCATOLA is null and GIOCHI_DI_RUOLO is not null and CARTE_COLLEZIONABILI is null)
           or (ACCESSORI is null and GADGET_E_GIOCATTOLI is null and MODELLISMO is null and LIBRI_E_FUMETTI is null and GIOCHI_IN_SCATOLA is null and GIOCHI_DI_RUOLO is null and CARTE_COLLEZIONABILI is not null)); 

alter table ARTICOLO add constraint FKcomporre_FK
     foreign key (id_ordine)
     references ORDINE;

alter table CARTE_COLLEZIONABILI add constraint FKART_CAR_FK
     foreign key (id_articolo)
     references ARTICOLO;

alter table EVENTO add constraint EXCL_EVENTO
     check((EVENTO_DI_RUOLO is not null and TORNEO is null)
           or (EVENTO_DI_RUOLO is null and TORNEO is not null)
           or (EVENTO_DI_RUOLO is null and TORNEO is null)); 

alter table EVENTO_DI_RUOLO add constraint FKEVE_EVE_FK
     foreign key (id_evento)
     references EVENTO;

alter table GADGET_E_GIOCATTOLI add constraint FKART_GAD_FK
     foreign key (id_articolo)
     references ARTICOLO;

alter table GIOCHI_DI_RUOLO add constraint FKART_GIO_1_FK
     foreign key (id_articolo)
     references ARTICOLO;

alter table GIOCHI_IN_SCATOLA add constraint FKART_GIO_FK
     foreign key (id_articolo)
     references ARTICOLO;

alter table LIBRI_E_FUMETTI add constraint FKART_LIB_FK
     foreign key (id_articolo)
     references ARTICOLO;

alter table MODELLISMO add constraint FKART_MOD_FK
     foreign key (id_articolo)
     references ARTICOLO;

alter table ORDINE add constraint FKrichiedere_FK
     foreign key (IdUtente)
     references PUBLIC;

alter table PRENOTAZIONI add constraint FKeseguire
     foreign key (IdUtente)
     references UTENTI;

alter table PUBLIC add constraint FKUTE_PUB_FK
     foreign key (IdUtente)
     references UTENTI;

alter table R_2 add constraint FKR_2_TAV
     foreign key (Numero)
     references TAVOLI;

alter table R_2 add constraint FKR_2_PRE_FK
     foreign key (IdUtente, Data)
     references PRENOTAZIONI;

alter table TORNEO add constraint FKEVE_TOR_FK
     foreign key (id_evento)
     references EVENTO;

alter table UTENTI add constraint EXTONE_UTENTI
     check((PUBLIC is not null and ADMIN is null)
           or (PUBLIC is null and ADMIN is not null)); 

alter table UTILIZZA add constraint FKUTI_TAV
     foreign key (Numero)
     references TAVOLI;

alter table UTILIZZA add constraint FKUTI_EVE_FK
     foreign key (id_evento)
     references EVENTO;


-- Index Section
-- _____________ 

create unique index FKART_ACC_IND
     on ACCESSORI (id_articolo);

create unique index ID_aderire_IND
     on aderire (id_evento, IdUtente);

create index FKade_UTE_IND
     on aderire (IdUtente);

create unique index FKUTE_ADM_IND
     on ADMIN (IdUtente);

create unique index ID_ARTICOLO_IND
     on ARTICOLO (id_articolo);

create index FKcomporre_IND
     on ARTICOLO (id_ordine);

create unique index FKART_CAR_IND
     on CARTE_COLLEZIONABILI (id_articolo);

create unique index ID_EVENTO_IND
     on EVENTO (id_evento);

create unique index FKEVE_EVE_IND
     on EVENTO_DI_RUOLO (id_evento);

create unique index FKART_GAD_IND
     on GADGET_E_GIOCATTOLI (id_articolo);

create unique index FKART_GIO_1_IND
     on GIOCHI_DI_RUOLO (id_articolo);

create unique index FKART_GIO_IND
     on GIOCHI_IN_SCATOLA (id_articolo);

create unique index FKART_LIB_IND
     on LIBRI_E_FUMETTI (id_articolo);

create unique index FKART_MOD_IND
     on MODELLISMO (id_articolo);

create unique index ID_ORDINE_IND
     on ORDINE (id_ordine);

create index FKrichiedere_IND
     on ORDINE (IdUtente);

create unique index ID_PRENOTAZIONI_IND
     on PRENOTAZIONI (IdUtente, Data);

create unique index FKUTE_PUB_IND
     on PUBLIC (IdUtente);

create unique index ID_R_2_IND
     on R_2 (Numero, IdUtente, Data);

create index FKR_2_PRE_IND
     on R_2 (IdUtente, Data);

create unique index ID_TAVOLI_IND
     on TAVOLI (Numero);

create unique index ID_TORNEO_IND
     on TORNEO (_id_torneo);

create unique index FKEVE_TOR_IND
     on TORNEO (id_evento);

create unique index ID_UTENTI_IND
     on UTENTI (IdUtente);

create unique index SID_UTENTI_IND
     on UTENTI (Mail);

create unique index ID_UTILIZZA_IND
     on UTILIZZA (Numero, id_evento);

create index FKUTI_EVE_IND
     on UTILIZZA (id_evento);

