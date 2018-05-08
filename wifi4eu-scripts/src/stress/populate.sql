DELETE FROM municipalities
DELETE FROM users
DELETE FROM registrations
DELETE FROM mayors
DELETE FROM applications
DELETE FROM rights
DELETE FROM temp_tokens
DECLARE @i INT = 0
DECLARE @u VARCHAR(255) = ''
DECLARE @m_id VARCHAR(255) = ''
DECLARE @u_id VARCHAR(255) = ''
DECLARE @r_id VARCHAR(255) = ''
DECLARE @t_id VARCHAR(255) = ''
WHILE @i < 30000
BEGIN
  set @u = 'uid'+RIGHT(CONCAT('00000', CONVERT(VARCHAR, @i)), 5)
  -- Add user
  INSERT INTO users
    (treatment, name, surname, address, address_num, postal_code, email, password, create_date, access_date, ecas_email, ecas_username, type, verified, lang)
  VALUES
    (NULL, @u, @u, 'Diagonal', '605', '08028', @u+'.loadtest@ec.europa.eu', '', 1523537782500, 1523537782500, @u+'.loadtest@ec.europa.eu', @u, 3, 0, 'en')
  -- Add municipality
  INSERT INTO municipalities
    (name,address,address_num,postal_code,country,lau)
  VALUES
    ('Barcelona', 'Diagonal', '605', '08028', 'ESPAÑA', 35511)
  -- Add user and municipality link
  SET @u_id = (SELECT TOP 1
    id
  FROM users
  ORDER BY ID DESC)
  SET @m_id = (SELECT TOP 1
    id
  FROM municipalities
  ORDER BY ID DESC)
  -- Add mayor
  INSERT INTO mayors
    (treatment, name, surname, email, municipality)
  VALUES
    (NULL, @u, @u, @u+'.loadtest@ec.europa.eu', @m_id)
  -- Add registration
  INSERT INTO registrations
    (_user, municipality, role, _status, legal_file1, legal_file2, legal_file3, legal_file4, ip_registration, organisation_id, association_name, upload_time, allFiles_flag, mail_counter)
  VALUES(@u_id, @m_id, 'Representative', 0, 'data:application/pdf;base64,JVBERi0xLjQNJeLjz9', NULL, 'data:application/pdf;base64,JVBERi0xLjQNJeLjz9', NULL, '127.0.0.1', 0, '', 0, 1, 0)
  -- Add registration link
  SET @r_id = (SELECT TOP 1
    id
  FROM users
  ORDER BY ID DESC)
  -- Add temp_tokens
  INSERT INTO temp_tokens
    (token, email, create_date, expiry_date, _user)
  VALUES
    (54379073478005177501523550744865+@i, @u+'.loadtest@ec.europa.eu', 1523550744865, 1523557944865, @u_id)
  -- Add temp_token link
  SET @t_id = (SELECT TOP 1
    id
  FROM temp_tokens
  ORDER BY ID DESC)
  -- Add right
  INSERT INTO rights
    (userId,rightdesc, type)
  VALUES
    (@u_id, 'users_'+@u_id, 0),
    (@u_id, 'municipalities_'+@m_id, 3),
    (@u_id, 'registrations_'+@r_id, 3),
    (@u_id, 'temp_tokens_'+@t_id, 3),
    (@u_id, 'mayors_'+@t_id, 3)
  SET @i = @i + 1
END
-- Add user
INSERT INTO users
  (treatment, name, surname, address, address_num, postal_code, email, password, create_date, access_date, ecas_email, ecas_username, type, verified, lang)
VALUES
  ('', 'aagaach', 'aagaach', 'Diagonal', 'Diagonal', '605', '08028', 'Christian.AAGAARD@ec.europa.eu', 1523537782500, 1523537782500, 'Christian.AAGAARD@ec.europa.eu', 'aagaach', 3, 0, 'en')
-- Add municipality
INSERT INTO municipalities
  (name,address,address_num,postal_code,country,lau)
VALUES
  ('Barcelona', 'Diagonal', '605', '08028', 'ESPAÑA', 35511)
-- Add user and municipality link
SET @u_id = (SELECT TOP 1
  id
FROM users
ORDER BY ID DESC)
SET @m_id = (SELECT TOP 1
  id
FROM municipalities
ORDER BY ID DESC)
-- Add mayor
INSERT INTO mayors
  (treatment, name, surname, email, municipality)
VALUES
  (NULL, 'aagaach', 'aagaach', 'Christian.AAGAARD@ec.europa.eu', @m_id)
-- Add registration
INSERT INTO registrations
  (_user, municipality, role, _status, legal_file1, legal_file2, legal_file3, legal_file4, ip_registration, organisation_id, association_name, upload_time, allFiles_flag, mail_counter)
VALUES(@u_id, @m_id, 'Representative', 0, 'data:application/pdf;base64,JVBERi0xLjQNJeLjz9', NULL, 'data:application/pdf;base64,JVBERi0xLjQNJeLjz9', NULL, '127.0.0.1', 0, '', 0, 1, 0)
-- Add registration link
SET @r_id = (SELECT TOP 1
  id
FROM users
ORDER BY ID DESC)
-- Add temp_tokens
INSERT INTO temp_tokens
  (token, email, create_date, expiry_date, _user)
VALUES
  (54379073478005177501523550744865+@i+1, 'Christian.AAGAARD@ec.europa.eu', 1523550744865, 1523557944865, @u_id)
-- Add temp_token link
SET @t_id = (SELECT TOP 1
  id
FROM temp_tokens
ORDER BY ID DESC)
-- Add right
INSERT INTO rights
  (userId,rightdesc, type)
VALUES
  (@u_id, 'users_'+@u_id, 0),
  (@u_id, 'municipalities_'+@m_id, 3),
  (@u_id, 'registrations_'+@r_id, 3),
  (@u_id, 'temp_tokens_'+@t_id, 3),
  (@u_id, 'mayors_'+@t_id, 3)
-- Add user
INSERT INTO users
  (treatment, name, surname, address, address_num, postal_code, email, password, create_date, access_date, ecas_email, ecas_username, type, verified, lang)
VALUES
  ('', 'aagaape', 'aagaape', 'Diagonal', 'Diagonal', '605', '08028', 'Peter.Aagaard@ec.europa.eu', 1523537782500, 1523537782500, 'Peter.Aagaard@ec.europa.eu', 'aagaape', 3, 0, 'en')
-- Add municipality
INSERT INTO municipalities
  (name,address,address_num,postal_code,country,lau)
VALUES
  ('Barcelona', 'Diagonal', '605', '08028', 'ESPAÑA', 35511)
-- Add user and municipality link
SET @u_id = (SELECT TOP 1
  id
FROM users
ORDER BY ID DESC)
SET @m_id = (SELECT TOP 1
  id
FROM municipalities
ORDER BY ID DESC)
-- Add mayor
INSERT INTO mayors
  (treatment, name, surname, email, municipality)
VALUES
  (NULL, 'aagaape', 'aagaape', 'Peter.Aagaard@ec.europa.eu', @m_id)
-- Add registration
INSERT INTO registrations
  (_user, municipality, role, _status, legal_file1, legal_file2, legal_file3, legal_file4, ip_registration, organisation_id, association_name, upload_time, allFiles_flag, mail_counter)
VALUES(@u_id, @m_id, 'Representative', 0, 'data:application/pdf;base64,JVBERi0xLjQNJeLjz9', NULL, 'data:application/pdf;base64,JVBERi0xLjQNJeLjz9', NULL, '127.0.0.1', 0, '', 0, 1, 0)
-- Add registration link
SET @r_id = (SELECT TOP 1
  id
FROM users
ORDER BY ID DESC)
-- Add temp_tokens
INSERT INTO temp_tokens
  (token, email, create_date, expiry_date, _user)
VALUES
  (54379073478005177501523550744865+@i+1, 'Peter.Aagaard@ec.europa.eu', 1523550744865, 1523557944865, @u_id)
-- Add temp_token link
SET @t_id = (SELECT TOP 1
  id
FROM temp_tokens
ORDER BY ID DESC)
-- Add right
INSERT INTO rights
  (userId,rightdesc, type)
VALUES
  (@u_id, 'users_'+@u_id, 0),
  (@u_id, 'municipalities_'+@m_id, 3),
  (@u_id, 'registrations_'+@r_id, 3),
  (@u_id, 'temp_tokens_'+@t_id, 3),
  (@u_id, 'mayors_'+@t_id, 3)
-- Add user
INSERT INTO users
  (treatment, name, surname, address, address_num, postal_code, email, password, create_date, access_date, ecas_email, ecas_username, type, verified, lang)
VALUES
  ('', 'aalankr', 'aalankr', 'Diagonal', 'Diagonal', '605', '08028', 'Kristine.AALAND@ec.europa.eu', 1523537782500, 1523537782500, 'Kristine.AALAND@ec.europa.eu', 'aalankr', 3, 0, 'en')
-- Add municipality
INSERT INTO municipalities
  (name,address,address_num,postal_code,country,lau)
VALUES
  ('Barcelona', 'Diagonal', '605', '08028', 'ESPAÑA', 35511)
-- Add user and municipality link
SET @u_id = (SELECT TOP 1
  id
FROM users
ORDER BY ID DESC)
SET @m_id = (SELECT TOP 1
  id
FROM municipalities
ORDER BY ID DESC)
-- Add mayor
INSERT INTO mayors
  (treatment, name, surname, email, municipality)
VALUES
  (NULL, 'aalankr', 'aalankr', 'Kristine.AALAND@ec.europa.eu', @m_id)
-- Add registration
INSERT INTO registrations
  (_user, municipality, role, _status, legal_file1, legal_file2, legal_file3, legal_file4, ip_registration, organisation_id, association_name, upload_time, allFiles_flag, mail_counter)
VALUES(@u_id, @m_id, 'Representative', 0, 'data:application/pdf;base64,JVBERi0xLjQNJeLjz9', NULL, 'data:application/pdf;base64,JVBERi0xLjQNJeLjz9', NULL, '127.0.0.1', 0, '', 0, 1, 0)
-- Add registration link
SET @r_id = (SELECT TOP 1
  id
FROM users
ORDER BY ID DESC)
-- Add temp_tokens
INSERT INTO temp_tokens
  (token, email, create_date, expiry_date, _user)
VALUES
  (54379073478005177501523550744865+@i+1, 'Kristine.AALAND@ec.europa.eu', 1523550744865, 1523557944865, @u_id)
-- Add temp_token link
SET @t_id = (SELECT TOP 1
  id
FROM temp_tokens
ORDER BY ID DESC)
-- Add right
INSERT INTO rights
  (userId,rightdesc, type)
VALUES
  (@u_id, 'users_'+@u_id, 0),
  (@u_id, 'municipalities_'+@m_id, 3),
  (@u_id, 'registrations_'+@r_id, 3),
  (@u_id, 'temp_tokens_'+@t_id, 3),
  (@u_id, 'mayors_'+@t_id, 3)
-- Add user
INSERT INTO users
  (treatment, name, surname, address, address_num, postal_code, email, password, create_date, access_date, ecas_email, ecas_username, type, verified, lang)
VALUES
  ('', 'aallima', 'aallima', 'Diagonal', 'Diagonal', '605', '08028', 'Malene.AALLING@ec.europa.eu', 1523537782500, 1523537782500, 'Malene.AALLING@ec.europa.eu', 'aallima', 3, 0, 'en')
-- Add municipality
INSERT INTO municipalities
  (name,address,address_num,postal_code,country,lau)
VALUES
  ('Barcelona', 'Diagonal', '605', '08028', 'ESPAÑA', 35511)
-- Add user and municipality link
SET @u_id = (SELECT TOP 1
  id
FROM users
ORDER BY ID DESC)
SET @m_id = (SELECT TOP 1
  id
FROM municipalities
ORDER BY ID DESC)
-- Add mayor
INSERT INTO mayors
  (treatment, name, surname, email, municipality)
VALUES
  (NULL, 'aallima', 'aallima', 'Malene.AALLING@ec.europa.eu', @m_id)
-- Add registration
INSERT INTO registrations
  (_user, municipality, role, _status, legal_file1, legal_file2, legal_file3, legal_file4, ip_registration, organisation_id, association_name, upload_time, allFiles_flag, mail_counter)
VALUES(@u_id, @m_id, 'Representative', 0, 'data:application/pdf;base64,JVBERi0xLjQNJeLjz9', NULL, 'data:application/pdf;base64,JVBERi0xLjQNJeLjz9', NULL, '127.0.0.1', 0, '', 0, 1, 0)
-- Add registration link
SET @r_id = (SELECT TOP 1
  id
FROM users
ORDER BY ID DESC)
-- Add temp_tokens
INSERT INTO temp_tokens
  (token, email, create_date, expiry_date, _user)
VALUES
  (54379073478005177501523550744865+@i+1, 'Malene.AALLING@ec.europa.eu', 1523550744865, 1523557944865, @u_id)
-- Add temp_token link
SET @t_id = (SELECT TOP 1
  id
FROM temp_tokens
ORDER BY ID DESC)
-- Add right
INSERT INTO rights
  (userId,rightdesc, type)
VALUES
  (@u_id, 'users_'+@u_id, 0),
  (@u_id, 'municipalities_'+@m_id, 3),
  (@u_id, 'registrations_'+@r_id, 3),
  (@u_id, 'temp_tokens_'+@t_id, 3),
  (@u_id, 'mayors_'+@t_id, 3)
-- Add user
INSERT INTO users
  (treatment, name, surname, address, address_num, postal_code, email, password, create_date, access_date, ecas_email, ecas_username, type, verified, lang)
VALUES
  ('', 'aaltope', 'aaltope', 'Diagonal', 'Diagonal', '605', '08028', 'Pekka.AALTO@ec.europa.eu', 1523537782500, 1523537782500, 'Pekka.AALTO@ec.europa.eu', 'aaltope', 3, 0, 'en')
-- Add municipality
INSERT INTO municipalities
  (name,address,address_num,postal_code,country,lau)
VALUES
  ('Barcelona', 'Diagonal', '605', '08028', 'ESPAÑA', 35511)
-- Add user and municipality link
SET @u_id = (SELECT TOP 1
  id
FROM users
ORDER BY ID DESC)
SET @m_id = (SELECT TOP 1
  id
FROM municipalities
ORDER BY ID DESC)
-- Add mayor
INSERT INTO mayors
  (treatment, name, surname, email, municipality)
VALUES
  (NULL, 'aaltope', 'aaltope', 'Pekka.AALTO@ec.europa.eu', @m_id)
-- Add registration
INSERT INTO registrations
  (_user, municipality, role, _status, legal_file1, legal_file2, legal_file3, legal_file4, ip_registration, organisation_id, association_name, upload_time, allFiles_flag, mail_counter)
VALUES(@u_id, @m_id, 'Representative', 0, 'data:application/pdf;base64,JVBERi0xLjQNJeLjz9', NULL, 'data:application/pdf;base64,JVBERi0xLjQNJeLjz9', NULL, '127.0.0.1', 0, '', 0, 1, 0)
-- Add registration link
SET @r_id = (SELECT TOP 1
  id
FROM users
ORDER BY ID DESC)
-- Add temp_tokens
INSERT INTO temp_tokens
  (token, email, create_date, expiry_date, _user)
VALUES
  (54379073478005177501523550744865+@i+1, 'Pekka.AALTO@ec.europa.eu', 1523550744865, 1523557944865, @u_id)
-- Add temp_token link
SET @t_id = (SELECT TOP 1
  id
FROM temp_tokens
ORDER BY ID DESC)
-- Add right
INSERT INTO rights
  (userId,rightdesc, type)
VALUES
  (@u_id, 'users_'+@u_id, 0),
  (@u_id, 'municipalities_'+@m_id, 3),
  (@u_id, 'registrations_'+@r_id, 3),
  (@u_id, 'temp_tokens_'+@t_id, 3),
  (@u_id, 'mayors_'+@t_id, 3)
-- Add user
INSERT INTO users
  (treatment, name, surname, address, address_num, postal_code, email, password, create_date, access_date, ecas_email, ecas_username, type, verified, lang)
VALUES
  ('', 'aaltosi', 'aaltosi', 'Diagonal', 'Diagonal', '605', '08028', 'Sirpa.AALTONEN@ec.europa.eu', 1523537782500, 1523537782500, 'Sirpa.AALTONEN@ec.europa.eu', 'aaltosi', 3, 0, 'en')
-- Add municipality
INSERT INTO municipalities
  (name,address,address_num,postal_code,country,lau)
VALUES
  ('Barcelona', 'Diagonal', '605', '08028', 'ESPAÑA', 35511)
-- Add user and municipality link
SET @u_id = (SELECT TOP 1
  id
FROM users
ORDER BY ID DESC)
SET @m_id = (SELECT TOP 1
  id
FROM municipalities
ORDER BY ID DESC)
-- Add mayor
INSERT INTO mayors
  (treatment, name, surname, email, municipality)
VALUES
  (NULL, 'aaltosi', 'aaltosi', 'Sirpa.AALTONEN@ec.europa.eu', @m_id)
-- Add registration
INSERT INTO registrations
  (_user, municipality, role, _status, legal_file1, legal_file2, legal_file3, legal_file4, ip_registration, organisation_id, association_name, upload_time, allFiles_flag, mail_counter)
VALUES(@u_id, @m_id, 'Representative', 0, 'data:application/pdf;base64,JVBERi0xLjQNJeLjz9', NULL, 'data:application/pdf;base64,JVBERi0xLjQNJeLjz9', NULL, '127.0.0.1', 0, '', 0, 1, 0)
-- Add registration link
SET @r_id = (SELECT TOP 1
  id
FROM users
ORDER BY ID DESC)
-- Add temp_tokens
INSERT INTO temp_tokens
  (token, email, create_date, expiry_date, _user)
VALUES
  (54379073478005177501523550744865+@i+1, 'Sirpa.AALTONEN@ec.europa.eu', 1523550744865, 1523557944865, @u_id)
-- Add temp_token link
SET @t_id = (SELECT TOP 1
  id
FROM temp_tokens
ORDER BY ID DESC)
-- Add right
INSERT INTO rights
  (userId,rightdesc, type)
VALUES
  (@u_id, 'users_'+@u_id, 0),
  (@u_id, 'municipalities_'+@m_id, 3),
  (@u_id, 'registrations_'+@r_id, 3),
  (@u_id, 'temp_tokens_'+@t_id, 3),
  (@u_id, 'mayors_'+@t_id, 3)
-- Add user
INSERT INTO users
  (treatment, name, surname, address, address_num, postal_code, email, password, create_date, access_date, ecas_email, ecas_username, type, verified, lang)
VALUES
  ('', 'aarabmo', 'aarabmo', 'Diagonal', 'Diagonal', '605', '08028', 'Mounir.AARAB@ext.ec.europa.eu', 1523537782500, 1523537782500, 'Mounir.AARAB@ext.ec.europa.eu', 'aarabmo', 3, 0, 'en')
-- Add municipality
INSERT INTO municipalities
  (name,address,address_num,postal_code,country,lau)
VALUES
  ('Barcelona', 'Diagonal', '605', '08028', 'ESPAÑA', 35511)
-- Add user and municipality link
SET @u_id = (SELECT TOP 1
  id
FROM users
ORDER BY ID DESC)
SET @m_id = (SELECT TOP 1
  id
FROM municipalities
ORDER BY ID DESC)
-- Add mayor
INSERT INTO mayors
  (treatment, name, surname, email, municipality)
VALUES
  (NULL, 'aarabmo', 'aarabmo', 'Mounir.AARAB@ext.ec.europa.eu', @m_id)
-- Add registration
INSERT INTO registrations
  (_user, municipality, role, _status, legal_file1, legal_file2, legal_file3, legal_file4, ip_registration, organisation_id, association_name, upload_time, allFiles_flag, mail_counter)
VALUES(@u_id, @m_id, 'Representative', 0, 'data:application/pdf;base64,JVBERi0xLjQNJeLjz9', NULL, 'data:application/pdf;base64,JVBERi0xLjQNJeLjz9', NULL, '127.0.0.1', 0, '', 0, 1, 0)
-- Add registration link
SET @r_id = (SELECT TOP 1
  id
FROM users
ORDER BY ID DESC)
-- Add temp_tokens
INSERT INTO temp_tokens
  (token, email, create_date, expiry_date, _user)
VALUES
  (54379073478005177501523550744865+@i+1, 'Mounir.AARAB@ext.ec.europa.eu', 1523550744865, 1523557944865, @u_id)
-- Add temp_token link
SET @t_id = (SELECT TOP 1
  id
FROM temp_tokens
ORDER BY ID DESC)
-- Add right
INSERT INTO rights
  (userId,rightdesc, type)
VALUES
  (@u_id, 'users_'+@u_id, 0),
  (@u_id, 'municipalities_'+@m_id, 3),
  (@u_id, 'registrations_'+@r_id, 3),
  (@u_id, 'temp_tokens_'+@t_id, 3),
  (@u_id, 'mayors_'+@t_id, 3)
-- Add user
INSERT INTO users
  (treatment, name, surname, address, address_num, postal_code, email, password, create_date, access_date, ecas_email, ecas_username, type, verified, lang)
VALUES
  ('', 'aarabnn', 'aarabnn', 'Diagonal', 'Diagonal', '605', '08028', 'Nesrin.TRIBAK@ec.europa.eu', 1523537782500, 1523537782500, 'Nesrin.TRIBAK@ec.europa.eu', 'aarabnn', 3, 0, 'en')
-- Add municipality
INSERT INTO municipalities
  (name,address,address_num,postal_code,country,lau)
VALUES
  ('Barcelona', 'Diagonal', '605', '08028', 'ESPAÑA', 35511)
-- Add user and municipality link
SET @u_id = (SELECT TOP 1
  id
FROM users
ORDER BY ID DESC)
SET @m_id = (SELECT TOP 1
  id
FROM municipalities
ORDER BY ID DESC)
-- Add mayor
INSERT INTO mayors
  (treatment, name, surname, email, municipality)
VALUES
  (NULL, 'aarabnn', 'aarabnn', 'Nesrin.TRIBAK@ec.europa.eu', @m_id)
-- Add registration
INSERT INTO registrations
  (_user, municipality, role, _status, legal_file1, legal_file2, legal_file3, legal_file4, ip_registration, organisation_id, association_name, upload_time, allFiles_flag, mail_counter)
VALUES(@u_id, @m_id, 'Representative', 0, 'data:application/pdf;base64,JVBERi0xLjQNJeLjz9', NULL, 'data:application/pdf;base64,JVBERi0xLjQNJeLjz9', NULL, '127.0.0.1', 0, '', 0, 1, 0)
-- Add registration link
SET @r_id = (SELECT TOP 1
  id
FROM users
ORDER BY ID DESC)
-- Add temp_tokens
INSERT INTO temp_tokens
  (token, email, create_date, expiry_date, _user)
VALUES
  (54379073478005177501523550744865+@i+1, 'Nesrin.TRIBAK@ec.europa.eu', 1523550744865, 1523557944865, @u_id)
-- Add temp_token link
SET @t_id = (SELECT TOP 1
  id
FROM temp_tokens
ORDER BY ID DESC)
-- Add right
INSERT INTO rights
  (userId,rightdesc, type)
VALUES
  (@u_id, 'users_'+@u_id, 0),
  (@u_id, 'municipalities_'+@m_id, 3),
  (@u_id, 'registrations_'+@r_id, 3),
  (@u_id, 'temp_tokens_'+@t_id, 3),
  (@u_id, 'mayors_'+@t_id, 3)
-- Add user
INSERT INTO users
  (treatment, name, surname, address, address_num, postal_code, email, password, create_date, access_date, ecas_email, ecas_username, type, verified, lang)
VALUES
  ('', 'aardean', 'aardean', 'Diagonal', 'Diagonal', '605', '08028', 'Ann.VAN-AARDEN@ec.europa.eu', 1523537782500, 1523537782500, 'Ann.VAN-AARDEN@ec.europa.eu', 'aardean', 3, 0, 'en')
-- Add municipality
INSERT INTO municipalities
  (name,address,address_num,postal_code,country,lau)
VALUES
  ('Barcelona', 'Diagonal', '605', '08028', 'ESPAÑA', 35511)
-- Add user and municipality link
SET @u_id = (SELECT TOP 1
  id
FROM users
ORDER BY ID DESC)
SET @m_id = (SELECT TOP 1
  id
FROM municipalities
ORDER BY ID DESC)
-- Add mayor
INSERT INTO mayors
  (treatment, name, surname, email, municipality)
VALUES
  (NULL, 'aardean', 'aardean', 'Ann.VAN-AARDEN@ec.europa.eu', @m_id)
-- Add registration
INSERT INTO registrations
  (_user, municipality, role, _status, legal_file1, legal_file2, legal_file3, legal_file4, ip_registration, organisation_id, association_name, upload_time, allFiles_flag, mail_counter)
VALUES(@u_id, @m_id, 'Representative', 0, 'data:application/pdf;base64,JVBERi0xLjQNJeLjz9', NULL, 'data:application/pdf;base64,JVBERi0xLjQNJeLjz9', NULL, '127.0.0.1', 0, '', 0, 1, 0)
-- Add registration link
SET @r_id = (SELECT TOP 1
  id
FROM users
ORDER BY ID DESC)
-- Add temp_tokens
INSERT INTO temp_tokens
  (token, email, create_date, expiry_date, _user)
VALUES
  (54379073478005177501523550744865+@i+1, 'Ann.VAN-AARDEN@ec.europa.eu', 1523550744865, 1523557944865, @u_id)
-- Add temp_token link
SET @t_id = (SELECT TOP 1
  id
FROM temp_tokens
ORDER BY ID DESC)
-- Add right
INSERT INTO rights
  (userId,rightdesc, type)
VALUES
  (@u_id, 'users_'+@u_id, 0),
  (@u_id, 'municipalities_'+@m_id, 3),
  (@u_id, 'registrations_'+@r_id, 3),
  (@u_id, 'temp_tokens_'+@t_id, 3),
  (@u_id, 'mayors_'+@t_id, 3)
-- Add user
INSERT INTO users
  (treatment, name, surname, address, address_num, postal_code, email, password, create_date, access_date, ecas_email, ecas_username, type, verified, lang)
VALUES
  ('', 'aardepe', 'aardepe', 'Diagonal', 'Diagonal', '605', '08028', 'Peter.Aardema@ec.europa.eu', 1523537782500, 1523537782500, 'Peter.Aardema@ec.europa.eu', 'aardepe', 3, 0, 'en')
-- Add municipality
INSERT INTO municipalities
  (name,address,address_num,postal_code,country,lau)
VALUES
  ('Barcelona', 'Diagonal', '605', '08028', 'ESPAÑA', 35511)
-- Add user and municipality link
SET @u_id = (SELECT TOP 1
  id
FROM users
ORDER BY ID DESC)
SET @m_id = (SELECT TOP 1
  id
FROM municipalities
ORDER BY ID DESC)
-- Add mayor
INSERT INTO mayors
  (treatment, name, surname, email, municipality)
VALUES
  (NULL, 'aardepe', 'aardepe', 'Peter.Aardema@ec.europa.eu', @m_id)
-- Add registration
INSERT INTO registrations
  (_user, municipality, role, _status, legal_file1, legal_file2, legal_file3, legal_file4, ip_registration, organisation_id, association_name, upload_time, allFiles_flag, mail_counter)
VALUES(@u_id, @m_id, 'Representative', 0, 'data:application/pdf;base64,JVBERi0xLjQNJeLjz9', NULL, 'data:application/pdf;base64,JVBERi0xLjQNJeLjz9', NULL, '127.0.0.1', 0, '', 0, 1, 0)
-- Add registration link
SET @r_id = (SELECT TOP 1
  id
FROM users
ORDER BY ID DESC)
-- Add temp_tokens
INSERT INTO temp_tokens
  (token, email, create_date, expiry_date, _user)
VALUES
  (54379073478005177501523550744865+@i+1, 'Peter.Aardema@ec.europa.eu', 1523550744865, 1523557944865, @u_id)
-- Add temp_token link
SET @t_id = (SELECT TOP 1
  id
FROM temp_tokens
ORDER BY ID DESC)
-- Add right
INSERT INTO rights
  (userId,rightdesc, type)
VALUES
  (@u_id, 'users_'+@u_id, 0),
  (@u_id, 'municipalities_'+@m_id, 3),
  (@u_id, 'registrations_'+@r_id, 3),
  (@u_id, 'temp_tokens_'+@t_id, 3),
  (@u_id, 'mayors_'+@t_id, 3)