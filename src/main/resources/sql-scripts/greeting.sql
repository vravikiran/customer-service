-- public.greeting definition

-- Drop table

-- DROP TABLE public.greeting;

CREATE TABLE public.greeting (
	greetingid uuid NOT NULL,
	greetingcode varchar NOT NULL,
	greetingdesc varchar NOT NULL,
	CONSTRAINT greeting_pk PRIMARY KEY (greetingid),
	CONSTRAINT greeting_unique UNIQUE (greetingcode),
	CONSTRAINT greeting_unique_1 UNIQUE (greetingdesc)
);