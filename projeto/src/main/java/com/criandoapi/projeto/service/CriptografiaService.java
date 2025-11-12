package com.criandoapi.projeto.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CriptografiaService {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public String criptografarSenha(String senha) {
        return passwordEncoder.encode(senha);
    }

    public boolean verificarSenha(String senhaTexto, String senhaCripto) {
        if (senhaTexto == null || senhaCripto == null) return false;
        return passwordEncoder.matches(senhaTexto, senhaCripto);
    }
}
