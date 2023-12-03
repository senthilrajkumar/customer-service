package com.sogeti.customerservice.service;

import com.sogeti.customerservice.client.IAMFeignClient;
import com.sogeti.customerservice.dto.CustomerDTO;
import com.sogeti.customerservice.exception.TokenValidationException;
import com.sogeti.customerservice.model.Customer;
import com.sogeti.customerservice.repository.CustomerRepository;
import com.sogeti.customerservice.utility.CustomerMapper;
import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    @Qualifier("iamFeignClient")
    private final IAMFeignClient iamFeignClient;
    public List<CustomerDTO> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        return customers.stream()
                .map(customerMapper::customerToCustomerDTO)
                .toList();
    }

    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        Customer customer = customerMapper.customerDTOToCustomer(customerDTO);
        return customerMapper.customerToCustomerDTO(customerRepository.save(customer));
    }

    public CustomerDTO getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id).orElse(null);
        return (customer != null) ? customerMapper.customerToCustomerDTO(customer) : null;
    }

    public CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO) {
        Customer existingCustomer = customerRepository.findById(id).orElse(null);
        if (existingCustomer != null) {
            Customer updatedCustomer = customerRepository.save(existingCustomer);
            return customerMapper.customerToCustomerDTO(updatedCustomer);
        }
        return null;
    }

    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }

    @Override
    public boolean isValidToken(String token) {
        try {
            String authorizationHeader = "Bearer " + token;
            // Call the token-validation endpoint of iam-service using Feign Client
            ResponseEntity<String> response = iamFeignClient.validateToken(authorizationHeader);
            if (response.getStatusCode() != HttpStatus.OK) {
                log.error("User is not authenticated");
                throw new TokenValidationException("Token validation failed");
            }
        } catch(FeignException e) {
            log.error("User is not authenticated");
            throw new TokenValidationException("Token validation failed");
        }
        log.info("User is successfully authenticated");
        return true;
    }
}
