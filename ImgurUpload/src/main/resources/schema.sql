DROP TABLE IF EXISTS image_upload_job;
create table image_upload_job
(
   id varchar(50) not null,
   created TIMESTAMP,
   finished TIMESTAMP,
   status varchar(15),
   primary key(id)
);

DROP TABLE IF EXISTS image_url;
create table image_url
(
   job_id varchar(50),
   url varchar(500),
   status varchar(15),
   foreign key (job_id) references image_upload_job(id),
   primary key(job_id, url)
);