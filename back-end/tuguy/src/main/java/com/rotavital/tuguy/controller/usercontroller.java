package com.rotavital.tuguy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.rotavital.tuguy.model.user; 
import com.rotavital.tuguy.service.userservice;

import jakarta.validation.Valid;

@Controller 
public class usercontroller {

    private final userservice Service;

    // instância de ClienteService é injetada pelo Spring no controller
    public usercontroller(userservice service) {
        this.Service = service;
    }

    @GetMapping("/login")
    public String listar(Model model) {
       
	// empacota a lista de clientes em um model, para que a view 
        // (arquivo HTML) possa acessar os dados.
        model.addAttribute("users", Service.listarTodos());
       

        // manda renderizar resources/templates/login.html
        // passando para esse html o model criado.
        return "login";
    }


    @PostMapping("/salvar")
    public String salvar(@Valid user user, BindingResult result) {
        if (result.hasErrors()) {
            return "cadastrar";
        }
        Service.salvar(user);
        return "redirect:/login";
    }
    
    // @PathVariable Long id --> Extrai o valor do ID da URL
    @GetMapping("/remover/{id}")
    public String remover(@PathVariable Long id) {
        Service.remover(id);
        return "redirect:/login";
    }
    

@GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("user", Service.buscarPorId(id));
        return "cadastrar";
    }   

 @GetMapping("/cadastrar/")
    public String novo(Model model) {
        model.addAttribute("user", new user());
        return "cadastrar";
    }

}
