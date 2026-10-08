package com.rotavital.tuguy.service;

import com.rotavital.tuguy.model.bolsahemocomponente;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class bolsahemocomponenteservice {

    private final Map<Long, bolsahemocomponente> estoque = new ConcurrentHashMap<>();
    private final AtomicLong sequencia = new AtomicLong(1L);

    public bolsahemocomponente criar(bolsahemocomponente bolsa) {
        if (bolsa == null) {
            throw new IllegalArgumentException("Bolsa não pode ser nula");
        }

        if (bolsa.getId() == null) {
            bolsa.setId(sequencia.getAndIncrement());
        }

        if (bolsa.getStatus() == null || bolsa.getStatus().isBlank()) {
            bolsa.setStatus("DISPONIVEL");
        }

        estoque.put(bolsa.getId(), bolsa);
        return bolsa;
    }

    public List<bolsahemocomponente> listarTodos() {
        return new ArrayList<>(estoque.values());
    }

    public Optional<bolsahemocomponente> buscarPorId(Long id) {
        return Optional.ofNullable(estoque.get(id));
    }

    public bolsahemocomponente atualizar(Long id, bolsahemocomponente bolsaAtualizada) {
        if (id == null || bolsaAtualizada == null) {
            throw new IllegalArgumentException("ID e bolsa são obrigatórios");
        }

        if (!estoque.containsKey(id)) {
            throw new IllegalArgumentException("Bolsa não encontrada para o ID informado");
        }

        bolsaAtualizada.setId(id);
        estoque.put(id, bolsaAtualizada);
        return bolsaAtualizada;
    }

    public boolean deletar(Long id) {
        if (id == null) {
            return false;
        }

        return estoque.remove(id) != null;
    }

    public List<bolsahemocomponente> listarDisponiveis() {
        return estoque.values().stream()
                .filter(bolsahemocomponente::estaDisponivel)
                .toList();
    }
}
