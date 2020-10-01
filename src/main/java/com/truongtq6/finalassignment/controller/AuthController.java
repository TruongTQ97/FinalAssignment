package com.truongtq6.finalassignment.controller;

import com.truongtq6.finalassignment.dto.FileSearchDTO;
import com.truongtq6.finalassignment.dto.UserDTO;
import com.truongtq6.finalassignment.dto.request.FileRequest;
import com.truongtq6.finalassignment.dto.request.UrlRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Controller
@AllArgsConstructor
public class AuthController {

    /**
     *
     * @return login page
     */
    @GetMapping("/home")
    public String showHomePage(Model model){
        model.addAttribute("searchDTO", new FileSearchDTO());
        model.addAttribute("fileRequest", new FileRequest());
        model.addAttribute("urlRequest", new UrlRequest());
        return "home";
    }

    /**
     *
     * @return login page
     */
    @GetMapping("/login")
    public String showLoginPage(){
        return "login";
    }

    @GetMapping("/signout")
    public String logout(HttpServletRequest request, HttpServletResponse response){
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/login";
    }

    /**
     *
     * @return 403 error page
     */
    @GetMapping("/403")
    public String accessDenied() {
        return "/error/403";
    }

    /**
     * Sign up.
     * @param request
     *      WebRequest
     * @param model
     *          Model
     * @return @{code view name}
     */
    @GetMapping("/user/registration")
    public String showRegistrationForm(WebRequest request, Model model) {
        UserDTO userDto = new UserDTO();
        model.addAttribute("user", userDto);
        return "registration";
    }
}
