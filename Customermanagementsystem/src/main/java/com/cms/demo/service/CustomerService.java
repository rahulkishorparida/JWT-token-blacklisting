package com.cms.demo.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import com.cms.demo.dto.CustomerDto;
import com.cms.demo.model.Customer;

public interface CustomerService {
	
	boolean saveCustomer(CustomerDto customerDto);
	boolean deletebyId(Integer id);
	Optional<Customer> findbyId(Integer id);
	List<Customer> getdAllCustomer();
	Customer updateCustomer(Integer id, CustomerDto customerDto);
	
	
	public default CustomerDto convertToDto(Customer customer) {
	    CustomerDto customerDto = new CustomerDto();
	    customerDto.setId(customer.getId());
	    customerDto.setName(customer.getName());
	    customerDto.setAge(customer.getAge());
	    customerDto.setPhone(customer.getPhone());

	    // Convert image path to Base64 string
	    String imagePath = customer.getImage();
	    if (imagePath != null && !imagePath.isEmpty()) {
	        try {
	            byte[] imageBytes = Files.readAllBytes(Paths.get(imagePath));
	            String base64Image = Base64.getEncoder().encodeToString(imageBytes);

	            // Optional: Add proper image type prefix (based on file extension)
	            String extension = imagePath.substring(imagePath.lastIndexOf(".") + 1).toLowerCase();
	            String contentType = "image/" + extension;
	            base64Image = "data:" + contentType + ";base64," + base64Image;

	            customerDto.setImagebase64(base64Image);
	        } catch (IOException e) {
	            e.printStackTrace();
	            customerDto.setImagebase64(null); 
	        }
	    } else {
	        customerDto.setImagebase64(null);
	    }

	    return customerDto;
	}
}
