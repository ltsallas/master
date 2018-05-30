-- -----------------------------------------------------
-- Table `dbo`.`users`
-- -----------------------------------------------------
CREATE TABLE dbo.users
(
  [id]            INT           NOT NULL IDENTITY,
  [treatment]     NVARCHAR(255) NULL,
  [name]          NVARCHAR(255) NULL,
  [surname]       NVARCHAR(255) NULL,
  [address]       NVARCHAR(255) NULL,
  [address_num]   NVARCHAR(255) NULL,
  [postal_code]   NVARCHAR(255) NULL,
  [email]         NVARCHAR(255) NULL,
  [password]      NVARCHAR(255) NULL,
  [create_date]   BIGINT        NULL,
  [access_date]   BIGINT        NULL,
  [ecas_email]    NVARCHAR(255) NULL,
  [ecas_username] NVARCHAR(255) NULL,
  [type]          INT           NULL     DEFAULT NULL,
  [verified]      SMALLINT      NOT NULL DEFAULT 0,
  [lang]          NVARCHAR(255)          DEFAULT 'en',
  PRIMARY KEY ([id])
);
-- -----------------------------------------------------
-- Table `dbo`.`rights`
-- -----------------------------------------------------
CREATE TABLE dbo.rights
(
  [id]        INT           NOT NULL IDENTITY,
  [userId]    INT           NULL,
  [rightdesc] NVARCHAR(255) NULL,
  [type]      INT           NULL     DEFAULT NULL,
  PRIMARY KEY ([id])
);
CREATE INDEX [IDX_rights_userId]
  ON dbo.rights ([userId] ASC);
-- -----------------------------------------------------
-- Table `dbo`.`laus`
-- -----------------------------------------------------
CREATE TABLE dbo.laus
(
  [id]               INT           NOT NULL IDENTITY,
  [country_code]     NVARCHAR(255) NULL,
  [nuts3]            NVARCHAR(255) NULL,
  [lau1]             NVARCHAR(255) NULL,
  [lau2]             NVARCHAR(255) NULL,
  [_change]          NVARCHAR(255) NULL,
  [name1]            NVARCHAR(255) NULL,
  [name2]            NVARCHAR(255) NULL,
  [pop]              NVARCHAR(255) NULL,
  [area]             NVARCHAR(255) NULL,
  [physical_address] NVARCHAR(255) NULL,
  PRIMARY KEY ([id])
);
-- -----------------------------------------------------
-- Table `dbo`.`municipalities`
-- -----------------------------------------------------
CREATE TABLE dbo.municipalities
(
  [id]          INT           NOT NULL IDENTITY,
  [name]        NVARCHAR(255) NULL,
  [address]     NVARCHAR(255) NULL,
  [address_num] NVARCHAR(255) NULL,
  [postal_code] NVARCHAR(255) NULL,
  [country]     NVARCHAR(255) NULL,
  [lau]         INT           NULL,
  PRIMARY KEY ([id])
  ,
  CONSTRAINT [fk_municipalities_laus]
  FOREIGN KEY ([lau])
  REFERENCES dbo.laus ([id])
    ON DELETE CASCADE
    ON UPDATE CASCADE
);
CREATE INDEX [fk_municipalities_laus1_idx]
  ON dbo.municipalities ([lau] ASC);
-- -----------------------------------------------------
-- Table `dbo`.`registrations`
-- -----------------------------------------------------
CREATE TABLE dbo.registrations
(
  [id]               INT           NOT NULL IDENTITY,
  [_user]            INT           NOT NULL,
  [municipality]     INT           NOT NULL,
  [role]             NVARCHAR(500) NULL,
  [_status]          INT           NOT NULL,
  --  0=HOLD; 1=KO; 2=OK
  [legal_file1]      NVARCHAR(MAX) NULL,
  [legal_file2]      NVARCHAR(MAX) NULL,
  [legal_file3]      NVARCHAR(MAX) NULL,
  [legal_file4]      NVARCHAR(MAX) NULL,
  [ip_registration]  NVARCHAR(30)  NULL,
  [organisation_id]  INT                    DEFAULT NULL,
  [association_name] NVARCHAR(MAX)          DEFAULT NULL,
  [upload_time]      BIGINT                 DEFAULT NULL,
  [allFiles_flag]    INT                    DEFAULT NULL,
  [mail_counter]     INT           NOT NULL DEFAULT '3',
  PRIMARY KEY ([id])
  ,
  CONSTRAINT [fk_registrations_users]
  FOREIGN KEY ([_user])
  REFERENCES dbo.users ([id])
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT [fk_registrations_municipalities]
  FOREIGN KEY ([municipality])
  REFERENCES dbo.municipalities ([id])
    ON DELETE CASCADE
    ON UPDATE CASCADE
);
CREATE INDEX [fk_user_idx]
  ON dbo.registrations ([_user] ASC);
CREATE INDEX [fk_municipality_idx]
  ON dbo.registrations ([municipality] ASC);
-- -----------------------------------------------------
-- Table `dbo`.`calls`
-- -----------------------------------------------------
CREATE TABLE dbo.calls
(
  [id] INT NOT NULL IDENTITY,
  [event] VARCHAR(500) NULL,
  [start_date] BIGINT NULL,
  [end_date] BIGINT NULL,
  [budget] INT DEFAULT 0 NOT NULL,
  [budget_voucher] INT DEFAULT 1000 NOT NULL,
  [max_percent_country] INT DEFAULT 10 NOT NULL,
  PRIMARY KEY ([id])
);
-- -----------------------------------------------------
-- Table `dbo`.`nuts`
-- -----------------------------------------------------
CREATE TABLE dbo.nuts
(
  [id]           INT           NOT NULL IDENTITY,
  [code]         NVARCHAR(255) NULL,
  [label]        NVARCHAR(255) NULL,
  [level]        INT           NULL,
  [country_code] NVARCHAR(255) NULL,
  [_order]       INT           NULL,
  [sorting]      INT           NULL,
  PRIMARY KEY ([id])
);
-- -----------------------------------------------------
-- Table `dbo`.`threads`
-- -----------------------------------------------------
CREATE TABLE dbo.threads
(
  [id]        INT           NOT NULL IDENTITY,
  [title]     NVARCHAR(255) NULL,
  [reason]    NVARCHAR(255) NULL,
  [type]      INT           NOT NULL,
  [mediation] INT                    DEFAULT 0,
  PRIMARY KEY ([id])
);
-- -----------------------------------------------------
-- Table `dbo`.`thread_messages`
-- -----------------------------------------------------
CREATE TABLE dbo.thread_messages
(
  [id]          INT           NOT NULL IDENTITY,
  [thread]      INT           NOT NULL,
  [author]      INT           NOT NULL,
  [message]     NVARCHAR(MAX) NULL,
  [create_date] BIGINT        NULL,
  PRIMARY KEY ([id])
  ,
  CONSTRAINT [fk_thread_messages_threads]
  FOREIGN KEY ([thread])
  REFERENCES dbo.threads ([id])
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT [fk_thread_messages_users]
  FOREIGN KEY ([author])
  REFERENCES dbo.users ([id])
    ON DELETE CASCADE
    ON UPDATE CASCADE
);
CREATE INDEX [fk_thread_idx]
  ON dbo.thread_messages ([thread] ASC);
CREATE INDEX [fk_author_idx]
  ON dbo.thread_messages ([author] ASC);
-- -----------------------------------------------------
-- Table `dbo`.`suppliers`
-- -----------------------------------------------------
CREATE TABLE dbo.suppliers
(
  [id]                   INT           NOT NULL IDENTITY,
  [name]                 NVARCHAR(255) NULL,
  [address]              NVARCHAR(255) NULL,
  [vat]                  NVARCHAR(255) NULL,
  [bic]                  NVARCHAR(255) NULL,
  [account_number]       NVARCHAR(255) NULL,
  [website]              NVARCHAR(255) NULL,
  [contact_name]         NVARCHAR(255) NULL,
  [contact_surname]      NVARCHAR(255) NULL,
  [contact_phone_prefix] NVARCHAR(255) NULL,
  [contact_phone_number] NVARCHAR(255) NULL,
  [contact_email]        NVARCHAR(255) NULL,
  [logo]                 NVARCHAR(MAX) NULL,
  [_user]                INT           NULL,
  [legal_file1]          NVARCHAR(MAX) NULL,
  [legal_file2]          NVARCHAR(MAX) NULL,
  [_status]              INT           NULL,
  PRIMARY KEY ([id])
  ,
  CONSTRAINT [fk_suppliers_users]
  FOREIGN KEY ([_user])
  REFERENCES dbo.users ([id])
    ON DELETE CASCADE
    ON UPDATE CASCADE
);
CREATE INDEX [fk_suppliers_users_idx]
  ON dbo.suppliers ([_user] ASC);
-- -----------------------------------------------------
-- Table `dbo`.`applications`
-- -----------------------------------------------------
CREATE TABLE dbo.applications
(
  [id]                INT          NOT NULL IDENTITY,
  [call_id]           INT          NOT NULL,
  [registration]      INT          NOT NULL,
  [supplier]          INT          NULL,
  [voucher_awarded]   SMALLINT     NULL     DEFAULT 0,
  [date]              BIGINT       NULL,
  [lef_export]        BIGINT       NULL,
  [lef_import]        BIGINT       NULL,
  [lef_status]        INT          NULL     DEFAULT 0,
  [bc_export]         BIGINT       NULL,
  [bc_import]         BIGINT       NULL,
  [bc_status]         INT          NULL     DEFAULT 0,
  [lc_export]         BIGINT       NULL,
  [lc_import]         BIGINT       NULL,
  [lc_status]         INT          NULL     DEFAULT 0,
  [_status]           INT          NOT NULL DEFAULT 0,
  [invalidate_reason] VARCHAR(MAX) NULL,
  PRIMARY KEY ([id])
  ,
  CONSTRAINT [fk_applications_calls]
  FOREIGN KEY ([call_id])
  REFERENCES dbo.calls ([id])
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT [fk_applications_registrations]
  FOREIGN KEY ([registration])
  REFERENCES dbo.registrations ([id])
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT [fk_applications_suppliers]
  FOREIGN KEY ([supplier])
  REFERENCES dbo.suppliers ([id])
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);
CREATE INDEX [fk_call_idx]
  ON dbo.applications ([call_id] ASC);
CREATE INDEX [fk_registration_idx]
  ON dbo.applications ([registration] ASC);
CREATE INDEX [fk_applications_supplier_idx]
  ON dbo.applications ([supplier] ASC);
-- -----------------------------------------------------
-- Table `dbo`.`mayors`
-- -----------------------------------------------------
CREATE TABLE dbo.mayors
(
  [id]           INT           NOT NULL IDENTITY,
  [name]         NVARCHAR(255) NULL,
  [surname]      NVARCHAR(255) NULL,
  [treatment]    NVARCHAR(255) NULL,
  [email]        NVARCHAR(255) NULL,
  [municipality] INT           NULL,
  PRIMARY KEY ([id])
  ,
  CONSTRAINT [fk_mayors_municipalities]
  FOREIGN KEY ([municipality])
  REFERENCES dbo.municipalities ([id])
    ON DELETE CASCADE
    ON UPDATE CASCADE
);
CREATE INDEX [fk_mayors_municipalities_idx]
  ON dbo.mayors ([municipality] ASC);
-- -----------------------------------------------------
-- Table `dbo`.`representations`
-- -----------------------------------------------------
CREATE TABLE dbo.representations
(
  [id]           INT NOT NULL IDENTITY,
  [municipality] INT NOT NULL,
  [mayor]        INT NOT NULL,
  PRIMARY KEY ([id])
  ,
  CONSTRAINT [fk_representations_municipalities]
  FOREIGN KEY ([municipality])
  REFERENCES dbo.municipalities ([id])
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT [fk_representations_mayors]
  FOREIGN KEY ([mayor])
  REFERENCES dbo.mayors ([id])
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);
CREATE INDEX [fk_municipality_idx]
  ON dbo.representations ([municipality] ASC);
CREATE INDEX [fk_mayor_idx]
  ON dbo.representations ([mayor] ASC);
-- -----------------------------------------------------
-- Table `dbo`.`access_points`
-- -----------------------------------------------------
CREATE TABLE dbo.access_points
(
  [id]            INT           NOT NULL IDENTITY,
  [municipality]  INT           NULL,
  [name]          NVARCHAR(255) NULL,
  [product_name]  NVARCHAR(255) NULL,
  [model_number]  NVARCHAR(255) NULL,
  [serial_number] NVARCHAR(255) NULL,
  [indoor]        SMALLINT      NULL     DEFAULT 0,
  PRIMARY KEY ([id])
  ,
  CONSTRAINT [fk_access_points_municipalities]
  FOREIGN KEY ([municipality])
  REFERENCES dbo.municipalities ([id])
    ON DELETE CASCADE
    ON UPDATE CASCADE
);
CREATE INDEX [fk_municipality_idx]
  ON dbo.access_points ([municipality] ASC);
-- -----------------------------------------------------
-- Table `dbo`.`supplied_regions`
-- -----------------------------------------------------
CREATE TABLE dbo.supplied_regions
(
  [id]       INT NOT NULL IDENTITY,
  [supplier] INT NOT NULL,
  [region]   INT NOT NULL,
  PRIMARY KEY ([id])
  ,
  CONSTRAINT [fk_supplier_regions_suppliers]
  FOREIGN KEY ([supplier])
  REFERENCES dbo.suppliers ([id])
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT [fk_supplier_regions_nuts]
  FOREIGN KEY ([region])
  REFERENCES dbo.nuts ([id])
    ON DELETE CASCADE
    ON UPDATE CASCADE
);
CREATE INDEX [fk_supplier_regions_supplier_idx]
  ON dbo.supplied_regions ([supplier] ASC);
CREATE INDEX [fk_supplier_regions_region_idx]
  ON dbo.supplied_regions ([region] ASC);
-- -----------------------------------------------------
-- Table `dbo`.`helpdesk_issues`
-- -----------------------------------------------------
CREATE TABLE dbo.helpdesk_issues
(
  [id]           INT           NOT NULL IDENTITY,
  [from_email]   NVARCHAR(255) NULL,
  [assigned_to]  NVARCHAR(255) NULL,
  [topic]        NVARCHAR(255) NULL,
  [portal]       NVARCHAR(255) NULL,
  [member_state] NVARCHAR(255) NULL,
  [summary]      NVARCHAR(MAX) NULL,
  [create_date]  BIGINT        NULL,
  [_status]      INT           NOT NULL DEFAULT 0,
  [_ticket]      INT           NOT NULL DEFAULT 0,
  [lang]         NVARCHAR(255)          DEFAULT 'en',
  PRIMARY KEY ([id])
);
-- -----------------------------------------------------
-- Table `dbo`.`helpdesk_comments`
-- -----------------------------------------------------
CREATE TABLE dbo.helpdesk_comments
(
  [id]          INT           NOT NULL IDENTITY,
  [issue]       INT           NULL,
  [from_email]  NVARCHAR(255) NULL,
  [comment]     NVARCHAR(MAX) NULL,
  [create_date] BIGINT        NULL,
  PRIMARY KEY ([id])
  ,
  CONSTRAINT [fk_helpdesk_comments_issues]
  FOREIGN KEY ([issue])
  REFERENCES dbo.helpdesk_issues ([id])
    ON DELETE CASCADE
    ON UPDATE CASCADE
);
CREATE INDEX [fk_helpdesk_comments_issue_idx]
  ON dbo.helpdesk_comments ([issue] ASC);
-- -----------------------------------------------------
-- Table `dbo`.`timelines`
-- -----------------------------------------------------
CREATE TABLE dbo.timelines
(
  [id]          INT           NOT NULL IDENTITY,
  [call_id]     INT           NULL,
  [description] NVARCHAR(255) NULL,
  [start_date]  BIGINT        NULL,
  [end_date]    BIGINT        NULL,
  PRIMARY KEY ([id])
  ,
  CONSTRAINT [fk_timelines_call_id]
  FOREIGN KEY ([call_id])
  REFERENCES dbo.calls ([id])
    ON DELETE CASCADE
    ON UPDATE CASCADE
);
CREATE INDEX [fk_timelines_call_id_idx]
  ON dbo.timelines ([call_id] ASC);
-- -----------------------------------------------------
-- Table `dbo`.`temp_tokens`
-- -----------------------------------------------------
CREATE TABLE dbo.temp_tokens
(
  [id]          BIGINT        NOT NULL IDENTITY,
  [token]       NVARCHAR(255) NULL,
  [email]       NVARCHAR(255) NULL,
  [create_date] BIGINT        NULL,
  [expiry_date] BIGINT        NULL,
  [_user]       INT           NULL,
  PRIMARY KEY ([id])
  ,
  CONSTRAINT [fk_temp_tokens_users]
  FOREIGN KEY ([_user])
  REFERENCES dbo.users ([id])
    ON DELETE CASCADE
    ON UPDATE CASCADE
);
CREATE INDEX [fk_temp_tokens_users_idx]
  ON dbo.temp_tokens ([_user] ASC);
-- -----------------------------------------------------
-- Table `dbo`.`organizations`
-- -----------------------------------------------------
CREATE TABLE dbo.organizations
(
  [id]      INT           NOT NULL IDENTITY,
  [name]    NVARCHAR(255) NULL,
  [type]    NVARCHAR(255) NULL,
  [country] NVARCHAR(255) NULL,
  PRIMARY KEY ([id])
);
INSERT INTO dbo.organizations
(name, type, country)
VALUES
  ('Everis', 'private', 'ES');
-- -----------------------------------------------------
-- Table `dbo`.`voucher_management`
-- -----------------------------------------------------
CREATE TABLE voucher_management
(
  [id] INT NOT NULL IDENTITY,
  [call_id] INT NOT NULL,
  [member_state] VARCHAR(255) NULL,
  [minimum] INT NULL,
  [maximum] INT NULL,
  [reserve] INT DEFAULT 50,
  PRIMARY KEY ([id])
);
INSERT INTO voucher_management
  (call_id, member_state, minimum, maximum, reserve)
VALUES
  (1, 'Austria', 15, 80, 5),
  (1, 'Belgium', 15, 80, 5),
  (1, 'Bulgaria', 15, 80, 5),
  (1, 'Croatia', 15, 80, 5),
  (1, 'Cyprus', 15, 80, 5),
  (1, 'Czech Republic', 15, 80, 5),
  (1, 'Denmark', 15, 80, 5),
  (1, 'Estonia', 15, 80, 5),
  (1, 'Finland', 15, 80, 5),
  (1, 'France', 15, 80, 5),
  (1, 'Germany', 15, 80, 5),
  (1, 'Greece', 15, 80, 5),
  (1, 'Hungary', 15, 80, 5),
  (1, 'Ireland', 15, 80, 5),
  (1, 'Italy', 15, 80, 5),
  (1, 'Latvia', 15, 80, 5),
  (1, 'Lithuania', 15, 80, 5),
  (1, 'Luxembourg', 15, 80, 5),
  (1, 'Malta', 15, 80, 5),
  (1, 'Netherlands', 15, 80, 5),
  (1, 'Poland', 15, 80, 5),
  (1, 'Portugal', 15, 80, 5),
  (1, 'Romania', 15, 80, 5),
  (1, 'Slovakia', 15, 80, 5),
  (1, 'Slovenia', 15, 80, 5),
  (1, 'Spain', 15, 80, 5),
  (1, 'Sweden', 15, 80, 5);
-- -----------------------------------------------------
-- Table `dbo`.`user_threads`
-- -----------------------------------------------------
CREATE TABLE dbo.user_threads
(
  [id]     INT NOT NULL IDENTITY,
  [_user]  INT NOT NULL,
  [thread] INT NOT NULL,
  PRIMARY KEY ([id])
);
CREATE INDEX [IDX_user_threads_user]
  ON dbo.user_threads ([_user] ASC);

-- -----------------------------------------------------
-- Table `dbo`.`legal_files`
-- -----------------------------------------------------
CREATE TABLE dbo.legal_files
(
  [id]                  INT NOT NULL IDENTITY,
  [registration]        INT NOT NULL,
  [type]                INT NOT NULL,
  [data]                NVARCHAR(MAX) NULL,
  [upload_time]         BIGINT DEFAULT NULL,
  [request_correction]  SMALLINT NULL DEFAULT 0,
  [correction_reason]   INT NULL DEFAULT NULL,
  PRIMARY KEY ([id])
  ,
  CONSTRAINT [fk_legal_files_registrations]
  FOREIGN KEY ([registration])
  REFERENCES dbo.registrations ([id])
    ON DELETE CASCADE
    ON UPDATE CASCADE
);
CREATE INDEX [fk_registration_idx]
  ON dbo.legal_files ([registration] ASC);