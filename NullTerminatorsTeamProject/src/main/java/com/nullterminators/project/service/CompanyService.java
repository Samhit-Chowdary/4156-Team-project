package com.nullterminators.project.service;

import com.nullterminators.project.model.Company;
import com.nullterminators.project.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CompanyService implements UserDetailsService {

    @Autowired
    private CompanyRepository companyRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Company> company = companyRepository.findByUsername(username);
        if (company.isPresent()) {
            var userObject = company.get();
            return User.builder()
                    .username(userObject.getUsername())
                    .password(userObject.getPassword())
                    .roles(new String[] {"ADMIN"})
                    .build();
        } else {
            throw new UsernameNotFoundException("User with name: " + username + " not found");
        }
    }
}
