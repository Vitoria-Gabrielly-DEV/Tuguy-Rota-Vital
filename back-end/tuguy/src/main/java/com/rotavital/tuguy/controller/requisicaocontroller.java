package com.rotavital.tuguy.controller;

import com.rotavital.tuguy.model.requisicao;
import com.rotavital.tuguy.service.requisicaoservice;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;                      // Novo import
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable; // Novo import
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class requisicaocontroller {

    private final requisicaoservice Service;

    public requisicaocontroller(requisicaoservice service) {
        this.Service = service;
    }

    @GetMapping("/requisicoes")
    public String nova(requisicao requisicao) {
        return "requisicao";
    }

    @PostMapping("/requisicoes")
    public String salvar(@Valid requisicao requisicao, BindingResult result) {
        if (result.hasErrors()) {
            return "requisicao";
        }

        Service.salvar(requisicao);
        return "redirect:/menu";
    }

    // Novo — carrega a requisição existente e reaproveita o mesmo formulário para editar
    @GetMapping("/requisicoes/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("requisicao", Service.buscarPorId(id));
        return "requisicao";
    }

    // Novo — remove e volta para o menu
    @GetMapping("/requisicoes/remover/{id}")
    public String remover(@PathVariable Long id) {
        Service.remover(id);
        return "redirect:/menu";
    }
}
