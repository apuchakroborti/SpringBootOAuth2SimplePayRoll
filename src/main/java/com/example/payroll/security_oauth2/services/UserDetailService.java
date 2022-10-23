package com.example.payroll.security_oauth2.services;


import com.example.payroll.dto.request.PasswordChangeRequestDto;
import com.example.payroll.dto.request.PasswordResetRequestDto;
import com.example.payroll.dto.response.PasswordChangeResponseDto;
import com.example.payroll.exceptions.GenericException;
import com.example.payroll.models.payroll.Employee;
import com.example.payroll.repository.payroll.EmployeeRepository;
import com.example.payroll.security_oauth2.models.security.User;
import com.example.payroll.security_oauth2.repository.AuthorityRepository;
import com.example.payroll.security_oauth2.repository.UserRepository;
import com.example.payroll.utils.Defs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
public class UserDetailService implements UserService, UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmployeeRepository customUserRepository;
    @Autowired
    private AuthorityRepository authorityRepository;

   @Autowired
   @Qualifier("userPasswordEncoder")
   private PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) {
        System.out.println("loadUserByUsername called");
        Optional<User> optionalUser = userRepository.findByUsername(username);

        if(optionalUser.isPresent()){
            if(!optionalUser.get().isEnabled()){
                throw new UsernameNotFoundException(Defs.USER_INACTIVE+": "+username);
            }
            return optionalUser.get();
        }
        throw new UsernameNotFoundException(Defs.USER_NOT_FOUND+": "+username);
    }

    @Override
    public PasswordChangeResponseDto changeUserPassword(PasswordChangeRequestDto passwordChangeRequestDto) throws GenericException {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String currentUsername = currentUser.getUsername();

        UserDetails userDetails = loadUserByUsername(currentUsername);
        String currentPassword = userDetails.getPassword();
        User user = (User) userDetails;

        if(passwordEncoder.matches(passwordChangeRequestDto.getCurrentPassword(), currentPassword)) {
            user.setPassword(passwordEncoder.encode(passwordChangeRequestDto.getNewPassword()));
            userRepository.save(user);
        } else {
            throw new GenericException(Defs.PASSWORD_MISMATCHED);
        }

        return new PasswordChangeResponseDto(true, Defs.PASSWORD_CHANGED_SUCCESSFUL);
    }

    @Override
    public PasswordChangeResponseDto resetPassword(PasswordResetRequestDto passwordResetRequestDto) throws GenericException{

        Optional<Employee> optionalEmployee = customUserRepository.findByEmail(passwordResetRequestDto.getUsername());
        if(!optionalEmployee.isPresent() || optionalEmployee.get().getStatus().equals(false)){
            throw new GenericException(Defs.USER_NOT_FOUND);
        }

        UserDetails userDetails = loadUserByUsername(passwordResetRequestDto.getUsername());
        User user = (User) userDetails;

        user.setPassword(passwordEncoder.encode("apu12345"));
        userRepository.save(user);

        return new PasswordChangeResponseDto(true, Defs.PASSWORD_CHANGED_SUCCESSFUL+": the new Password is: apu12345");
    }
}