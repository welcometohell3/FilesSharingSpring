package org.example.filessharingsystemspring.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.example.filessharingsystemspring.entity.Files;
import org.example.filessharingsystemspring.entity.Users;
import org.example.filessharingsystemspring.repository.UsersRepository;
import org.example.filessharingsystemspring.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class UsersController {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private FileService fileService;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/")
    public String home() {
        return "login";
    }

    @PostMapping("/login")
    public String login(HttpServletRequest request, Model model) {
        String userName = request.getParameter("userName");
        String password = request.getParameter("password");

        Users user = usersRepository.findByNameAndPassword(userName, password);

        if (user != null) {
            Long userId = user.getId();

            HttpSession session = request.getSession();
            session.setAttribute("userName", userName);
            session.setAttribute("userId", userId);
            return "redirect:/main";
        } else {
            model.addAttribute("error", true);
            return "login";
        }
    }

    @GetMapping("/share")
    public String showSharePage(Model model, HttpSession session) {
        String userName = (String) session.getAttribute("userName");
        List<Users> allUsers = usersRepository.findAll();
        Users currentUser = usersRepository.findByName(userName);
        model.addAttribute("allUsers", allUsers);
        model.addAttribute("userName", userName);
        model.addAttribute("currentUser", currentUser);
        return "share";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/login";
    }

    @GetMapping("/main")
    public String mainPage(Model model, HttpSession session) {
        String userName = (String) session.getAttribute("userName");

        if (userName != null) {
            model.addAttribute("userName", userName);
            return "main";
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping("/catalog")
    public String catalogPage(Model model, HttpSession session) {
        String userName = (String) session.getAttribute("userName");
        if (userName != null) {
            Long userId = (Long) session.getAttribute("userId");

            List<Files> userFiles = fileService.getFilesByUserId(userId);

            model.addAttribute("userName", userName);
            model.addAttribute("files", userFiles);

            return "catalog";
        } else {
            return "redirect:/login";
        }
    }


    @GetMapping("/profile")
    public String profilePage(Model model, HttpSession session) {
        String userName = (String) session.getAttribute("userName");

        if (userName != null) {
            model.addAttribute("userName", userName);
            return "profile";
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(HttpServletRequest request, Model model) {
        String userName = request.getParameter("userName");
        String password = request.getParameter("password");

        Users existingUser = usersRepository.findByName(userName);

        if (existingUser != null) {
            model.addAttribute("error", "Юзер с таким именем уже существует");
            return "register";
        }

        Users newUser = new Users();
        newUser.setName(userName);
        newUser.setPassword(password);

        usersRepository.save(newUser);

        return "redirect:/login";
    }
}
