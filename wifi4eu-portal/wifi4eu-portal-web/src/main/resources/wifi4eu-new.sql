-- MySQL Script generated by MySQL Workbench
-- Wed Oct 11 14:33:16 2017
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema wifi4eu-new
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `wifi4eu-new`;
-- -----------------------------------------------------
-- Schema wifi4eu-new
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `wifi4eu-new` DEFAULT CHARACTER SET utf8 COLLATE utf8_bin ;
USE `wifi4eu-new` ;

CREATE TABLE IF NOT EXISTS `wifi4eu-new`.`SEQUENCE` (
  `SEQ_NAME`  VARCHAR(50) NOT NULL,
  `SEQ_COUNT` NUMERIC(38),
  PRIMARY KEY (`SEQ_NAME`)
);


-- -----------------------------------------------------
-- Table `wifi4eu-new`.`audit_data_t`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `AUDIT_DATA_T` (
  `AUDIT_DATA_ID` int(11) NOT NULL,
  `REQUEST_ENDPOINT` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `REQUEST_METHOD` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `REQUEST_BODY` blob,
  `RESPONSE_BODY` blob,
  `USER_ID` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;


-- -----------------------------------------------------
-- Table `wifi4eu-new`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `wifi4eu-new`.`users` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `treatment` VARCHAR(45) NULL,
  `name` VARCHAR(255) NULL,
  `surname` VARCHAR(255) NULL,
  `email` VARCHAR(255) NULL,
  `password` VARCHAR(255) NULL,
  `create_date` BIGINT NULL,
  `access_date` BIGINT NULL,
  `type` INT NULL DEFAULT NULL,
  `verified` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `wifi4eu-new`.`laus`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `wifi4eu-new`.`laus` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `country_code` VARCHAR(255) NULL,
  `nuts3` VARCHAR(255) NULL,
  `lau1` VARCHAR(255) NULL,
  `lau2` VARCHAR(255) NULL,
  `_change` VARCHAR(255) NULL,
  `name1` VARCHAR(255) NULL,
  `name2` VARCHAR(255) NULL,
  `pop` INT NULL,
  `area` INT NULL,
  `physical_address` VARCHAR(255) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `wifi4eu-new`.`municipalities`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `wifi4eu-new`.`municipalities` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NULL,
  `address` VARCHAR(255) NULL,
  `address_num` VARCHAR(255) NULL,
  `postal_code` VARCHAR(255) NULL,
  `country` VARCHAR(255) NULL,
  `lau` INT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_municipalities_laus1_idx` (`lau` ASC),
  CONSTRAINT `fk_municipalities_laus`
    FOREIGN KEY (`lau`)
    REFERENCES `wifi4eu-new`.`laus` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `wifi4eu-new`.`registrations`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `wifi4eu-new`.`registrations` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user` INT NOT NULL,
  `municipality` INT NOT NULL,
  `role` VARCHAR(500) NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_user_idx` (`user` ASC),
  INDEX `fk_municipality_idx` (`municipality` ASC),
  CONSTRAINT `fk_registrations_users`
    FOREIGN KEY (`user`)
    REFERENCES `wifi4eu-new`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_registrations_municipalities`
    FOREIGN KEY (`municipality`)
    REFERENCES `wifi4eu-new`.`municipalities` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `wifi4eu-new`.`calls`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `wifi4eu-new`.`calls` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `event` VARCHAR(500) NULL,
  `start_date` BIGINT NULL,
  `end_date` BIGINT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `wifi4eu-new`.`nuts`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `wifi4eu-new`.`nuts` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `code` VARCHAR(255) NULL,
  `label` VARCHAR(255) NULL,
  `level` INT NULL,
  `country_code` VARCHAR(255) NULL,
  `_order` INT NULL,
  `sorting` INT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `wifi4eu-new`.`threads`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `wifi4eu-new`.`threads` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(255) NULL,
  `municipality` INT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_threads_municipalities_idx` (`municipality` ASC),
  CONSTRAINT `fk_threads_municipalities`
    FOREIGN KEY (`municipality`)
    REFERENCES `wifi4eu-new`.`municipalities` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `wifi4eu-new`.`thread_messages`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `wifi4eu-new`.`thread_messages` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `thread` INT NOT NULL,
  `author` INT NOT NULL,
  `message` MEDIUMTEXT NULL,
  `create_date` BIGINT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_thread_idx` (`thread` ASC),
  INDEX `fk_author_idx` (`author` ASC),
  CONSTRAINT `fk_thread_messages_threads`
    FOREIGN KEY (`thread`)
    REFERENCES `wifi4eu-new`.`threads` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_thread_messages_users`
    FOREIGN KEY (`author`)
    REFERENCES `wifi4eu-new`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `wifi4eu-new`.`suppliers`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `wifi4eu-new`.`suppliers` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NULL,
  `address` VARCHAR(255) NULL,
  `vat` VARCHAR(255) NULL,
  `bic` VARCHAR(255) NULL,
  `account_number` VARCHAR(255) NULL,
  `website` VARCHAR(255) NULL,
  `contact_name` VARCHAR(255) NULL,
  `contact_surname` VARCHAR(255) NULL,
  `contact_phone_prefix` VARCHAR(255) NULL,
  `contact_phone_number` VARCHAR(255) NULL,
  `contact_email` VARCHAR(255) NULL,
  `logo` LONGTEXT NULL,
  `user` INT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_suppliers_users_idx` (`user` ASC),
  CONSTRAINT `fk_suppliers_users`
    FOREIGN KEY (`user`)
    REFERENCES `wifi4eu-new`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_bin;


-- -----------------------------------------------------
-- Table `wifi4eu-new`.`applications`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `wifi4eu-new`.`applications` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `call_id` INT NOT NULL,
  `registration` INT NOT NULL,
  `supplier` INT NULL,
  `voucher_awarded` TINYINT NULL DEFAULT 0,
  `date` BIGINT NULL,
  `lef_export` BIGINT NULL,
  `lef_import` BIGINT NULL,
  `lef_status` INT NULL DEFAULT 0,
  `bc_export` BIGINT NULL,
  `bc_import` BIGINT NULL,
  `bc_status` INT NULL DEFAULT 0,
  `lc_export` BIGINT NULL,
  `lc_import` BIGINT NULL,
  `lc_status` INT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  INDEX `fk_call_idx` (`call_id` ASC),
  INDEX `fk_registration_idx` (`registration` ASC),
  INDEX `fk_applications_supplier_idx` (`supplier` ASC),
  CONSTRAINT `fk_applications_calls`
    FOREIGN KEY (`call_id`)
    REFERENCES `wifi4eu-new`.`calls` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_applications_registrations`
    FOREIGN KEY (`registration`)
    REFERENCES `wifi4eu-new`.`registrations` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_applications_suppliers`
    FOREIGN KEY (`supplier`)
    REFERENCES `wifi4eu-new`.`suppliers` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `wifi4eu-new`.`mayors`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `wifi4eu-new`.`mayors` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NULL,
  `surname` VARCHAR(255) NULL,
  `treatment` VARCHAR(255) NULL,
  `email` VARCHAR(255) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `wifi4eu-new`.`representations`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `wifi4eu-new`.`representations` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `municipality` INT NOT NULL,
  `mayor` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_municipality_idx` (`municipality` ASC),
  INDEX `fk_mayor_idx` (`mayor` ASC),
  CONSTRAINT `fk_representations_municipalities`
    FOREIGN KEY (`municipality`)
    REFERENCES `wifi4eu-new`.`municipalities` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_representations_mayors`
    FOREIGN KEY (`mayor`)
    REFERENCES `wifi4eu-new`.`mayors` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `wifi4eu-new`.`access_points`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `wifi4eu-new`.`access_points` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `municipality` INT NULL,
  `name` VARCHAR(255) NULL,
  `product_name` VARCHAR(255) NULL,
  `model_number` VARCHAR(255) NULL,
  `serial_number` VARCHAR(255) NULL,
  `indoor` TINYINT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  INDEX `fk_municipality_idx` (`municipality` ASC),
  CONSTRAINT `fk_access_points_municipalities`
    FOREIGN KEY (`municipality`)
    REFERENCES `wifi4eu-new`.`municipalities` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `wifi4eu-new`.`supplied_regions`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `wifi4eu-new`.`supplied_regions` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `supplier` INT NOT NULL,
  `region` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_supplier_regions_supplier_idx` (`supplier` ASC),
  INDEX `fk_supplier_regions_region_idx` (`region` ASC),
  CONSTRAINT `fk_supplier_regions_suppliers`
    FOREIGN KEY (`supplier`)
    REFERENCES `wifi4eu-new`.`suppliers` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_supplier_regions_nuts`
    FOREIGN KEY (`region`)
    REFERENCES `wifi4eu-new`.`nuts` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_bin;


-- -----------------------------------------------------
-- Table `wifi4eu-new`.`helpdesk_issues`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `wifi4eu-new`.`helpdesk_issues` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `from_email` VARCHAR(255) NULL,
  `assigned_to` VARCHAR(255) NULL,
  `topic` VARCHAR(255) NULL,
  `portal` VARCHAR(255) NULL,
  `member_state` VARCHAR(255) NULL,
  `summary` MEDIUMTEXT NULL,
  `create_date` BIGINT NULL,
  `status` INT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_bin;


-- -----------------------------------------------------
-- Table `wifi4eu-new`.`helpdesk_comments`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `wifi4eu-new`.`helpdesk_comments` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `issue` INT NULL,
  `from_email` VARCHAR(255) NULL,
  `comment` MEDIUMTEXT NULL,
  `create_date` BIGINT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_helpdesk_comments_issue_idx` (`issue` ASC),
  CONSTRAINT `fk_helpdesk_comments_issues`
    FOREIGN KEY (`issue`)
    REFERENCES `wifi4eu-new`.`helpdesk_issues` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_bin;


-- -----------------------------------------------------
-- Table `wifi4eu-new`.`timelines`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `wifi4eu-new`.`timelines` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `call_id` INT NULL,
  `description` VARCHAR(255) NULL,
  `start_date` BIGINT NULL,
  `end_date` BIGINT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_timelines_call_id_idx` (`call_id` ASC),
  CONSTRAINT `fk_timelines_call_id`
    FOREIGN KEY (`call_id`)
    REFERENCES `wifi4eu-new`.`calls` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_bin;


-- -----------------------------------------------------
-- Table `wifi4eu-new`.`temp_tokens`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `wifi4eu-new`.`temp_tokens` (
  `id` BIGINT NOT NULL,
  `token` VARCHAR(255) NULL,
  `email` VARCHAR(255) NULL,
  `create_date` BIGINT NULL,
  `expiry_date` BIGINT NULL,
  `user` INT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_temp_tokens_users_idx` (`user` ASC),
  CONSTRAINT `fk_temp_tokens_users`
    FOREIGN KEY (`user`)
    REFERENCES `wifi4eu-new`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `wifi4eu-new`.`organizations`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `wifi4eu-new`.`organizations` (
  `id` INT NOT NULL,
  `name` VARCHAR(255) NULL,
  `type` VARCHAR(255) NULL,
  `country` VARCHAR(255) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;



SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
