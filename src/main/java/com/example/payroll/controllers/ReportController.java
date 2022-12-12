package com.example.payroll.controllers;

import com.example.payroll.dto.EmployeeDto;
import com.example.payroll.dto.request.EmployeeSearchCriteria;
import com.example.payroll.dto.response.APIResponse;
import com.example.payroll.dto.response.Pagination;
import com.example.payroll.entity.payroll.Employee;
import com.example.payroll.exceptions.GenericException;
import com.example.payroll.services.EmployeeService;
import com.example.payroll.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/report")
@Slf4j
public class ReportController {

    private final EmployeeService employeeService;

    @Autowired
    ReportController(EmployeeService employeeService){
        this.employeeService = employeeService;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping
    public String exportEmployeeReport(String reportFormat,EmployeeSearchCriteria criteria, @PageableDefault(value = 10) Pageable pageable) throws GenericException, FileNotFoundException, JRException {

        reportFormat="pdf";
        String path = "D:\\intellijidea_projects\\reports";

        Page<Employee> employeeList = employeeService.getEmployeeList(criteria, pageable);

        File file = ResourceUtils.getFile("classpath:employee.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(employeeList.getContent());

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "Java Techie");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        if (reportFormat.equalsIgnoreCase("html")) {
            JasperExportManager.exportReportToHtmlFile(jasperPrint, path + "\\employees.html");
        }
        if (reportFormat.equalsIgnoreCase("pdf")) {
            JasperExportManager.exportReportToPdfFile(jasperPrint, path + "\\employees.pdf");
        }

        return "report generated in path : " + path;

        /*log.info("EmployeeController::searchEmployee start...");

        Page<Employee> employeePage = employeeService.getEmployeeList(criteria, pageable);

        List<EmployeeDto> employeeDtoList = Utils.toDtoList(employeePage, EmployeeDto.class);

        APIResponse<List<EmployeeDto>> responseDTO = APIResponse
                .<List<EmployeeDto>>builder()
                .status("SUCCESS")
                .results(employeeDtoList)
                .pagination(new Pagination(employeePage.getTotalElements(), employeePage.getNumberOfElements(), employeePage.getNumber(), employeePage.getSize()))
                .build();

        log.info("EmployeeController::searchEmployee end");
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);*/
    }
}
