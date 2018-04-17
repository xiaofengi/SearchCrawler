drop table if exists web_page_detail;

/*==============================================================*/
/* Table: web_page_detail                                       */
/*==============================================================*/
create table web_page_detail
(
   id                   bigint(15) not null,
   url                  varchar(500),
   domain               varchar(100),
   title                varchar(100),
   src                  varchar(500),
   create_time          timestamp,
   author               varchar(100),
   keyword              varchar(100),
   content              text,
   html                 text,
   view_num             int(10),
   comment_num          int(10),
   crawl_time           timestamp,
   update_time          timestamp,
   primary key (id)
);

alter table web_page_detail comment '网页详情表';
