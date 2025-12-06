

const loginFormContainer = document.getElementById('loginFormContainer');
const registerFormContainer = document.getElementById('registerFormContainer');
const goToRegister = document.getElementById('goToRegister');
const goToLogin = document.getElementById('goToLogin');

goToRegister.addEventListener('click', () => {
    loginFormContainer.style.display = 'none';
    registerFormContainer.style.display = 'block';
});

goToLogin.addEventListener('click', () => {
    registerFormContainer.style.display = 'none';
    loginFormContainer.style.display = 'block';
});

// Cadastro
const registerForm = document.getElementById('registerForm');
const registerError = document.getElementById('registerError');

registerForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    const nome = document.getElementById('nome').value.trim();
    const matricula = document.getElementById('matriculaCadastro').value.trim();
    const email = document.getElementById('email').value.trim();
    const senha = document.getElementById('senhaCadastro').value.trim();
    const confirmSenha = document.getElementById('confirmSenha').value.trim();

    if (!nome || !matricula || !email || !senha || !confirmSenha) {
        registerError.style.display = 'block';
        registerError.textContent = "Todos os campos devem ser preenchidos.";
        return;
    }
    if (senha !== confirmSenha) {
        registerError.style.display = 'block';
        registerError.textContent = "As senhas não coincidem.";
        return;
    }

    // Definir role com base no domínio do email
    let tipoUsuario = "ALUNO";
    if (email.endsWith("@admin.unifor.br")) {
        tipoUsuario = "ADMINISTRADOR";
    } else if (email.endsWith("@unifor.br")) {
        tipoUsuario = "BIBLIOTECARIO";
    } else if (email.endsWith("@edu.unifor.br")) {
        tipoUsuario = "ALUNO";
    }

    try {
        await axios.post('http://localhost:8080/api/v1/usuarios/cadastro', {
            nomeCompleto: nome,
            matricula: matricula,
            email: email,
            senha: senha,
            tipoUsuario: tipoUsuario
        });
        alert("Cadastro realizado com sucesso!");
        // Voltar para login
        registerFormContainer.style.display = 'none';
        loginFormContainer.style.display = 'block';
    } catch (err) {
        registerError.style.display = 'block';
        registerError.textContent = "Erro ao cadastrar usuário.";
    }
});

// Login
const loginForm = document.getElementById('loginForm');
const loginError = document.getElementById('loginError');

loginForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    const matricula = document.getElementById('matricula').value.trim();
    const senha = document.getElementById('senha').value.trim();

    if (!matricula || !senha) {
        loginError.style.display = 'block';
        loginError.textContent = "Preencha todos os campos.";
        return;
    }

    try {
        const response = await axios.post('http://localhost:8080/api/v1/usuarios/login', {
            matricula: matricula,
            senha: senha
        });

        alert(response.data.mensagem); // "Login realizado com sucesso"
        console.log("Usuário logado:", response.data.usuario);

        // Redirecionar para dashboard (exemplo)
        // window.location.href = "/dashboard.html";

    } catch (err) {
        loginError.style.display = 'block';
        loginError.textContent = "Matrícula ou senha inválida.";
    }
});

const forgotPasswordContainer = document.getElementById('forgotPasswordContainer');
const resetPasswordContainer = document.getElementById('resetPasswordContainer');
const forgotPasswordLink = document.getElementById('forgotPassword');
const backToLoginFromForgot = document.getElementById('backToLoginFromForgot');

// Alternar para tela de recuperação
forgotPasswordLink.addEventListener('click', () => {
    loginFormContainer.style.display = 'none';
    forgotPasswordContainer.style.display = 'block';
});

// Voltar ao login
backToLoginFromForgot.addEventListener('click', () => {
    forgotPasswordContainer.style.display = 'none';
    loginFormContainer.style.display = 'block';
});

const forgotPasswordForm = document.getElementById('forgotPasswordForm');
const forgotError = document.getElementById('forgotError');

forgotPasswordForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    const nome = document.getElementById('nomeRecuperacao').value.trim();
    const email = document.getElementById('emailRecuperacao').value.trim();
    const matricula = document.getElementById('matriculaRecuperacao').value.trim();

    if (!nome || !email || !matricula) {
        forgotError.style.display = 'block';
        forgotError.textContent = "Todos os campos devem ser preenchidos.";
        return;
    }

    try {
        await axios.post('http://localhost:8080/api/v1/usuarios/validar-recuperacao', {
            nomeCompleto: nome,
            email: email,
            matricula: matricula
        });

        // Se válido, ir para etapa 2
        forgotPasswordContainer.style.display = 'none';
        resetPasswordContainer.style.display = 'block';
    } catch (err) {
        forgotError.style.display = 'block';
        forgotError.textContent = "Dados inválidos ou usuário não encontrado.";
    }
});

const resetPasswordForm = document.getElementById('resetPasswordForm');
const resetError = document.getElementById('resetError');

resetPasswordForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    const novaSenha = document.getElementById('novaSenha').value.trim();
    const confirmNovaSenha = document.getElementById('confirmNovaSenha').value.trim();

    if (!novaSenha || !confirmNovaSenha) {
        resetError.style.display = 'block';
        resetError.textContent = "Preencha todos os campos.";
        return;
    }
    if (novaSenha !== confirmNovaSenha) {
        resetError.style.display = 'block';
        resetError.textContent = "As senhas não coincidem.";
        return;
    }

    try {
        await axios.post('http://localhost:8080/api/v1/usuarios/redefinir-senha', {
            novaSenha: novaSenha
        });

        alert("Senha redefinida com sucesso!");
        resetPasswordContainer.style.display = 'none';
        loginFormContainer.style.display = 'block';
    } catch (err) {
        resetError.style.display = 'block';
        resetError.textContent = "Erro ao redefinir senha.";
    }
});
