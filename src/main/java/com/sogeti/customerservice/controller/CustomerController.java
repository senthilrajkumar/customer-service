package com.sogeti.customerservice.controller;

import com.sogeti.customerservice.dto.CustomerDTO;
import com.sogeti.customerservice.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Customer Management", description = "Customer Management APIs")
@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Operation(summary = "Receive All Customer Details", security = @SecurityRequirement(name = "bearerToken"), description = "Receive All Customer Details after authentication using JWT token")
    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAllCustomers(HttpServletRequest request) {
        String token = extractToken(request);
        if (token != null && customerService.isValidToken(token)) {
            return ResponseEntity.status(HttpStatus.OK).body(customerService.getAllCustomers());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @Operation(summary = "Create Customer In Customer Database", security = @SecurityRequirement(name = "bearerToken"), description = "Create Customer after authentication using JWT token")
    @PostMapping
    public ResponseEntity<CustomerDTO> createCustomer(@RequestBody CustomerDTO customer, HttpServletRequest request) {
        String token = extractToken(request);
        if (token != null && customerService.isValidToken(token)) {
            return ResponseEntity.status(HttpStatus.OK).body(customerService.createCustomer(customer));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @Operation(summary = "Get specific customer details", security = @SecurityRequirement(name = "bearerToken"), description = "Get customer details after authentication using JWT token")
    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable Long id, HttpServletRequest request) {
        String token = extractToken(request);

        if (token != null && customerService.isValidToken(token)) {
            CustomerDTO customer = customerService.getCustomerById(id);
            return (customer != null) ?
                    ResponseEntity.ok(customer) :
                    ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @Operation(summary = "Update specific customer details", security = @SecurityRequirement(name = "bearerToken"), description = "Update customer details after authentication using JWT token")
    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable Long id, @RequestBody CustomerDTO customer, HttpServletRequest request) {
        String token = extractToken(request);
        if (token != null && customerService.isValidToken(token)) {
            return ResponseEntity.status(HttpStatus.OK).body(customerService.updateCustomer(id, customer));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @Operation(summary = "Delete specific customer", security = @SecurityRequirement(name = "bearerToken"), description = "Delete specific customer after authentication using JWT token")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id, HttpServletRequest request) {
        String token = extractToken(request);
        if (token != null && customerService.isValidToken(token)) {
            customerService.deleteCustomer(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    private String extractToken(HttpServletRequest request) {
        // Extract the token from the Authorization header
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring("Bearer ".length());
        }
        return null;
    }
}
