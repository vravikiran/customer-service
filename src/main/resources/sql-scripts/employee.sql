-- public.employee definition

-- Drop table

-- DROP TABLE public.employee;

CREATE TABLE public.employee (
	customeremppk uuid NOT NULL,
	customerfk uuid NOT NULL,
	designation varchar NOT NULL,
	empname varchar NOT NULL,
	department varchar NOT NULL,
	phoneno int8 NOT NULL,
	mobileno int8 NULL,
	email varchar NULL,
	dob date NULL,
	anniversarydate date NULL,
	cdate date NULL,
	isactive bool DEFAULT true NULL,
	CONSTRAINT employee_pkey PRIMARY KEY (customeremppk),
	CONSTRAINT employee_customerfk_fkey FOREIGN KEY (customerfk) REFERENCES public.customer(customerpk)
);