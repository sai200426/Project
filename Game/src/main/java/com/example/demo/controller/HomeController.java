package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.reposetory.UserRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class HomeController {

    @Autowired
    private UserRepository userRepository;

    // ✅ Home Page
    @GetMapping("/home")
    public String homePage(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login"; // Redirect if not logged in
        }
        model.addAttribute("user", loggedInUser);
        return "home"; 
    }

    // ✅ Profile Page
    @GetMapping("/profile")
    public String profilePage(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", loggedInUser);
        return "profile"; 
    }

    // ✅ Update Profile - Update user name
    @PostMapping("/updateProfile")
    public String updateProfile(@RequestParam String name, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        loggedInUser.setName(name);
        userRepository.save(loggedInUser);

        // Update session data
        session.setAttribute("loggedInUser", loggedInUser);

        return "redirect:/profile"; // Redirect to profile page after update
    }

    // ✅ Update High Score - Returns JSON Response
    @PostMapping("/update-score")
    @ResponseBody
    public ResponseEntity<?> updateHighScore(@RequestBody Map<String, Integer> request, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "User not logged in"));
        }

        int newScore = request.get("score");

        // Update high score only if it's greater than the current score
        if (newScore > loggedInUser.getHighScore()) {
            loggedInUser.setHighScore(newScore);
            userRepository.save(loggedInUser);

            // Update session data
            session.setAttribute("loggedInUser", loggedInUser);
            
            return ResponseEntity.ok(Map.of("message", "High score updated!", "newHighScore", loggedInUser.getHighScore()));
        }
        return ResponseEntity.ok().build();
    }

    // ✅ Delete Account (POST request - when user confirms)
    @Transactional
    @PostMapping("/deleteAccount")
    public String deleteAccount(HttpSession session) {
        // Get the logged-in user from the session
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        
        if (loggedInUser == null) {
            return "redirect:/login"; // Redirect to login if no user is logged in
        }

        // Delete the user by their email
        userRepository.deleteByEmail(loggedInUser.getEmail());
        
        // Invalidate the session to log out the user
        session.invalidate();
        
        // Redirect to login page after successful deletion
        return "redirect:/login";
    }

    // ✅ Delete Account (GET request - Show confirmation popup)
    @GetMapping("/deleteAccount")
    public String showDeleteConfirmation(HttpSession session, Model model) {
        // Check if the user is logged in
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login"; // If not logged in, redirect to login
        }

        model.addAttribute("user", loggedInUser); // Pass the logged-in user to the view (if needed)

        // Return the profile page with the confirmation popup
        return "profile"; 
    }
    @GetMapping("/how-to-play")
    public String howToPlayPage() {
        return "how-to-play";
    }
    @GetMapping("/about")
    public String aboutPage() {
        return "about"; 
    }
 // ✅ Game Page
    @GetMapping("/game")
    public String gamePage(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", loggedInUser);
        return "game"; 
    }
}
