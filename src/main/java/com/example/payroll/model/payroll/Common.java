package com.example.payroll.model.payroll;


import lombok.Data;

import javax.persistence.Column;
import java.time.LocalDate;

@Data
public class Common {
    @Column(name = "CREATED_BY" )
    private String createdBy;

    @Column(name = "CREATE_TIME" )
    private LocalDate createTime;

    @Column(name = "EDITED_BY" )
    private String editedBy;

    @Column(name = "EDIT_TIME" )
    private LocalDate editTime;
}
