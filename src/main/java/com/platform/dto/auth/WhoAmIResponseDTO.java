package com.platform.dto.auth;

import com.platform.entity.Business;
import com.platform.entity.Employee;
import com.platform.entity.ProvidedService;
import com.platform.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.*;

@Data
@Builder
@AllArgsConstructor
public class WhoAmIResponseDTO {
    private User user;
    private List<Business> businessList;
    private List<ProvidedService> providedServiceList;
    private List<Employee> employeeList;




}
