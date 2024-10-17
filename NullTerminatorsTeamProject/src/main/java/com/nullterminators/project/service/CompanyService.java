package com.nullterminators.project.service;

import com.nullterminators.project.model.Company;
import com.nullterminators.project.repository.CompanyRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service for company management and basic authentication of API's.
 */
@Service
public class CompanyService implements UserDetailsService {

  @Autowired private CompanyRepository companyRepository;

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


  /**
   * Create a New Company with given details.
   *
   * @param company company to be registered
   */
  public void registerCompany(Company company) {
    company.setId(null);
    company.setPassword(new BCryptPasswordEncoder().encode(company.getPassword()));
    companyRepository.save(company);
  }

  public String getCompanyUsername() {
    return SecurityContextHolder.getContext().getAuthentication().getName();
  }
}