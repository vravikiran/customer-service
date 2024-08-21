-- public.dataimport definition

-- Drop table

-- DROP TABLE public.dataimport;

CREATE TABLE public.dataimport (
	dataimportpk uuid NOT NULL,
	importdate date NOT NULL,
	doctypefk uuid NOT NULL,
	filepath varchar NOT NULL,
	filedata bytea NOT NULL,
	CONSTRAINT dataimport_pk PRIMARY KEY (dataimportpk)
);