drop table if exists url_relation;

/*==============================================================*/
/* Table: url_relation                                          */
/*==============================================================*/
create table url_relation
(
   id                   bigint(15) not null,
   src_url              varchar(500),
   target_url           varchar(1000),
   target_url_type      smallint(2),
   crawl_time           timestamp,
   update_time          timestamp,
   primary key (id)
);

alter table url_relation comment 'url关系表';
