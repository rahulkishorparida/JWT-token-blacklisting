package com.cms.demo.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.cms.demo.dto.CustomerDto;
import com.cms.demo.model.Customer;
import com.cms.demo.repository.CustomerRepository;
import com.cms.demo.service.CustomerService;

import jakarta.transaction.Transactional;
@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
	private CustomerRepository customerRepository;

    @Override
    public boolean saveCustomer(CustomerDto customerDto) {

        if (customerRepository.existsByPhone(customerDto.getPhone())) {
            throw new RuntimeException("Phone number already exists");
        }

        try {
            Customer customer = new Customer();
            customer.setName(customerDto.getName());
            customer.setAge(customerDto.getAge());
            customer.setPhone(customerDto.getPhone());

            String base64Image = customerDto.getImagebase64();

            if (base64Image != null && !base64Image.isEmpty()) {
                String extension = "png";
                String imageData = base64Image;

                if (base64Image.contains(",")) {
                    String[] parts = base64Image.split(",");
                    String metaInfo = parts[0];
                    imageData = parts[1];

                    // Extract file extension like "jpeg", "png", etc.
                    if (metaInfo.contains("image/")) {
                        extension = metaInfo.substring(metaInfo.indexOf("/") + 1, metaInfo.indexOf(";"));
                    }
                }

                try {
                    byte[] imageBytes = Base64.getDecoder().decode(imageData);
                    String imageName = "customer_" + System.currentTimeMillis() + "." + extension;
                    String uploadDir = "uploads/";

                    File uploadFolder = new File(uploadDir);
                    if (!uploadFolder.exists()) {
                        uploadFolder.mkdirs();
                    }

                    String filePath = uploadDir + imageName;

                    try (OutputStream os = new FileOutputStream(filePath)) {
                        os.write(imageBytes);
                    }

                    customer.setImage(filePath);

                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }
    
            Customer saved = customerRepository.save(customer);
            return saved != null;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

	@Override
	public boolean deletebyId(Integer id) {
	if(customerRepository.existsById(id)) {
		customerRepository.deleteById(id);
		return true;
	}
		return false;
	}

	@Override
	public Optional<Customer> findbyId(Integer id) {
		Optional<Customer> c = customerRepository.findById(id);
		return c;		
	}

	@Override
	public List<Customer> getdAllCustomer() {
		List<Customer> c =customerRepository.findAll();
		return c;
	}
	
	@Override
	public Customer updateCustomer(Integer id, CustomerDto customerDto) {
	    Optional<Customer> c = customerRepository.findById(id);

	    if (c.isPresent()) {
	        Customer customer = c.get();

	        if (customer.getPhone() != customerDto.getPhone() &&
	                customerRepository.existsByPhone(customerDto.getPhone())) {
	            throw new RuntimeException("Phone number already exists");
	        }


	        customer.setName(customerDto.getName());
	        customer.setAge(customerDto.getAge());
	        customer.setPhone(customerDto.getPhone());

	        // Handle new base64 image (if provided)
	        String base64Image = customerDto.getImagebase64();

	        if (base64Image != null && !base64Image.trim().isEmpty()) {
	            String extension = "png";
	            String imageData = base64Image;

	            // Extract metadata and image data
	            if (base64Image.contains(",")) {
	                String[] parts = base64Image.split(",");
	                String metaInfo = parts[0];
	                imageData = parts[1];

	                // Determine image extension from MIME type
	                if (metaInfo.contains("image/")) {
	                    extension = metaInfo.substring(metaInfo.indexOf("/") + 1, metaInfo.indexOf(";"));
	                }
	            }

	            try {
	                // Decode and save new image
	                byte[] imageBytes = Base64.getDecoder().decode(imageData);
	                String imageName = "customer_" + System.currentTimeMillis() + "." + extension;
	                String uploadDir = "uploads/";

	                File uploadFolder = new File(uploadDir);
	                if (!uploadFolder.exists() && !uploadFolder.mkdirs()) {
	                    throw new IOException("Could not create upload directory.");
	                }

	                String filePath = uploadDir + imageName;

	                try (OutputStream os = new FileOutputStream(filePath)) {
	                    os.write(imageBytes);
	                }

	                // Delete old image if it exists
	                String oldImagePath = customer.getImage();
	                if (oldImagePath != null) {
	                    File oldImageFile = new File(oldImagePath);
	                    if (oldImageFile.exists()) {
	                        oldImageFile.delete();
	                    }
	                }

	                customer.setImage(filePath);

	            } catch (IOException e) {
	                e.printStackTrace();
	                return null; // Return null if image upload failed
	            }
	        }

	        return customerRepository.save(customer); // ✅ Return updated customer
	    }

	    return null; // Not found
	}


	
	public CustomerDto convertToDto(Customer customer) {
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
	            customerDto.setImagebase64(null); // or some default
	        }
	    } else {
	        customerDto.setImagebase64(null);
	    }

	    return customerDto;
	}


    }
