select * from  Assignment.File_tbl;
delete from  Assignment.files_permissions_tbl;

select * from Assignment.file_permission order by created_at desc;
insert into Filesystem.file_permission values (1, SYSDATETIME(), 'DOWNLOAD');
insert into Filesystem.file_permission values (2, SYSDATETIME(), 'VIEW');

select * from Filesystem.url_sharing_tbl;

