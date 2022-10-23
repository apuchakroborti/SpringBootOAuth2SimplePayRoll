CREATE TABLE EMPLOYEE(
    ID bigint not null auto_increment,
    FIRST_NAME	varchar(255) not null,
    LAST_NAME	varchar(255),
    EMAIL	varchar(255) not null,
    TIN	varchar(255),
    NID	varchar(255),
    PASSPORT	varchar(255),
    USERNAME	varchar(255) not null,
    PASSWORD	varchar(255) not null,
    DATE_OF_JOINING datetime(6) not null,
    DESIGNATION	varchar(255),
    PHONE	varchar(255),
    ROLE_ID int not null,
    STATUS	varchar(16) not null,

    CREATED_BY	varchar(64),
    CREATE_TIME	datetime(6),
    EDITED_BY	varchar(64),
    EDIT_TIME datetime(6),
    primary key (ID)
) engine=InnoDB;

CREATE TABLE EMPLOYEE_PROVIDENT_FUND(
    ID bigint not null auto_increment,
    EMPLOYEE_ID bigint not null,
    EMPLOYEE_CONTRIBUTION double,
    COMPANY_CONTRIBUTION double,
    COMMENTS	varchar(128),
    FROM_DATE	datetime(6) not null,
    TO_DATE	    datetime(6) not null,

    CREATED_BY	varchar(64),
    CREATE_TIME	datetime(6),
    EDITED_BY	varchar(64),
    EDIT_TIME datetime(6),
    primary key (ID)
) engine=InnoDB;

CREATE TABLE EMPLOYEE_TAX_DEPOSIT(
    ID bigint not null auto_increment,
    EMPLOYEE_ID bigint not null,
    AMOUNT double not null,
    CHALAN_NO varchar(64) not null,
    COMMENTS	varchar(128),
    FROM_DATE	datetime(6) not null,
    TO_DATE	    datetime(6) not null,

    CREATED_BY	varchar(64),
    CREATE_TIME	datetime(6),
    EDITED_BY	varchar(64),
    EDIT_TIME datetime(6),
    primary key (ID)
) engine=InnoDB;

CREATE TABLE EMPLOYEE_SALARY(
    ID bigint not null auto_increment,
    EMPLOYEE_ID bigint not null,
    BASIC_SALARY double,
    GROSS_SALARY double,
    STATUS varchar(16),
    COMMENTS	varchar(128),
    FROM_DATE	datetime(6) not null,
    TO_DATE	    datetime(6),

    CREATED_BY	varchar(64),
    CREATE_TIME	datetime(6),
    EDITED_BY	varchar(64),
    EDIT_TIME datetime(6),
    primary key (ID)
) engine=InnoDB;

CREATE TABLE EMPLOYEE_MONTHLY_PAY_SLIP(
    ID bigint not null auto_increment,
    EMPLOYEE_ID bigint not null,
    GROSS_SALARY double not null,
    BASIC_SALARY double not null,
    HOUSE_RENT	double,
    CONVEYANCE	double,
    MEDICAL	double,
    DUE	double,
    PF_DEDUCTION double,
    TAX_DEDUCTION	double,
    ARREARS	double,
    FESTIVAL_BONUS double,
    INCENTIVE_BONUS	double,
    OTHER_PAY	double,
    NET_PAYMENT double,
    STATUS varchar(16) not null,
    COMMENTS	varchar(128),
    FROM_DATE	datetime(6) not null,
    TO_DATE	    datetime(6) not null,

    CREATED_BY	varchar(64),
    CREATE_TIME	datetime(6),
    EDITED_BY	varchar(64),
    EDIT_TIME datetime(6),
    primary key (ID)
) engine=InnoDB;

CREATE TABLE ROLES(
    ID bigint not null auto_increment,
    LEVEL varchar(128),
    STATUS varchar(128),
    CREATED_BY	varchar(64),
    CREATE_TIME	datetime(6),
    EDITED_BY	varchar(64),
    EDIT_TIME datetime(6),
    primary key (ID)
) engine=InnoDB;
