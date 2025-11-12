
function showMessage(message, type = 'info') {
    const messageBox = document.getElementById("messageBox");
    if (!messageBox) return; 

    messageBox.className = 'message-box';

    if (type === 'success') {
        messageBox.classList.add("success");
    } else if (type === 'error') {
        messageBox.classList.add("error");
    } else { // info/warning
        messageBox.classList.add("warning");
    }
    
    messageBox.textContent = message;
    messageBox.classList.remove("hidden");
}

document.addEventListener("DOMContentLoaded", () => {
    const loginForm = document.getElementById("loginForm");

    loginForm.addEventListener("submit", async (event) => {
        event.preventDefault();

        const email = document.getElementById("emailLogin").value.trim();
        const senha = document.getElementById("senhaLogin").value.trim();

        showMessage("", 'hidden'); 

        if (!email || !senha) {
            showMessage("Por favor, preencha todos os campos!", 'warning');
            return;
        }

        try {
            const response = await fetch("http://localhost:8080/usuarios/login", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    email: email,
                    senha: senha 
                })
            });

            if (response.ok) {
                const usuario = await response.json(); 
                
                showMessage(`Login realizado com sucesso! Bem-vindo, ${usuario.nome}!`, 'success');
                
                setTimeout(() => {
                     window.location.href = "index.html"; 
                }, 1000);
                
            } else if (response.status === 401 || response.status === 400 || response.status === 409) {
                const errorMessage = await response.text(); 
                showMessage(`${errorMessage}`, 'error'); 
            } else {
                showMessage("Erro no servidor. Tente novamente mais tarde.", 'error');
            }
        } catch (error) {
            console.error("Erro ao tentar fazer login:", error);
            showMessage("Erro de conexão com o servidor. Verifique se o backend está rodando.", 'error');
        }
    });
});