package com.rotavital.tuguy.service;

import com.rotavital.tuguy.model.requisicao;
import com.rotavital.tuguy.repository.requisicaorepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class requisicaoservice {

    private final requisicaorepository Repository;

    public requisicaoservice(requisicaorepository Repository) {
        this.Repository = Repository;
    }

    public requisicao salvar(requisicao requisicao) {
        return Repository.save(requisicao);
    }

    public List<requisicao> listarTodos() {
        return Repository.findAll();
    }
}