package com.hk3t.controller;

import com.hk3t.model.Blog;
import com.hk3t.model.User;
import com.hk3t.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@SessionAttributes("user")
public class UserController {

    @Autowired
    BlogService blogService;

    @ModelAttribute("user")
    public User setUpUserForm() {
        return new User();
    }

    @GetMapping("/")
    public String loginForm(@CookieValue(value = "setUser", defaultValue = "") String setUser, Model model) {
        Cookie cookie = new Cookie("setUser", setUser);
        model.addAttribute("cookieValue", cookie);
        return "login";
    }

    @GetMapping("/index")
    public String index() {
        return "index";
    }


    @PostMapping("/index")
    public String doLogin(@ModelAttribute("user") User user, @CookieValue(value = "setUser", defaultValue = "") String setUser,
                          HttpServletRequest request, HttpServletResponse response, Model model, Pageable pageable) {
        if (user.getUsername().equals("admin") && user.getPassword().equals("admin")) {
            if (user.getUsername() != null) {
                setUser = user.getUsername();

                Cookie cookie = new Cookie("setUser", setUser);
                cookie.setMaxAge(60 * 60);
                response.addCookie(cookie);

                Cookie[] cookies = request.getCookies();

                for (Cookie ck : cookies) {
                    if (ck.getName().equals("setUser")) {
                        model.addAttribute("cookieValue", ck);
                        break;
                    } else {
                        ck.setValue("");
                        model.addAttribute("cookieValue", ck);
                        break;
                    }
                }
            }
            Page <Blog> blogs = blogService.findAll(pageable);
            model.addAttribute("blog", blogs);
            return "index";
        } else {
            user.setUsername("");
            Cookie cookie = new Cookie("setUser", setUser);
            model.addAttribute("cookieValue", cookie);
        }
        return "login";
    }
}
