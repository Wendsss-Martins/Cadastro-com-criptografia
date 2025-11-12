document.getElementById("formCadastro").addEventListener("submit", async (event) => {
    event.preventDefault();

    const usuario = {
        nome: document.getElementById("nome").value.trim(),
        email: document.getElementById("email").value.trim(),
        senha: document.getElementById("senha").value.trim(),
        telefone: document.getElementById("telefone").value.trim()
    };

    if (!usuario.nome || !usuario.email || !usuario.senha || !usuario.telefone) {
        alert("Por favor, preencha todos os campos antes de cadastrar.");
        return;
    }

    try {
        const response = await fetch("http://localhost:8080/usuarios/cadastrar", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(usuario)
        });

        if (response.ok) {
            alert("Usuário cadastrado com sucesso!");
            document.getElementById("formCadastro").reset();
        } else {
            // Tenta capturar a mensagem de erro do backend
            const errorData = await response.json().catch(() => null);
            const msg = errorData?.message || "Erro ao cadastrar usuário. Verifique os dados.";
            alert(msg);
        }
    } catch (error) {
        console.error("Erro ao conectar com o servidor:", error);
        alert("Erro de conexão com o servidor. Verifique se o backend está rodando.");
    }
});

const usuario = {
    nome: "Teste",
    email: "teste@teste.com",
    senha: "123456",
    telefone: "999999999"
};
console.log(JSON.stringify(usuario));

