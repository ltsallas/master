alter table legal_files_correction_reason ADD request_correction_date DATETIME NULL;
alter table APPLICANTLISTITEM ADD SUPPORTINGDOCUMENTS BIT DEFAULT(0);
alter table legal_files ADD upload_time DATETIME NULL;
ALTER TABLE [applications] ADD authorized_person INT NULL;

INSERT INTO [dbo].[registration_users]
          ([contact_email]
          ,[main]
          ,[registration]
          ,[status]
          ,[_user])
   SELECT u.ecas_email, 1, r.id, 1, u.id from registrations r inner join users u on r._user = u.id where u.ecas_email is not null;
ALTER TABLE [registrations] ALTER COLUMN _user INT null;