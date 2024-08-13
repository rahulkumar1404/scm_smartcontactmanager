package com.scm.SCM.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.scm.SCM.entity.User;
import com.scm.SCM.forms.UserForm;
import com.scm.SCM.helpers.Message;
import com.scm.SCM.helpers.MessageType;
import com.scm.SCM.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class PageController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String homePage(Model model) {
        return home(model);
    }
    @GetMapping("/home")
    public String home(Model model) {
        System.out.println("Home page handler");
        model.addAttribute("name1", "Tech1");
        model.addAttribute("name2", "Tech2");
        return "home";
    }

    @GetMapping("/about")
    public String about() {
        System.out.println("About page handler");
        return "about";
    }
    
    @GetMapping("/services")
    public String services() {
        System.out.println("Services page handler");
        return "services";
    }
    
    
    // contact page
    @GetMapping("/contact")
    public String contact() {
        return new String("contact");
    }

    // this is showing login page
    @GetMapping("/login")
    public String login() {
        
        System.out.println("Login page handler");
        return "login";
    }
 
    // registration page
    @GetMapping("/register")
    public String register(Model model) {
 
        UserForm userForm = new UserForm();
        model.addAttribute("userForm", userForm);
 
        return "register";
    }

    //processing register
    @PostMapping("/do-register")
    public String processRegister(@Valid @ModelAttribute UserForm userForm, BindingResult bindingResult, HttpSession session){
        System.out.println("Processing registration");
        //fetch form data
        System.out.println(userForm);
        //check for errors
        if(bindingResult.hasErrors()){
            System.out.println("Errors found");
            return "register";
        }
        //save user to database
        User user = new User();
        user.setName(userForm.getName());
        user.setEmail(userForm.getEmail());
        user.setPassword(userForm.getPassword());
        user.setAbout(userForm.getAbout());
        user.setPhoneNumber(userForm.getPhone());
        user.setProfilePic("https://static-00.iconduck.com/assets.00/profile-default-icon-2048x2045-u3j7s5nj.png");
        
        try {
            User user2 = userService.saveUser(user);
            System.out.println("user saved : \n");
            System.out.println(user2);
            Message message = Message.builder().content("Registration Successful").type(MessageType.green).build();
            session.setAttribute("message",message);
            return "redirect:/register";
        } catch (Exception e) {
            System.out.println("Error saving user: " + e.getMessage());
            return "register";
        }
    }
}


