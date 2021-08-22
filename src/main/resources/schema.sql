DROP TABLE IF EXISTS product;
CREATE TABLE product (
    product_id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    total_investing_amount INT NOT NULL DEFAULT  0,
    current_investing_amount INT NOT NULL DEFAULT  0,
    total_invester INT NOT NULL DEFAULT  0,
    status VARCHAR(255) NOT NULL DEFAULT 'RECRUITMENT',
    started_at DATETIME NOT NULL,
    finished_at DATETIME NOT NULL
) ENGINE = InnoDB, CHARSET=utf8;

DROP TABLE IF EXISTS investment;
CREATE TABLE investment (
    investment_id INT PRIMARY KEY AUTO_INCREMENT,
    product_id INT NOT NULL,
    user_id INT NOT NULL,
    investment_amount INT NOT NULL,
    investment_at DATETIME NOT NULL
) ENGINE = InnoDB, CHARSET=utf8;