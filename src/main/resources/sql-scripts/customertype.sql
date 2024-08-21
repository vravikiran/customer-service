-- public.customertype definition

-- Drop table

-- DROP TABLE public.customertype;

CREATE TABLE public.customertype (
	bizcontacttypeid uuid NOT NULL,
	bizcontacttypedesc varchar NOT NULL,
	CONSTRAINT customertype_pk PRIMARY KEY (bizcontacttypeid),
	CONSTRAINT customertype_unique UNIQUE (bizcontacttypedesc)
);