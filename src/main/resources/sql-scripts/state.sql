-- public.state definition

-- Drop table

-- DROP TABLE public.state;

CREATE TABLE public.state (
	stateid uuid NOT NULL,
	statecode int4 NOT NULL,
	statename varchar NOT NULL,
	CONSTRAINT state_pk PRIMARY KEY (stateid),
	CONSTRAINT state_unique UNIQUE (statecode),
	CONSTRAINT statename_unique UNIQUE (statename)
);