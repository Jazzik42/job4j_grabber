CREATE TABLE company
(
    id integer NOT NULL,
    name character varying,
    CONSTRAINT company_pkey PRIMARY KEY (id)
);

CREATE TABLE person
(
    id integer NOT NULL,
    name character varying,
    company_id integer references company(id),
    CONSTRAINT person_pkey PRIMARY KEY (id)
);

select pr.name, cn.name from person as pr
join company as cn on pr.company_id=cn.id where cn.id != 5;


select cn.name, count(ps.name) from company as cn
join person as ps on ps.company_id=cn.id group by cn.name order by count Desc limit 1;
