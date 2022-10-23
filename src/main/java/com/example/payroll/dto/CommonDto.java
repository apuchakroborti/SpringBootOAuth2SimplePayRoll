package com.example.payroll.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonDto {
    private Long createdBy;
    private LocalDate createTime;
    private Long editedBy;
    private LocalDate editTime;
}
