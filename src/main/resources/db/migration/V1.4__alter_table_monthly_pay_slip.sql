alter table EMPLOYEE_MONTHLY_PAY_SLIP drop column TAX_DEDUCTION;
alter table EMPLOYEE_TAX_DEPOSIT add column MONTHLY_PAYSLIP_ID bigint;
