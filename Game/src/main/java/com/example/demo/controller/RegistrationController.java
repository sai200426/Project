package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;

    // Registration page (GET request)
    @GetMapping("/register")
    public String registrationPage() {
        return "register";  // This will return register.html
    }

    // Handle registration logic (POST request)
    @PostMapping("/register")
    public String registerUser(@RequestParam String name, @RequestParam String email, @RequestParam String password, Model model) {
        try {
            // Create new user object
            User user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setPassword(password);
            
            // Call service to register the user
            userService.registerUser(user);
            
            return "redirect:/login";  // Redirect to login page after successful registration
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", "Email already exists");  // Show error if email exists
            return "register";  // Return to registration page
        }
    }
}
