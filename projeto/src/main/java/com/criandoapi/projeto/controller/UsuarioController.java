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