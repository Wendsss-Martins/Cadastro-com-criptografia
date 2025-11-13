package com.criandoapi.projeto.controller;

import com.criandoapi.projeto.dao.IUsuario;
import com.criandoapi.projeto.model.Usuario;
import com.criandoapi.projeto.service.CriptografiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private IUsuario usuarioRepository;

    @Autowired
    private CriptografiaService criptografiaService;

    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrar(@RequestBody Usuario usuario) {
        if (usuario.getSenha() == null || usuario.getSenha().isEmpty()) {
            return ResponseEntity.badRequest().body("Senha é obrigatória");
        }

        Usuario usuarioExistente = usuarioRepository.findFirstByEmail(usuario.getEmail());
        if (usuarioExistente != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email já cadastrado.");
        }

        String senhaCripto = criptografiaService.criptografarSenha(usuario.getSenha());
        usuario.setSenhaCriptografada(senhaCripto);
        usuario.setSenha(null);

        System.out.println("------------------------------------------------------------------");
        System.out.println("CADASTRO BEM-SUCEDIDO:");
        System.out.println("NOME DO USUÁRIO: " + usuario.getNome());
        System.out.println("EMAIL: " + usuario.getEmail());
        System.out.println("HASH BCrypt GERADO (Terminal): " + senhaCripto);
        System.out.println("------------------------------------------------------------------");
        // -----------------------------------------------------------


        usuarioRepository.save(usuario);
        return ResponseEntity.ok("Usuário cadastrado com sucesso");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Usuario usuario) {
        if (usuario.getEmail() == null || usuario.getSenha() == null) {
            return ResponseEntity.badRequest().body("Email e senha são obrigatórios");
        }

        Usuario encontrado = usuarioRepository.findFirstByEmail(usuario.getEmail());

        if (encontrado == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas"); // Retorna 401
        }

        System.out.println("------------------------------------------------------------------");
        System.out.println("USUÁRIO: " + encontrado.getNome());
        System.out.println("EMAIL FORNECIDO: " + usuario.getEmail());
        System.out.println("SENHA HASH (DB): " + encontrado.getSenhaCriptografada());
        System.out.println("SENHA PLANO (INPUT): " + usuario.getSenha());
        System.out.println("------------------------------------------------------------------");

        boolean senhaValida = criptografiaService.verificarSenha(
                usuario.getSenha(),
                encontrado.getSenhaCriptografada()
        );

        if (!senhaValida) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas"); // Retorna 401
        }

        encontrado.setSenhaCriptografada(null);
        encontrado.setSenha(null);

        return ResponseEntity.ok(encontrado);
    }
}