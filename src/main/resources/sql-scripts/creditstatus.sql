-- public.creditstatus definition

-- Drop table

-- DROP TABLE public.creditstatus;

CREATE TABLE public.creditstatus (
	creditstatusid uuid NOT NULL,
	creditstatus varchar NOT NULL,
	CONSTRAINT creditstatus_pk PRIMARY KEY (creditstatusid),
	CONSTRAINT creditstatus_unique UNIQUE (creditstatus)
);