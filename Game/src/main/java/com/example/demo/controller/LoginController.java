package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;
    
    @GetMapping("/")
    public String loginPage1() {
        return "login";  // This will return login.html
    }

    // Show login page
    @GetMapping("/login")
    public String loginPage() {
        return "login";  // This will return login.html
    }
    @PostMapping("/login")
    public String loginUser(@RequestParam String email, @RequestParam String password, Model model, HttpSession session) {
        try {
            User user = userService.validateUserLogin(email, password);
            
            // Store user in session after successful login
            session.setAttribute("loggedInUser", user);
            
            return "redirect:/home";  // Redirect to home page
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", "Invalid email or password");
            return "login";  // Stay on the login page with error message
        }
    }

    // Logout endpoint
    @RequestMapping(value = "/logout", method = {RequestMethod.GET, RequestMethod.POST})
    public String logout(HttpSession session) {
        session.invalidate(); // Clear session
        return "redirect:/login"; // Redirect to login page
    }
}
