package com.example.payroll.services.payroll.impls;


import com.example.payroll.dto.MonthlyPaySlipDto;
import com.example.payroll.dto.request.MonthlyPaySlipRequestDto;
import com.example.payroll.dto.request.PayslipSearchCriteria;
import com.example.payroll.exceptions.GenericException;
import com.example.payroll.models.payroll.*;
import com.example.payroll.repository.payroll.TaxDepositRepository;
import com.example.payroll.repository.payroll.MonthlyPaySlipRepository;
import com.example.payroll.repository.payroll.EmployeeRepository;
import com.example.payroll.repository.payroll.EmployeeSalaryRepository;
import com.example.payroll.services.payroll.MonthlyPaySlipService;
import com.example.payroll.services.payroll.ProvidentFundService;
import com.example.payroll.services.payroll.TaxDepositService;
import com.example.payroll.utils.Defs;
import com.example.payroll.utils.TaxType;
import com.example.payroll.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

@Service
@Transactional
public class MonthlyPaySlipServiceImpl implements MonthlyPaySlipService {
    @Autowired
    MonthlyPaySlipRepository monthlyPaySlipRepository;

    @Autowired
    ProvidentFundService providentFundService;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    EmployeeSalaryRepository employeeSalaryRepository;

    @Autowired
    TaxDepositRepository taxDepositRepository;

    @Autowired
    TaxDepositService taxDepositService;

    @Override
    public MonthlyPaySlipDto geneartePaySlip(MonthlyPaySlipRequestDto monthlyPaySlipRequestDto) throws GenericException {
        Optional<Employee> optionalEmployee = employeeRepository.findById(monthlyPaySlipRequestDto.getEmployee().getId());
        if(!optionalEmployee.isPresent())throw new GenericException(Defs.USER_NOT_FOUND);

        LocalDate currDate = LocalDate.now();
        if(monthlyPaySlipRequestDto.getFromDate().isAfter(currDate.with(lastDayOfMonth()))){
            throw new GenericException("Payslip should be generated for the current or previous month!");
        }

        if(monthlyPaySlipRequestDto.getFromDate().getMonth().getValue()!=monthlyPaySlipRequestDto.getToDate().getMonth().getValue()){
            throw new GenericException(Defs.PAYSLIP_FROM_TO_DATE_MUST_BE_SAME_MONTH);
        }

        List<LocalDate> currentFinancialYear = Utils.getCurrentFinancialYear();
        if(monthlyPaySlipRequestDto.getFromDate().isBefore(currentFinancialYear.get(0)) || monthlyPaySlipRequestDto.getToDate().isAfter(currentFinancialYear.get(1))){
            throw new GenericException(Defs.PAYSLIP_FROM_TO_DATE_MUST_BE_CURRENT_FINANCIAL_YEAR);
        }

        MonthlyPaySlipDto monthlyPaySlipDto = new MonthlyPaySlipDto();

        Page<MonthlyPaySlip> page = monthlyPaySlipRepository.getEmployeeMonthlyPaySlipByDateRangeAndEmployeeId(
                monthlyPaySlipRequestDto.getFromDate(), monthlyPaySlipRequestDto.getToDate(), optionalEmployee.get().getId(), PageRequest.of(0,1));

        if(page.getTotalElements()==0){
            //generate payslip for the current financial year
            EmployeeSalary employeeSalary = employeeSalaryRepository.getEmployeeCurrentSalaryByEmployeeId(optionalEmployee.get().getId());
            Boolean res = this.generatePayslipForCurrentFinancialYear(optionalEmployee.get(), employeeSalary, currentFinancialYear.get(0));
            if(!res){
                throw new GenericException(Defs.INTERNAL_SERVER_ERROR);
            }

            page = monthlyPaySlipRepository.getEmployeeMonthlyPaySlipByDateRangeAndEmployeeId(
                    monthlyPaySlipRequestDto.getFromDate(), monthlyPaySlipRequestDto.getToDate(), optionalEmployee.get().getId(), PageRequest.of(0,1));

            Utils.copyProperty(page.getContent().get(0), monthlyPaySlipDto);

        }else{
            Utils.copyProperty(page.getContent().get(0), monthlyPaySlipDto);
        }

        //TODO check tax deposited for this month or not
        //TODO need to check the tax already deposited for this month or not

        //if no then insert tax info
        //this is to store the monthly tax deposit from company
        Double taxToDepositForTheRequestMonth = this.getMonthlyTaxDepositAmount(optionalEmployee.get(), monthlyPaySlipRequestDto.getFromDate());

        //deposit monthly tax
        taxDepositService.insertPayslipTaxInfo(page.getContent().get(0),
                optionalEmployee.get(),
                taxToDepositForTheRequestMonth,
                TaxType.FROM_COMPANY,
                monthlyPaySlipRequestDto.getFromDate(),
                monthlyPaySlipRequestDto.getToDate());

        //TODO check provident fund deposited for this month or not
        //TODO get provident fund and check

        //TODO if not then insert provident fund data for this month


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

            //index 0 = financial year start
            //index 1 = financial year end
            List<LocalDate> currentFinancialYear = Utils.getCurrentFinancialYear();

            Integer remainingNumberOfMonth = Utils.getRemainingMonthForTheCurrentFinancialYear();

            if (date.isAfter(currentFinancialYear.get(1))) {
                throw new GenericException("The date should be within the current financial year");
            }

            //get total taxable Income and total tax the for the current financial year
            Double totalTaxAbleIncome = this.getTotalEstimatedTaxableIncomeBasedOnSalary(employee, currentFinancialYear.get(0), currentFinancialYear.get(1));

            //get total tax deposition for the current financial year
            Double totalTaxDeposited = generateTotalTaxDepositForTheCurrentFinancialYear(employee, currentFinancialYear.get(0), currentFinancialYear.get(1));

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
                     fromDate, toDate,employee.getId(), PageRequest.of(0, 100)).getContent();
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
        divisionWiseMinimumTax.addAll(Arrays.asList(
                Defs.MINIMUM_TAX_BARISHAL,
                Defs.MINIMUM_TAX_CHATTOGRAM,
                Defs.MINIMUM_TAX_DHAKA,
                Defs.MINIMUM_TAX_KHULNA,
                Defs.MINIMUM_TAX_RAJSHAHI,
                Defs.MINIMUM_TAX_RANGPUR,
                Defs.MINIMUM_TAX_MYMENSINGH,
                Defs.MINIMUM_TAX_SYLHET));

        taxableIncome-=Defs.TAX_FREE_INCOME_PER_YEAR;

        if(taxableIncome<1.0)return divisionWiseMinimumTax.get(2);

        Double tax = 0.0;

        if((taxableIncome-Defs.PERCENT_5_PER_YEAR_UPTO)<0.0){
            tax += taxableIncome * Defs.PERCENT_5;
            return Math.max(tax, divisionWiseMinimumTax.get(2));
        }
        tax +=Defs.PERCENT_5_PER_YEAR_UPTO * Defs.PERCENT_5;
        taxableIncome-=Defs.PERCENT_5_PER_YEAR_UPTO;

        if((taxableIncome-Defs.PERCENT_10_PER_YEAR_UPTO)<0.0){
            tax += taxableIncome * Defs.PERCENT_10;
            return Math.max(tax, divisionWiseMinimumTax.get(2));
        }
        tax +=Defs.PERCENT_10_PER_YEAR_UPTO * Defs.PERCENT_10;
        taxableIncome-=Defs.PERCENT_10_PER_YEAR_UPTO;

        if((taxableIncome-Defs.PERCENT_15_PER_YEAR_UPTO)<0.0){
            tax += taxableIncome * Defs.PERCENT_15;
            return Math.max(tax, divisionWiseMinimumTax.get(2));
        }
        tax +=Defs.PERCENT_15_PER_YEAR_UPTO * Defs.PERCENT_15;
        taxableIncome-=Defs.PERCENT_15_PER_YEAR_UPTO;

        if((taxableIncome-Defs.PERCENT_20_PER_YEAR_UPTO)<0.0){
            tax += taxableIncome * Defs.PERCENT_20;
            return Math.max(tax, divisionWiseMinimumTax.get(2));
        }
        tax +=Defs.PERCENT_20_PER_YEAR_UPTO *Defs.PERCENT_20;
        taxableIncome-=Defs.PERCENT_20_PER_YEAR_UPTO;

        if(taxableIncome>0){
            tax +=taxableIncome*Defs.PERCENT_25;
            return Math.max(tax, divisionWiseMinimumTax.get(2));
        }
        return tax;
    }

    @Override
    public Boolean generatePayslipForCurrentFinancialYear(Employee employee, EmployeeSalary employeeSalary, LocalDate fromDate) throws GenericException{

        LocalDate toDate = null;
        int month = fromDate.getMonthValue();
        int year =fromDate.getYear();

        if(month <=6){
            toDate = LocalDate.of(year, 6, 30 );
        }else{
            toDate = LocalDate.of(year+1, 6, 30 );
        }
        List<MonthlyPaySlip> monthlyPaySlipList = new ArrayList<>();

        MonthlyPaySlip monthlyPaySlip = new MonthlyPaySlip();
        monthlyPaySlip = getBySettingAllValue(monthlyPaySlip, employeeSalary, fromDate, fromDate.with(lastDayOfMonth()));
        monthlyPaySlipList.add(monthlyPaySlip);

        fromDate = fromDate.with(lastDayOfMonth());
        fromDate = fromDate.plusDays(1);

        while(fromDate.isBefore(toDate.plusDays(1))){
            MonthlyPaySlip monthlyPaySlipNew = new MonthlyPaySlip();
            monthlyPaySlipNew = getBySettingAllValue(monthlyPaySlipNew, employeeSalary, fromDate.with(firstDayOfMonth()), fromDate.with(lastDayOfMonth()));
            monthlyPaySlipList.add(monthlyPaySlipNew);

            fromDate = fromDate.with(lastDayOfMonth());
            fromDate = fromDate.plusDays(1);
        }

        monthlyPaySlipRepository.saveAll(monthlyPaySlipList);

        return true;
    }
    private MonthlyPaySlip getBySettingAllValue(MonthlyPaySlip monthlyPaySlip, EmployeeSalary employeeSalary, LocalDate fromDate, LocalDate toDate){
        monthlyPaySlip.setEmployee(employeeSalary.getEmployee());

        monthlyPaySlip.setGrossSalary(employeeSalary.getGrossSalary());
        monthlyPaySlip.setBasicSalary(employeeSalary.getBasicSalary());//60 percent

        monthlyPaySlip.setHouseRent(employeeSalary.getGrossSalary()*0.3);//30 percent
        monthlyPaySlip.setConveyanceAllowance(employeeSalary.getGrossSalary()*0.045);//4.5 percent
        monthlyPaySlip.setMedicalAllowance(employeeSalary.getGrossSalary()*0.055); // 5.5 percent

        monthlyPaySlip.setFestivalBonus(0.0);

        monthlyPaySlip.setDue(0.0);
        monthlyPaySlip.setArrears(0.0);
        monthlyPaySlip.setIncentiveBonus(0.0);
        monthlyPaySlip.setOtherPay(0.0);

        monthlyPaySlip.setProvidentFund(null);

        monthlyPaySlip.setNetPayment(
                employeeSalary.getGrossSalary()+
                        monthlyPaySlip.getDue()+
                        monthlyPaySlip.getFestivalBonus()+
                        monthlyPaySlip.getIncentiveBonus()+
                        monthlyPaySlip.getOtherPay());

        monthlyPaySlip.setFromDate(fromDate);
        monthlyPaySlip.setToDate(toDate);

        monthlyPaySlip.setCreatedBy(1l);
        monthlyPaySlip.setCreateTime(LocalDateTime.now());
        return monthlyPaySlip;
    }
}
