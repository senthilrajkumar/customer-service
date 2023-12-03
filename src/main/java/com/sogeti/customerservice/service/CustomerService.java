package com.sogeti.customerservice.service;

import com.sogeti.customerservice.dto.CustomerDTO;

import java.util.List;

public interface CustomerService {

    public List<CustomerDTO> getAllCustomers();

    public CustomerDTO createCustomer(CustomerDTO customer);

    public CustomerDTO getCustomerById(Long id);

    public CustomerDTO updateCustomer(Long id, CustomerDTO customer);

    public void deleteCustomer(Long id);

    boolean isValidToken(String token);
}
