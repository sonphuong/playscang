﻿-- Table: public.account

-- DROP TABLE public.account;

CREATE TABLE public.account
(
    id integer NOT NULL DEFAULT nextval('account_id_seq'::regclass),
    username character varying(30) COLLATE pg_catalog."default" NOT NULL,
    password character varying(150) COLLATE pg_catalog."default" NOT NULL,
    email character varying(255) COLLATE pg_catalog."default",
    website character varying(255) COLLATE pg_catalog."default",
    jp_name character varying(255) COLLATE pg_catalog."default",
    name character varying(255) COLLATE pg_catalog."default",
    gender integer NOT NULL DEFAULT 1,
    age integer,
    address character varying(255) COLLATE pg_catalog."default",
    is_deleted integer DEFAULT 0,
    updated_on timestamp without time zone,
    CONSTRAINT account_pkey PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.account OWNER to postgres;