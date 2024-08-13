package com.scm.SCM.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scm.SCM.entity.Contact;
import com.scm.SCM.services.ContactService;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private ContactService contactService;

    @GetMapping("/contact/{contactId}")
    public Contact getContact(@PathVariable String contactId){
        return contactService.getById(contactId);
    }
}
