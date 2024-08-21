-- public.rating definition

-- Drop table

-- DROP TABLE public.rating;

CREATE TABLE public.rating (
	ratingid uuid NOT NULL,
	rating varchar NOT NULL,
	CONSTRAINT rating_pk PRIMARY KEY (ratingid),
	CONSTRAINT rating_unique UNIQUE (rating)
);