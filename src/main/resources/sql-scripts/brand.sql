-- public.brand definition

-- Drop table

-- DROP TABLE public.brand;

CREATE TABLE public.brand (
	brandpk uuid NOT NULL,
	brandcode varchar NOT NULL,
	brandname varchar NOT NULL,
	CONSTRAINT brand_code_unique UNIQUE (brandcode),
	CONSTRAINT brand_name_unique UNIQUE (brandname),
	CONSTRAINT brand_pk PRIMARY KEY (brandpk)
);