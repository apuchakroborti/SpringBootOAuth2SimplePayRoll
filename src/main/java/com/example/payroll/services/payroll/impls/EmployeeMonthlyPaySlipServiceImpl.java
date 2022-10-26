package com.example.payroll.services.payroll.impls;


import com.example.payroll.dto.MonthlyPaySlipDto;
import com.example.payroll.dto.request.MonthlyPaySlipRequestDto;
import com.example.payroll.dto.request.PayslipSearchCriteria;
import com.example.payroll.exceptions.GenericException;
import com.example.payroll.models.payroll.*;
import com.example.payroll.repository.payroll.EmployeeTaxDepositRepository;
import com.example.payroll.repository.payroll.MonthlyPaySlipRepository;
import com.example.payroll.repository.payroll.EmployeeRepository;
import com.example.payroll.repository.payroll.EmployeeSalaryRepository;
import com.example.payroll.services.payroll.EmployeeMonthlyPaySlipService;
import com.example.payroll.services.payroll.ProvidentFundService;
import com.example.payroll.utils.Defs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmployeeMonthlyPaySlipServiceImpl implements EmployeeMonthlyPaySlipService {
    @Autowired
    MonthlyPaySlipRepository monthlyPaySlipRepository;

    @Autowired
    ProvidentFundService providentFundService;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    EmployeeSalaryRepository employeeSalaryRepository;

    @Autowired
    EmployeeTaxDepositRepository taxDepositRepository;

    //TODO need to implement payslip generation at the starting of a financial year

    //TODO need to implement
    @Override
    public MonthlyPaySlipDto createPaySlip(MonthlyPaySlipRequestDto monthlyPaySlipRequestDto) throws GenericException {
        Optional<Employee> optionalEmployee = employeeRepository.findById(monthlyPaySlipRequestDto.getEmployee().getId());
        if(!optionalEmployee.isPresent())throw new GenericException(Defs.USER_NOT_FOUND);
        EmployeeSalary employeeSalary = employeeSalaryRepository.getEmployeeCurrentSalaryByEmployeeId(optionalEmployee.get().getId());

        ProvidentFund providentFund = providentFundService.insertPfData(employeeSalary, monthlyPaySlipRequestDto.getFromDate());

        MonthlyPaySlip monthlyPaySlip = new MonthlyPaySlip();

        monthlyPaySlip.setProvidentFund(providentFund);


        monthlyPaySlip = monthlyPaySlipRepository.save(monthlyPaySlip);

        MonthlyPaySlipDto monthlyPaySlipDto = new MonthlyPaySlipDto();
        return monthlyPaySlipDto;

    }
    @Override
    public Page<MonthlyPaySlip> getPaySlipWithInDateRangeAndEmployeeId(PayslipSearchCriteria criteria, Pageable pageable) throws GenericException{
        Page<MonthlyPaySlip> employeeMonthlyPaySlipPage = monthlyPaySlipRepository.getEmployeeMonthlyPaySlipByDateRangeAndEmployeeId(
                criteria.getFromDate(), criteria.getToDate(), criteria.getEmployeeId(), pageable);
        return employeeMonthlyPaySlipPage;
    }

    //TODO need to use this calculation where needed
    private Double getMonthlyTaxDepositAmount(Employee employee, LocalDate date)throws GenericException{
        try {


            LocalDate currentDate = LocalDate.now();
            LocalDate currFinancialYearStart = null;
            LocalDate currFinancialYearEnd = null;

            Integer remainingNumberOfMonth = 0;

            if (currentDate.getMonth().getValue() <= 6) {
                currFinancialYearStart = LocalDate.of(currentDate.getYear() - 1, 7, 1);
                currFinancialYearEnd = LocalDate.of(currentDate.getYear(), 6, 30);

                remainingNumberOfMonth = 6 - currentDate.getMonth().getValue() + 1;
            } else {
                currFinancialYearStart = LocalDate.of(currentDate.getYear(), 7, 1);
                currFinancialYearEnd = LocalDate.of(currentDate.getYear() + 1, 6, 30);

                remainingNumberOfMonth = 12 - currentDate.getMonth().getValue() + 1;
            }
            if (date.isAfter(currFinancialYearEnd)) {
                throw new GenericException("The date should be within the current financial year");
            }

            //get total taxable Income and total tax the for the current financial year
            Double totalTaxAbleIncome = this.getTotalEstimatedTaxableIncomeBasedOnSalary(employee, currFinancialYearStart, currFinancialYearEnd);

            //get total tax deposition for the current financial year
            Double totalTaxDeposited = generateTotalTaxDepositForTheCurrentFinancialYear(employee, currFinancialYearStart, currFinancialYearEnd);

            //get the total tax need to pay for this financial year
            Double totalTaxNeedToPay = calculateTotalTaxFromTaxableIncome(totalTaxAbleIncome);

            totalTaxNeedToPay -= totalTaxDeposited;

            return totalTaxNeedToPay/remainingNumberOfMonth;

        }catch (ArithmeticException e){
            throw new GenericException("Arithmetic exception occurred while generating monthly tax calculation, message: "+e.getMessage());
        }catch (NullPointerException e){
            throw new GenericException("Null pointer exception occurred while generating monthly tax calculation, message: "+e.getMessage());
        }
        catch (Exception e){
            throw new GenericException("Internal server error! Contact with admin!, message: "+e.getMessage());
        }
    }
    //TODO need to implement
    private Double getTotalEstimatedTaxableIncomeBasedOnSalary(Employee employee, LocalDate fromDate, LocalDate toDate){
        EmployeeSalary employeeSalary = employee.getEmployeeSalaryList()
                .stream()
                .filter(salary -> salary.getStatus().equals(true))
                .collect(Collectors.toList()).get(0);

        //house rent per year
        final Double HOUSE_RENT_FIXED_EXEMPTION = 12*25000.0;
        final Double HOUSE_RENT_50_PERCENT_OF_BASIC = 12 * employeeSalary.getBasicSalary()*0.5;
        final Double HOUSE_RENT_EXEMPTION = Math.min(HOUSE_RENT_FIXED_EXEMPTION, HOUSE_RENT_50_PERCENT_OF_BASIC);

        //conveyance per year
        final Double CONVEYANCE_FIXED_EXEMPTION = 300000.0;

        //medical per year
        final Double MEDICAL_FIXED_EXEMPTION = 120000.0;
        final Double MEDICAL_10_PERCENT_OF_BASIC = 12 * employeeSalary.getBasicSalary()*0.10;//10 percent monthly
        final Double MEDICAL_EXEMPTION = Math.min(MEDICAL_FIXED_EXEMPTION, MEDICAL_10_PERCENT_OF_BASIC);



        try{
            List<MonthlyPaySlip> monthlyPaySlipList = monthlyPaySlipRepository.getEmployeeMonthlyPaySlipByDateRangeAndEmployeeId(
                    fromDate, toDate, employee.getId(),
                    PageRequest.of(0,12)
            ).getContent();

            Double totalBasic = monthlyPaySlipList
                    .stream()
                    .mapToDouble(o -> o.getBasicSalary()).sum();

            Double totalHouseRent = monthlyPaySlipList
                    .stream()
                    .mapToDouble(o -> o.getHouseRent()).sum();

            Double totalConveyance = monthlyPaySlipList
                    .stream()
                    .mapToDouble(o -> o.getConveyanceAllowance()).sum();

            Double totalMedical = monthlyPaySlipList
                    .stream()
                    .mapToDouble(o -> o.getMedicalAllowance()).sum();

            Double totalArrears = monthlyPaySlipList
                    .stream()
                    .mapToDouble(o -> o.getArrears()).sum();

            Double totalFestivalBonus = monthlyPaySlipList
                    .stream()
                    .mapToDouble(o -> o.getFestivalBonus()).sum();

            Double totalPFCompany = monthlyPaySlipList
                    .stream()
                    .mapToDouble(o -> o.getProvidentFund().getCompanyContribution()).sum();

            Double totalDue = monthlyPaySlipList
                    .stream()
                    .mapToDouble(o -> o.getDue()).sum();

            Double totalIncentiveBonus = monthlyPaySlipList
                    .stream()
                    .mapToDouble(o -> o.getIncentiveBonus()).sum();

            Double totalOtherPay = monthlyPaySlipList
                    .stream()
                    .mapToDouble(o -> o.getOtherPay()).sum();


            totalHouseRent = (totalHouseRent-HOUSE_RENT_EXEMPTION) < 0.0 ? 0.0 : (totalHouseRent-HOUSE_RENT_EXEMPTION);
            totalConveyance = (totalConveyance-CONVEYANCE_FIXED_EXEMPTION) < 0.0 ? 0.0 : (totalConveyance-CONVEYANCE_FIXED_EXEMPTION);
            totalMedical = (totalMedical-MEDICAL_EXEMPTION) < 0.0 ? 0.0 : (totalMedical-MEDICAL_EXEMPTION);

            Double totalTaxableIncome = totalBasic +
                    totalHouseRent +
                    totalConveyance +
                    totalMedical +
                    totalArrears +
                    totalFestivalBonus +
                    totalPFCompany +
                    totalDue +
                    totalIncentiveBonus +
                    totalOtherPay;

            return totalTaxableIncome;

        }catch (NullPointerException e){
            System.out.println("Need to general monthly payslip for this employee for the current financial year");
        }catch (Exception e){
            System.out.println("Internal server error! please contact with admin!");
        }finally {
            return 0.0;
        }
    }
    //get total tax deposited
    private Double generateTotalTaxDepositForTheCurrentFinancialYear(Employee employee, LocalDate fromDate, LocalDate toDate) throws GenericException{
        try {
            List<EmployeeTaxDeposit> list = taxDepositRepository.getAllByEmployeeIdAndFromDateAndToDate(
                    employee.getId(), fromDate, toDate, PageRequest.of(0, 100)).getContent();
            Double totalTaxDeposited = list
                                        .stream()
                                        .mapToDouble(o -> o.getAmount())
                                        .sum();

            //TODO need to implement to get the advanced tax from the previous year
            return totalTaxDeposited;
        }catch (NullPointerException e){
            System.out.println("No tax deposition found for this employee for the current financial year");
            return 0.0;
        }
    }
    //get total tax from total taxable income as per the rules of  BD
    private Double calculateTotalTaxFromTaxableIncome(Double taxableIncome){

        //get the minimum tax division wise
        //Barishal, Chattogram, Dhaka, Khulna, Rajshahi, Rangpur, Mymensingh and Sylhet.
        //TODO here need to check employee where is he/she living and his tax circle; here I am considering all them are in Dhaka
        List<Double> divisionWiseMinimumTax = new ArrayList<>();
        divisionWiseMinimumTax.addAll(Arrays.asList(3000.0, 5000.0, 5000.0, 3000.0, 5000.0, 5000.0, 3000.0, 3000.0));


        final Double taxFreeIncomePerYear = 300000.0;
        final Double percent_5_PerYearUpto = 100000.0;
        final Double percent_10_PerYearUpto = 300000.0;
        final Double percent_15_PerYearUpto = 400000.0;
        final Double percent_20_PerYearUpto = 500000.0;

        taxableIncome-=taxFreeIncomePerYear;

        if(taxableIncome<1.0)return divisionWiseMinimumTax.get(2);

        Double tax = 0.0;

        if((taxableIncome-percent_5_PerYearUpto)<0.0){
            tax += taxableIncome * 0.05;
            return Math.max(tax, divisionWiseMinimumTax.get(2));
        }
        tax +=percent_5_PerYearUpto*0.05;
        taxableIncome-=percent_5_PerYearUpto;

        if((taxableIncome-percent_10_PerYearUpto)<0.0){
            tax += taxableIncome * 0.10;
            return Math.max(tax, divisionWiseMinimumTax.get(2));
        }
        tax +=percent_5_PerYearUpto*0.10;
        taxableIncome-=percent_5_PerYearUpto;

        if((taxableIncome-percent_15_PerYearUpto)<0.0){
            tax += taxableIncome * 0.15;
            return Math.max(tax, divisionWiseMinimumTax.get(2));
        }
        tax +=percent_15_PerYearUpto*0.15;
        taxableIncome-=percent_15_PerYearUpto;

        if((taxableIncome-percent_20_PerYearUpto)<0.0){
            tax += taxableIncome * 0.20;
            return Math.max(tax, divisionWiseMinimumTax.get(2));
        }
        tax +=percent_20_PerYearUpto*0.20;
        taxableIncome-=percent_20_PerYearUpto;

        if(taxableIncome>0){
            tax +=taxableIncome*0.25;
            return Math.max(tax, divisionWiseMinimumTax.get(2));
        }
        return tax;
    }
}
