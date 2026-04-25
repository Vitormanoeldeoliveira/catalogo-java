package br.com.fatec.catalogo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
@Controller
public class LoginController {
    @GetMapping("/gera-hash")
    public String hash() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println(encoder.encode("123456"));

        return "login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}