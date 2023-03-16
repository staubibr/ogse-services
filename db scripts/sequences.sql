alter table public.odhf alter column id drop default;
alter table toy.csd alter column id drop default;
alter table toy.da alter column id drop default;
alter table toy.hospitals alter column id drop default;
alter table oil.alberta_grid_3347 alter column id drop default;
alter table oil.alberta_grid_4326 alter column id drop default;
alter table public.odhf alter column id drop default;

create sequence public.unique_id;
create sequence toy.unique_id;
create sequence oil.unique_id;

alter sequence public.unique_id RESTART WITH 1;
alter sequence toy.unique_id RESTART WITH 1;
alter sequence oil.unique_id RESTART WITH 1;

update public.odhf set id = nextval('public.unique_id');
update toy.csd  set id = nextval('toy.unique_id');
update toy.da  set id = nextval('toy.unique_id');
update toy.hospitals  set id = nextval('toy.unique_id');
update oil.alberta_grid_3347 set id = nextval('oil.unique_id');
update oil.alberta_grid_4326 set id = nextval('oil.unique_id');





