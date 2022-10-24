drop table if exists EMPLOYEE;
drop table if exists EMPLOYEE_PROVIDENT_FUND;
drop table if exists EMPLOYEE_TAX_DEPOSIT;
drop table if exists EMPLOYEE_SALARY;
drop table if exists EMPLOYEE_MONTHLY_PAY_SLIP;

CREATE TABLE EMPLOYEE(
    ID              bigint not null auto_increment,
    USER_ID         VARCHAR(255) NOT NULL UNIQUE,
    FIRST_NAME	    varchar(255) not null,
    LAST_NAME	    varchar(255),
    EMAIL	        varchar(128) not null,
    PHONE	        varchar(32),
    TIN	            varchar(255),
    NID	            varchar(255),
    PASSPORT	    varchar(64),
    DATE_OF_JOINING datetime not null,
    DESIGNATION_ID	int,
    ADDRESS_ID	    int,

    STATUS	        BOOLEAN DEFAULT TRUE,

    CREATED_BY	    bigint NOT NULL,
    CREATE_TIME	    datetime NOT NULL,
    EDITED_BY	    bigint,
    EDIT_TIME       datetime,
    INTERNAL_VERSION bigint default 1,
    oauth_user_id     bigint,
    primary key (ID)
) engine=InnoDB;

CREATE TABLE EMPLOYEE_PROVIDENT_FUND(
    ID bigint not null auto_increment,
    EMPLOYEE_ID bigint not null,
    EMPLOYEE_CONTRIBUTION double,
    COMPANY_CONTRIBUTION double,
    COMMENTS	varchar(128),
    FROM_DATE	datetime not null,
    TO_DATE	    datetime not null,

    CREATED_BY	    bigint NOT NULL,
    CREATE_TIME	    datetime NOT NULL,
    EDITED_BY	    bigint,
    EDIT_TIME       datetime,
    INTERNAL_VERSION bigint default 1,
    primary key (ID)
) engine=InnoDB;

CREATE TABLE EMPLOYEE_TAX_DEPOSIT(
    ID              bigint not null auto_increment,
    EMPLOYEE_ID     bigint not null,
    AMOUNT          double not null,
    CHALAN_NO       varchar(64) not null,
    COMMENTS	    varchar(255),
    FROM_DATE	    datetime not null,
    TO_DATE	        datetime not null,

    CREATED_BY	    bigint NOT NULL,
    CREATE_TIME	    datetime NOT NULL,
    EDITED_BY	    bigint,
    EDIT_TIME       datetime,
    INTERNAL_VERSION bigint default 1,
    primary key (ID)
) engine=InnoDB;

CREATE TABLE EMPLOYEE_SALARY(
    ID              bigint not null auto_increment,
    EMPLOYEE_ID     bigint not null,
    BASIC_SALARY    double not null,
    GROSS_SALARY    double not null,
    STATUS          boolean default true,
    COMMENTS	    varchar(255),
    FROM_DATE	    datetime not null,
    TO_DATE	        datetime,

    CREATED_BY	    bigint NOT NULL,
    CREATE_TIME	    datetime NOT NULL,
    EDITED_BY	    bigint,
    EDIT_TIME       datetime,
    INTERNAL_VERSION bigint default 1,
    primary key (ID)
) engine=InnoDB;

CREATE TABLE EMPLOYEE_MONTHLY_PAY_SLIP(
    ID                  bigint not null auto_increment,
    EMPLOYEE_ID         bigint not null,
    GROSS_SALARY        double not null,
    BASIC_SALARY        double not null,
    HOUSE_RENT	        double,
    CONVEYANCE	        double,
    MEDICAL_ALLOWANCE	double,
    DUE	                double,
    PF_DEDUCTION        double,
    TAX_DEDUCTION	    double,
    ARREARS	            double,
    FESTIVAL_BONUS      double,
    INCENTIVE_BONUS	    double,
    OTHER_PAY	        double,
    NET_PAYMENT         double,
    STATUS              boolean default true,
    COMMENTS	        varchar(255),
    FROM_DATE	        datetime not null,
    TO_DATE	            datetime not null,

    CREATED_BY	        bigint NOT NULL,
    CREATE_TIME	        datetime NOT NULL,
    EDITED_BY	        bigint,
    EDIT_TIME           datetime,
    INTERNAL_VERSION    bigint default 1,
    primary key (ID)
) engine=InnoDB;
