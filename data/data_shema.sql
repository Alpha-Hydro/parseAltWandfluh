USE wandfluh;

ALTER DATABASE wandfluh CHARACTER SET utf8 COLLATE utf8_general_ci;

CREATE TABLE category(
  id INT AUTO_INCREMENT PRIMARY KEY,
  parent_id INT(11) NULL,
  name VARCHAR(45) NULL,
  image VARCHAR(45) NULL,
  description VARCHAR(258) NULL,
  CONSTRAINT id_UNIQUE
  UNIQUE (id)
)
  ENGINE = innoDB
  DEFAULT CHARACTER SET = utf8
;

CREATE TABLE category_properties(
  id INT AUTO_INCREMENT PRIMARY KEY ,
  category_id INT(11) NULL ,
  value VARCHAR(258) NULL ,
  CONSTRAINT id_UNIQUE
  UNIQUE (id)
)
  ENGINE = innoDB
  DEFAULT CHARACTER SET = utf8
;

CREATE TABLE product(
  id INT AUTO_INCREMENT PRIMARY KEY ,
  category_id INT(11) NULL ,
  name VARCHAR(45) NULL ,
  data_sheet_no VARCHAR(11) NULL ,
  data_sheet_pdf VARCHAR(45) NULL ,
  construction_id INT DEFAULT NULL ,
  size_id INT DEFAULT NULL ,
  type_id INT DEFAULT NULL ,
  CONSTRAINT id_UNIQUE
  UNIQUE (id)
)
  ENGINE = innoDB
  DEFAULT CHARACTER SET = utf8
;

CREATE TABLE product_construction(
  id INT AUTO_INCREMENT PRIMARY KEY ,
  name VARCHAR(45) NULL ,
  CONSTRAINT id_UNIQUE
  UNIQUE (id)
)
  ENGINE = innoDB
  DEFAULT CHARACTER SET = utf8
;
ALTER TABLE product ADD FOREIGN KEY (construction_id) REFERENCES product_construction(id);

CREATE TABLE product_size(
  id INT AUTO_INCREMENT PRIMARY KEY ,
  name VARCHAR(45) NULL ,
  CONSTRAINT id_UNIQUE
  UNIQUE (id)
)
  ENGINE = innoDB
  DEFAULT CHARACTER SET = utf8
;
ALTER TABLE product ADD FOREIGN KEY (size_id) REFERENCES product_size(id);

CREATE TABLE product_type(
  id INT AUTO_INCREMENT PRIMARY KEY ,
  name VARCHAR(45) NULL ,
  CONSTRAINT id_UNIQUE
  UNIQUE (id)
)
  ENGINE = innoDB
  DEFAULT CHARACTER SET = utf8
;
ALTER TABLE product ADD FOREIGN KEY (type_id) REFERENCES product_type(id);