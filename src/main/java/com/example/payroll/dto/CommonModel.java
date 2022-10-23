package com.example.payroll.dto;


import lombok.Data;

import javax.persistence.Column;
import java.time.LocalDate;

@Data
public class CommonModel {
    String createdBy;

    LocalDate createTime;

    String editedBy;

    LocalDate editTime;
}
