package com.reborn.golf.service;


import com.reborn.golf.dto.user.EmployeeDto;
import com.reborn.golf.entity.Employee;

public interface EmployeeService {
    boolean register(EmployeeDto employeeDto);

    EmployeeDto read(Integer email);

    Integer modify(Integer idx, EmployeeDto employeeDto);

    Integer remove(Integer idx);

    String searchEmail(EmployeeDto phone);

    Integer searchPassword(EmployeeDto employeeDto);

    default Employee dtoToEntity(EmployeeDto employeeDto) {
        return Employee.builder()
                .idx(employeeDto.getIdx())
                .email(employeeDto.getEmail())
                .password(employeeDto.getPassword())
                .phone(employeeDto.getPhone())
                .name(employeeDto.getName())
                .address(employeeDto.getAddress())
                .build();
    }

    default EmployeeDto entityToDto(Employee employee) {
        return EmployeeDto.builder()
                .idx(employee.getIdx())
                .email(employee.getEmail())
                .password(employee.getPassword())
                .phone(employee.getPhone())
                .name(employee.getName())
                .address(employee.getAddress())
                .build();
    }
}
