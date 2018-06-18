DROP TABLE IF EXISTS tbl_product;
CREATE TABLE tbl_product (
    item_id BIGINT NOT NULL AUTO_INCREMENT,
    item_name VARCHAR(45) NULL,
    item_descr VARCHAR(128) NOT NULL,
    item_img VARCHAR(128) NULL,
    item_price DECIMAL(20,2) NOT NULL,
    PRIMARY KEY(item_id)
);