-- public.gstntype definition

-- Drop table

-- DROP TABLE public.gstntype;

CREATE TABLE public.gstntype (
	gstntypeid uuid NOT NULL,
	gsttypedesc varchar NOT NULL,
	CONSTRAINT gstntype_pk PRIMARY KEY (gstntypeid),
	CONSTRAINT gstntype_unique UNIQUE (gsttypedesc)
);