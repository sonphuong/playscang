CREATE TABLE account (
    id SERIAL PRIMARY KEY NOT NULL,
    username character varying(30) NOT NULL,
    password character varying(150) NOT NULL,
    email character varying(255),
    website character varying(255),
    jp_name character varying(255),
    name character varying(255),
    gender integer DEFAULT 1 NOT NULL,
    age integer,
    address character varying(255),
    is_deleted integer DEFAULT 0,
    updated_on timestamp without time zone
);

ALTER TABLE ONLY account
    ADD CONSTRAINT account_email_key UNIQUE (email);
ALTER TABLE ONLY account
    ADD CONSTRAINT account_username_key UNIQUE (username);
