package com.reborn.golf.service;

import com.reborn.golf.dto.EmployeeDto;
import com.reborn.golf.entity.Employee;
import com.reborn.golf.entity.Enum.Role;
import com.reborn.golf.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    //비밀번호 암호화
    private final PasswordEncoder passwordEncoder;

    private final EmployeeRepository employeeRepository;

    @Override
    public boolean register(EmployeeDto employeeDto) {
        Optional<Employee> result = employeeRepository.getAssociatesByEmailAndRemovedFalse(employeeDto.getEmail());

        //추가 1. 삭제된 정보의 경우 언제부터 다시 회원가입 가능한지 조건 필요

        if (result.isEmpty()) {
            employeeDto.setPassword(passwordEncoder.encode(employeeDto.getPassword()));
            Employee newEmployee = dtoToEntity(employeeDto);
            newEmployee.addMemberAuthority(Role.ROLE_MANAGER);
            log.info(newEmployee);
            employeeRepository.save(newEmployee);
            return true;
        }
        return false;
    }

    @Override
    public EmployeeDto read(Integer idx) {

        Optional<Employee> result = employeeRepository.getAssociatesByIdxAndRemovedFalse(idx);

        return result.map(this::entityToDto).orElse(null);

    }

    @Override
    public Integer modify(Integer idx, EmployeeDto employeeDto) {

        Optional<Employee> result = employeeRepository.getAssociatesByIdxAndRemovedFalse(idx);

        if (result.isPresent()) {

            Employee employee = result.get();

            if (employee.getEmail().equals(employeeDto.getEmail())) {
                employee.changeName(employeeDto.getName());
                employee.changeAddress(employeeDto.getAddress());
                employee.changePhone(employeeDto.getPhone());

                log.info(employee);
                employeeRepository.save(employee);
                return employee.getIdx();

            }
        }
        return null;
    }

    @Override
    @Transactional
    public Integer remove(Integer idx) {

        Optional<Employee> result = employeeRepository.getAssociatesByIdxAndRemovedFalse(idx);

        if (result.isPresent()) {

            Employee employee = result.get();
            employee.changeIsRemoved(true);

            log.info(employee);
            employeeRepository.save(employee);
            return employee.getIdx();
        }
        return null;
    }

    @Override
    public String searchEmail(EmployeeDto employeeDto) {
        Optional<Employee> result = employeeRepository.getAssociatesByEmailAndRemovedFalse(employeeDto.getEmail());
        if(result.isPresent()){
            Employee employee = result.get();
            return employee.getEmail();
        }
        return null;
    }

    @Override
    public Integer searchPassword(EmployeeDto employeeDto) {
        Optional<Employee> result = employeeRepository.getAssociatesByEmailAndPhoneAndRemovedFalse(employeeDto.getEmail(), employeeDto.getPhone());
        if(result.isPresent()){
            Employee employee = result.get();
            employee.changePhone(employeeDto.getPhone());
            return employee.getIdx();
        }
        return null;
    }

}
