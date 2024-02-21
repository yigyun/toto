
CREATE TABLE Board (
                       bno BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                       moddate TIMESTAMP NULL,
                       created TIMESTAMP NULL,
                       auction_start_time TIMESTAMP NULL,
                       auction_status VARCHAR(255) NULL,
                       board_category VARCHAR(255) NULL,
                       book_mark_count BIGINT NULL,
                       content VARCHAR(2000) NOT NULL,
                       price BIGINT NULL,
                       title VARCHAR(500) NOT NULL,
                       writer VARCHAR(50) NOT NULL
);

CREATE TABLE BoardImage (
                            uuid VARCHAR(255) NOT NULL PRIMARY KEY,
                            fileName VARCHAR(255) NULL,
                            ord INT NOT NULL,
                            board_bno BIGINT NULL,
                            FOREIGN KEY (board_bno) REFERENCES Board (bno)
);

CREATE TABLE BoardImage (
                            uuid VARCHAR(255) PRIMARY KEY,
                            fileName VARCHAR(255) NOT NULL,
                            ord INT NOT NULL,
                            bno BIGINT,
                            FOREIGN KEY (bno) REFERENCES Board (bno)
);

CREATE TABLE Member (
                        mid VARCHAR(255) NOT NULL PRIMARY KEY,
                        moddate TIMESTAMP NULL,
                        created TIMESTAMP NULL,
                        del BOOLEAN NOT NULL DEFAULT 0,
                        email VARCHAR(255) NULL,
                        mpassword VARCHAR(255) NULL,
                        social BOOLEAN NOT NULL DEFAULT 0
);


CREATE TABLE Bid (
                     bid_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                     price BIGINT NOT NULL,
                     bno BIGINT NOT NULL,
                     mid VARCHAR(255) NOT NULL,
                     FOREIGN KEY (bno) REFERENCES Board (bno),
                     FOREIGN KEY (mid) REFERENCES Member (mid)
);

CREATE TABLE BookMark (
                          bid BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                          bno BIGINT NULL,
                          mid VARCHAR(255) NOT NULL,
                          FOREIGN KEY (bno) REFERENCES Board (bno),
                          FOREIGN KEY (mid) REFERENCES Member (mid)
);
-- spring security 용
CREATE TABLE MEMBER_ROLE_SET (
                                 MEMBER_MID VARCHAR(255) NOT NULL,
                                 ROLE_SET INT NULL,
                                 FOREIGN KEY (MEMBER_MID) REFERENCES Member (mid)
);

CREATE TABLE USER  (
                                 ID BIGINT NOT NULL PRIMARY KEY,
                                 CREATED_DATE TIMESTAMP NULL,
                                 MODIFIED_DATE TIMESTAMP NULL,
                                 CREATED_AT TIMESTAMP NULL,
                                 EMAIL VARCHAR(512) NOT NULL UNIQUE,
                                 EMAIL_VERIFIED BOOLEAN NOT NULL,
                                 IMG_URL TEXT NULL,
                                 LOCATION VARCHAR(255) NULL,
                                 MANNER DOUBLE NOT NULL,
                                 MODIFIED_AT TIMESTAMP NOT NULL,
                                 NICK VARCHAR(255) NOT NULL UNIQUE,
                                 PASSWORD VARCHAR(128) NOT NULL,
                                 PROVIDER_TYPE VARCHAR(20) NOT NULL,
                                 ROLE_TYPE VARCHAR(20) NOT NULL,
                                 USER_ID VARCHAR(64) NOT NULL UNIQUE,
                                 USERNAME VARCHAR(100) NOT NULL
);

CREATE TABLE USERS (
                       USER_ID BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                       AGE INT NOT NULL,
                       CITY VARCHAR(255) NULL,
                       EMAIL VARCHAR(255) NULL,
                       IMAGE_URL VARCHAR(255) NULL,
                       NICKNAME VARCHAR(255) NULL,
                       PASSWORD VARCHAR(255) NULL,
                       REFRESH_TOKEN VARCHAR(255) NULL,
                       ROLE VARCHAR(255) NULL,
                       SOCIAL_ID VARCHAR(255) NULL,
                       SOCIAL_TYPE VARCHAR(255) NULL
);

CREATE TABLE USER_REFRESH_TOKEN (
                                    REFRESH_ID BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                    REFRESH_TOKEN VARCHAR(256) NOT NULL,
                                    USER_ID VARCHAR(64) NOT NULL UNIQUE
);