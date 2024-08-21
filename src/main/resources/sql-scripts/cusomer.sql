-- public.customer definition

-- Drop table

-- DROP TABLE public.customer;

CREATE TABLE public.customer (
	customerpk uuid NOT NULL,
	parentcustomerfk uuid NULL,
	dataimportfk uuid NULL,
	customertypefk uuid NOT NULL,
	customercode varchar NOT NULL,
	customername varchar NOT NULL,
	customeralias varchar NOT NULL,
	brandfk uuid NULL,
	supplystatefk uuid NOT NULL,
	gsttypefk uuid NOT NULL,
	istaxexempt bool NOT NULL,
	greetingfk uuid NOT NULL,
	creditstatusfk uuid NOT NULL,
	ratingfk uuid NOT NULL,
	allowduplicategstin bool DEFAULT false NULL,
	customergstin varchar NULL,
	supplygstin varchar NULL,
	phoneno int8 NOT NULL,
	mobileno int8 NOT NULL,
	faxnumber int8 NOT NULL,
	email varchar NOT NULL,
	panno varchar NULL,
	isactive bool DEFAULT true NOT NULL,
	tanno varchar NULL,
	website varchar NULL,
	CONSTRAINT customer_pk PRIMARY KEY (customerpk),
	CONSTRAINT customer_unique UNIQUE (customercode),
	CONSTRAINT customer_unique_1 UNIQUE (customername),
	CONSTRAINT customer_unique_2 UNIQUE (customeralias),
	CONSTRAINT customer_brand_fk FOREIGN KEY (brandfk) REFERENCES public.brand(brandpk),
	CONSTRAINT customer_creditstatus_fk FOREIGN KEY (creditstatusfk) REFERENCES public.creditstatus(creditstatusid),
	CONSTRAINT customer_customer_fk FOREIGN KEY (parentcustomerfk) REFERENCES public.customer(customerpk),
	CONSTRAINT customer_customertype_fk FOREIGN KEY (customertypefk) REFERENCES public.customertype(bizcontacttypeid),
	CONSTRAINT customer_greeting_fk FOREIGN KEY (greetingfk) REFERENCES public.greeting(greetingid),
	CONSTRAINT customer_gstntype_fk FOREIGN KEY (gsttypefk) REFERENCES public.gstntype(gstntypeid),
	CONSTRAINT customer_rating_fk FOREIGN KEY (ratingfk) REFERENCES public.rating(ratingid),
	CONSTRAINT customer_state_fk FOREIGN KEY (supplystatefk) REFERENCES public.state(stateid)
);