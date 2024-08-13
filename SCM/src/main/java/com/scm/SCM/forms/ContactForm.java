package com.scm.SCM.forms;

import org.springframework.web.multipart.MultipartFile;

import com.scm.SCM.validators.ValidFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ContactForm {

    @NotBlank(message = "Name is Required")
    private String name;

    @NotBlank(message = "Email is Required")
    private String email;

    
    @NotBlank(message = "Phone Number is Required")
    @Pattern(regexp = "^[0-9]{10}$",message = "Invalid Phone Number")
    private String phoneNumber;

    @NotBlank(message = "Address is Required")
    private String address;

    private String description;

    private boolean favorite;

    private String websiteLink;

    private String linkedInLink;

    @ValidFile(message = "Invalid File")
    private MultipartFile contactImage;

    private String picture;

    

    

}
