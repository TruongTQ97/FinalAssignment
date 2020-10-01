IF NOT EXISTS(SELECT * FROM sys.databases WHERE name = 'filesharing')
  BEGIN
    CREATE DATABASE filesharing
    END
    GO
       USE filesharing
    GO


USE filesharing;
IF (NOT EXISTS (SELECT * FROM sys.schemas WHERE name = 'Assignment'))
BEGIN
    EXEC ('CREATE SCHEMA Assignment AUTHORIZATION [dbo]')
END

DROP TABLE Assignment.user_tbl;
IF NOT EXISTS(SELECT * FROM sysobjects WHERE name = 'USER_MST')
  BEGIN
	CREATE TABLE Assignment.file_tbl (
	fid bigint not null,
	created_at DATETIME2,
	file_name VARCHAR(255),
	file_size BIGINT,
	download_uri VARCHAR(255),
	username VARCHAR(70) UNIQUE NOT NULL,
	PRIMARY KEY (fid) );
	END

DROP TABLE Assignment.file_tbl;
IF NOT EXISTS(SELECT * FROM sysobjects WHERE name = 'FILE_MST')
  BEGIN
	CREATE TABLE Assignment.user_tbl (
	uid BIGINT NOT NULL,
	created_at DATETIME2,
	password VARCHAR(255),
	role VARCHAR(10),
	username VARCHAR(70) UNIQUE NOT NULL,
	PRIMARY KEY (uid));
	END


create table filesystem.url_sharing_tbl (username varchar(70) not null, shortenurl varchar(255) not null, primary key (username, shortenurl));

ALTER TABLE Assignment.file_tbl ADD CONSTRAINT FK FOREIGN KEY (username) REFERENCES Assignment.user_tbl (username);