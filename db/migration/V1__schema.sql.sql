CREATE TABLE MEMBER (
                        MEMBER_ID BIGINT PRIMARY KEY AUTO_INCREMENT,
                        USER_EMAIL VARCHAR(255) UNIQUE NOT NULL,
                        USER_PASSWORD VARCHAR(255) NOT NULL,
                        USER_NAME VARCHAR(100),
                        REGISTRATION_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        STATUS VARCHAR(20) NOT NULL
);

CREATE TABLE REVIEWS (
                         REVIEW_ID BIGINT PRIMARY KEY AUTO_INCREMENT,
                         CONTENT TEXT NOT NULL,
                         CREATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         MEMBER_ID BIGINT NOT NULL,
                         PRODUCT_ID BIGINT NOT NULL,
                         FOREIGN KEY (MEMBER_ID) REFERENCES MEMBER(MEMBER_ID),
                         FOREIGN KEY (PRODUCT_ID) REFERENCES PRODUCTS(PRODUCT_ID)
);

CREATE TABLE PRODUCT_SIZE (
                              PRODUCT_SIZE_ID BIGINT PRIMARY KEY AUTO_INCREMENT,
                              SIZE_NAME VARCHAR(10) NOT NULL
);

CREATE TABLE PRODUCT_COLORS (
                                PRODUCT_COLOR_ID BIGINT PRIMARY KEY AUTO_INCREMENT,
                                COLOR VARCHAR(50) NOT NULL
);

CREATE TABLE PRODUCT_OPTION (
                                PRODUCT_OPTION_ID BIGINT PRIMARY KEY AUTO_INCREMENT,
                                STOCK_QUANTITY INT,
                                PRODUCT_COLOR_ID BIGINT,
                                PRODUCT_SIZE_ID BIGINT,
                                PRODUCT_ID BIGINT,
                                FOREIGN KEY (PRODUCT_COLOR_ID) REFERENCES PRODUCT_COLORS(PRODUCT_COLOR_ID),
                                FOREIGN KEY (PRODUCT_SIZE_ID) REFERENCES PRODUCT_SIZE(PRODUCT_SIZE_ID),
                                FOREIGN KEY (PRODUCT_ID) REFERENCES PRODUCTS(PRODUCT_ID)
);

CREATE TABLE PRODUCTS (
                          PRODUCT_ID BIGINT PRIMARY KEY AUTO_INCREMENT,
                          PRODUCT_NAME VARCHAR(100) NOT NULL,
                          PRODUCT_IMAGE_1 VARCHAR(255) NOT NULL,
                          PRODUCT_IMAGE_2 VARCHAR(255) NOT NULL,
                          PRODUCT_IMAGE_3 VARCHAR(255) NOT NULL,
                          PRODUCT_DESCRIPTION TEXT NOT NULL,
                          PRODUCT_PRICE BIGINT NOT NULL,
                          CATEGORY_ID BIGINT NOT NULL,
                          FOREIGN KEY (CATEGORY_ID) REFERENCES CATEGORYS(CATEGORY_ID)
);

CREATE TABLE CATEGORYS (
                           CATEGORY_ID BIGINT PRIMARY KEY AUTO_INCREMENT,
                           CATEGORY_NAME VARCHAR(50) NOT NULL
);

CREATE TABLE ORDER_ITEMS (
                             ORDER_ITEM_ID BIGINT PRIMARY KEY AUTO_INCREMENT,
                             ORDER_ID BIGINT NOT NULL,
                             PRODUCT_ID BIGINT NOT NULL,
                             PRODUCT_OPTION_ID BIGINT NOT NULL,
                             QUANTITY INT NOT NULL,
                             PRODUCT_NAME VARCHAR(100) NOT NULL,
                             PRICE BIGINT NOT NULL,
                             PRODUCT_IMG VARCHAR(255) NOT NULL,
                             FOREIGN KEY (ORDER_ID) REFERENCES ORDERS(ORDER_ID),
                             FOREIGN KEY (PRODUCT_ID) REFERENCES PRODUCTS(PRODUCT_ID),
                             FOREIGN KEY (PRODUCT_OPTION_ID) REFERENCES PRODUCT_OPTION(PRODUCT_OPTION_ID)
);

CREATE TABLE ORDERS (
                        ORDER_ID BIGINT PRIMARY KEY AUTO_INCREMENT,
                        MEMBER_ID BIGINT NOT NULL,
                        MERCHAT_UID VARCHAR(50) UNIQUE NOT NULL,
                        IMP_UID VARCHAR(50),
                        RECIPIENT_NAME VARCHAR(100) NOT NULL,
                        DELIVERY_ADDRESS TEXT NOT NULL,
                        DELIVERY_DETAIL TEXT NOT NULL,
                        DELIVERY_ZIPCODE VARCHAR(10) NOT NULL,
                        PHONE_NUMBER VARCHAR(11) NOT NULL,
                        ORDER_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        STATUS VARCHAR(10) NOT NULL,
                        TOTAL_PRICE BIGINT NOT NULL,
                        REQUEST_MESSAGE TEXT,
                        FOREIGN KEY (MEMBER_ID) REFERENCES MEMBER(MEMBER_ID)
);

CREATE TABLE MEMBER_ADDRESSES (
                                  MEMBER_ADDRESS_ID BIGINT PRIMARY KEY AUTO_INCREMENT,
                                  MEMBER_ID BIGINT NOT NULL,
                                  ZIPCODE VARCHAR(10),
                                  ADDRESS TEXT NOT NULL,
                                  ADDRESS_DETAIL TEXT NOT NULL,
                                  RECIPIENT_TEL VARCHAR(11),
                                  IS_DEFAULT BOOLEAN,
                                  FOREIGN KEY (MEMBER_ID) REFERENCES MEMBER(MEMBER_ID)
);

CREATE TABLE COMMENTS (
                          COMMENT_ID BIGINT PRIMARY KEY AUTO_INCREMENT,
                          CONTENT TEXT NOT NULL,
                          CREATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          MEMBER_ID BIGINT NOT NULL,
                          BOARD_ID BIGINT NOT NULL,
                          PARENT_COMMENT_ID BIGINT,
                          FOREIGN KEY (MEMBER_ID) REFERENCES MEMBER(MEMBER_ID),
                          FOREIGN KEY (BOARD_ID) REFERENCES BOARD(BOARD_ID),
                          FOREIGN KEY (PARENT_COMMENT_ID) REFERENCES COMMENTS(COMMENT_ID)
);
