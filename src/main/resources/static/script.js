// Containers principais
const loginFormContainer = document.getElementById('loginFormContainer');
const registerFormContainer = document.getElementById('registerFormContainer');
const forgotPasswordContainer = document.getElementById('forgotPasswordContainer');
const resetPasswordContainer = document.getElementById('resetPasswordContainer');

// Novos containers SPA
const adminContainer = document.getElementById('adminContainer');
const alunoContainer = document.getElementById('alunoContainer');
const bibliotecarioContainer = document.getElementById('bibliotecarioContainer');

// Links de navegação
const goToRegister = document.getElementById('goToRegister');
const goToLogin = document.getElementById('goToLogin');
const forgotPasswordLink = document.getElementById('forgotPassword');
const backToLoginFromForgot = document.getElementById('backToLoginFromForgot');

// Alternar entre login e cadastro
goToRegister.addEventListener('click', () => {
    loginFormContainer.style.display = 'none';
    registerFormContainer.style.display = 'block';
});

goToLogin.addEventListener('click', () => {
    registerFormContainer.style.display = 'none';
    loginFormContainer.style.display = 'block';
});

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

        const usuario = response.data.usuario;
        const tipo = usuario?.tipoUsuario;

        // Esconde todos os containers
        loginFormContainer.style.display = 'none';
        registerFormContainer.style.display = 'none';
        forgotPasswordContainer.style.display = 'none';
        resetPasswordContainer.style.display = 'none';
        if (adminContainer) adminContainer.style.display = 'none';
        if (alunoContainer) alunoContainer.style.display = 'none';
        if (bibliotecarioContainer) bibliotecarioContainer.style.display = 'none';

        // Mostra o container correto
        if (tipo === "ADMINISTRADOR") {
            adminContainer.style.display = 'block';
        } else if (tipo === "ALUNO") {
            alunoContainer.style.display = 'block';
        } else if (tipo === "BIBLIOTECARIO") {
            bibliotecarioContainer.style.display = 'block';
        }

    } catch (err) {
        loginError.style.display = 'block';
        loginError.textContent = "Matrícula ou senha inválida.";
    }
});



// =======================================
// LÓGICA DA RECUPERACAO DE SENHA(PARTE 1)
// =======================================

// Recuperação de senha - Etapa 1
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



// =======================================
// LÓGICA DA RECUPERACAO DE SENHA(PARTE 2)
// =======================================


// Recuperação de senha - Etapa 2
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

////////////////////////////////////Código da Lógica de Administrador////////////////////////////////////

// Referências principais
const searchBookForm = document.getElementById('searchBookForm');
const searchQuery = document.getElementById('searchQuery');
const searchField = document.getElementById('searchField');
const booksList = document.getElementById('booksList');

// Modais
const consultBookModal = new bootstrap.Modal(document.getElementById('consultBookModal'));
const editBookModal = new bootstrap.Modal(document.getElementById('editBookModal'));
const deleteConfirmModal = new bootstrap.Modal(document.getElementById('deleteConfirmModal'));
const createBookModal = new bootstrap.Modal(document.getElementById('createBookModal'));
const usersModal = new bootstrap.Modal(document.getElementById('usersModal'));
const profileModal = new bootstrap.Modal(document.getElementById('profileModal'));

// Botões principais
const openCreateBookModalBtn = document.getElementById('openCreateBookModalBtn');
const openUsersModalBtn = document.getElementById('openUsersModalBtn');
const logoutBtn = document.getElementById('logoutBtn');

// Campos de consulta de livro (modal)
const viewCodigoInterno = document.getElementById('viewCodigoInterno');
const viewTitulo = document.getElementById('viewTitulo');
const viewAutor = document.getElementById('viewAutor');
const viewGenero = document.getElementById('viewGenero');
const viewIsbn10 = document.getElementById('viewIsbn10');
const viewIsbn13 = document.getElementById('viewIsbn13');
const viewStatus = document.getElementById('viewStatus');
const viewSinopse = document.getElementById('viewSinopse');

// Formulários
const editBookForm = document.getElementById('editBookForm');
const createBookForm = document.getElementById('createBookForm');

// Botões de remoção
const openDeleteConfirmBtn = document.getElementById('openDeleteConfirmBtn');
const confirmDeleteBookBtn = document.getElementById('confirmDeleteBookBtn');

// Lista de usuários e perfil
const usersList = document.getElementById('usersList');
const profileUserName = document.getElementById('profileUserName');
const profileUserMatricula = document.getElementById('profileUserMatricula');
const profileUserTipo = document.getElementById('profileUserTipo');
const profileLogsList = document.getElementById('profileLogsList');

// Estado temporário
let currentBookId = null;

// 🔎 Pesquisa de livros
searchBookForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    const query = searchQuery.value.trim();
    const field = searchField.value;

    try {
        const response = await axios.get(`http://localhost:8080/api/v1/livros`, {
            params: { query, field }
        });

        booksList.innerHTML = "";
        response.data.forEach(livro => {
            const li = document.createElement('li');
            li.className = "list-group-item d-flex justify-content-between align-items-start";

            li.innerHTML = `
        <div class="me-2 flex-grow-1">
          <div class="fw-bold">${livro.titulo}</div>
          <small>Código: ${livro.codigoInterno} • Autor: ${livro.autor} • Gênero: ${livro.genero} • Status: ${livro.status}</small>
        </div>
        <div class="d-flex flex-column gap-1">
          <button class="btn btn-outline-primary btn-sm" data-action="consultar" data-id="${livro.id}">Consultar</button>
          <button class="btn btn-outline-warning btn-sm" data-action="editar" data-id="${livro.id}">Editar</button>
        </div>
      `;
            booksList.appendChild(li);
        });
    } catch (err) {
        alert("Erro ao buscar livros.");
    }
});

// 📖 Consultar livro
booksList.addEventListener('click', async (e) => {
    const btn = e.target.closest('button');
    if (!btn) return;

    const action = btn.dataset.action;
    const id = btn.dataset.id;

    try {
        const response = await axios.get(`http://localhost:8080/api/v1/livros/${id}`);
        const livro = response.data;
        currentBookId = livro.id;

        if (action === "consultar") {
            viewCodigoInterno.textContent = livro.codigoInterno;
            viewTitulo.textContent = livro.titulo;
            viewAutor.textContent = livro.autor;
            viewGenero.textContent = livro.genero;
            viewIsbn10.textContent = livro.isbn10;
            viewIsbn13.textContent = livro.isbn13;
            viewStatus.textContent = livro.status;
            viewSinopse.textContent = livro.sinopse;
            consultBookModal.show();
        }

        if (action === "editar") {
            document.getElementById('editId').value = livro.id;
            document.getElementById('editCodigoInterno').value = livro.codigoInterno;
            document.getElementById('editTitulo').value = livro.titulo;
            document.getElementById('editAutor').value = livro.autor;
            document.getElementById('editGenero').value = livro.genero;
            document.getElementById('editIsbn10').value = livro.isbn10;
            document.getElementById('editIsbn13').value = livro.isbn13;
            document.getElementById('editStatus').value = livro.status;
            document.getElementById('editSinopse').value = livro.sinopse;
            editBookModal.show();
        }
    } catch (err) {
        alert("Erro ao consultar livro.");
    }
});

// ✏️ Editar livro
editBookForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    const id = document.getElementById('editId').value;

    const livroAtualizado = {
        codigoInterno: document.getElementById('editCodigoInterno').value,
        titulo: document.getElementById('editTitulo').value,
        autor: document.getElementById('editAutor').value,
        genero: document.getElementById('editGenero').value,
        isbn10: document.getElementById('editIsbn10').value,
        isbn13: document.getElementById('editIsbn13').value,
        status: document.getElementById('editStatus').value,
        sinopse: document.getElementById('editSinopse').value
    };

    try {
        await axios.put(`http://localhost:8080/api/v1/livros/${id}`, livroAtualizado);
        alert("Livro atualizado com sucesso!");
        editBookModal.hide();
        searchBookForm.dispatchEvent(new Event('submit')); // atualiza lista
    } catch (err) {
        alert("Erro ao atualizar livro.");
    }
});

// 🗑️ Remover livro
openDeleteConfirmBtn.addEventListener('click', () => {
    deleteConfirmModal.show();
});

confirmDeleteBookBtn.addEventListener('click', async () => {
    try {
        await axios.delete(`http://localhost:8080/api/v1/livros/${currentBookId}`);
        alert("Livro removido com sucesso!");
        deleteConfirmModal.hide();
        editBookModal.hide();
        searchBookForm.dispatchEvent(new Event('submit'));
    } catch (err) {
        alert("Erro ao remover livro.");
    }
});

// ➕ Cadastrar livro
openCreateBookModalBtn.addEventListener('click', () => {
    createBookModal.show();
});

createBookForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    const novoLivro = {
        codigoInterno: document.getElementById('createCodigoInterno').value,
        titulo: document.getElementById('createTitulo').value,
        autor: document.getElementById('createAutor').value,
        genero: document.getElementById('createGenero').value,
        isbn10: document.getElementById('createIsbn10').value,
        isbn13: document.getElementById('createIsbn13').value,
        status: document.getElementById('createStatus').value,
        sinopse: document.getElementById('createSinopse').value
    };

    try {
        await axios.post(`http://localhost:8080/api/v1/livros`, novoLivro);
        alert("Livro cadastrado com sucesso!");
        createBookModal.hide();
        searchBookForm.dispatchEvent(new Event('submit'));
    } catch (err) {
        alert("Erro ao cadastrar livro.");
    }
});

// 👥 Consultar cadastro de usuários
openUsersModalBtn.addEventListener('click', async () => {
    try {
        const response = await axios.get(`http://localhost:8080/api/v1/usuarios?tipo=ADMINISTRADOR,BIBLIOTECARIO`);
        usersList.innerHTML = "";
        response.data.forEach(user => {
            const li = document.createElement('li');
            li.className = "list-group-item d-flex justify-content-between align-items-center";
            li.innerHTML = `
        <span>${user.nomeCompleto} • Matrícula: ${user.matricula} • Tipo: ${user.tipoUsuario}</span>
        <button class="btn btn-outline-primary btn-sm" data-id="${user.id}">Ver perfil</button>`;

            usersList.appendChild(li);
        });

        // Exibir modal de usuários
        usersModal.show();
    } catch (err) {
        alert("Erro ao carregar usuários.");
    }
});

// 📋 Ver perfil de usuário (com logs)
usersList.addEventListener('click', async (e) => {
    const btn = e.target.closest('button');
    if (!btn) return;

    const userId = btn.dataset.id;

    try {
        const response = await axios.get(`http://localhost:8080/api/v1/usuarios/${userId}`);
        const usuario = response.data;

        // Preencher dados do perfil
        profileUserName.textContent = usuario.nomeCompleto;
        profileUserMatricula.textContent = usuario.matricula;
        profileUserTipo.textContent = usuario.tipoUsuario;

        // Buscar logs do usuário
        const logsResponse = await axios.get(`http://localhost:8080/api/v1/logs?usuarioId=${userId}`);
        profileLogsList.innerHTML = "";
        logsResponse.data.forEach(log => {
            const li = document.createElement('li');
            li.className = "list-group-item";
            li.textContent = `${log.data} • ${log.acao}`;
            profileLogsList.appendChild(li);
        });

        // Exibir modal de perfil
        profileModal.show();
    } catch (err) {
        alert("Erro ao carregar perfil do usuário.");
    }
});

// 🚪 Logout
logoutBtn.addEventListener('click', () => {
    // Esconde container do admin
    document.getElementById('adminContainer').style.display = 'none';
    // Volta para tela de login
    loginFormContainer.style.display = 'block';
});



// ==========================
// LÓGICA DO ALUNO
// ==========================

// Referências principais
const searchBookAlunoForm = document.getElementById('searchBookAlunoForm');
const searchBookAlunoInput = document.getElementById('searchBookAlunoInput');
const searchBookAlunoField = document.getElementById('searchBookAlunoField');
const booksAlunoList = document.getElementById('booksAlunoList');

const confirmReservaModal = new bootstrap.Modal(document.getElementById('confirmReservaModal'));
const viewAluguelModal = new bootstrap.Modal(document.getElementById('viewAluguelModal'));
const reservasModal = new bootstrap.Modal(document.getElementById('reservasModal'));
const alugueisModal = new bootstrap.Modal(document.getElementById('alugueisModal'));

const confirmReservaBtn = document.getElementById('confirmReservaBtn');
const viewReservasBtn = document.getElementById('viewReservasBtn');
const viewAlugueisBtn = document.getElementById('viewAlugueisBtn');
const logoutAlunoBtn = document.getElementById('logoutAlunoBtn');

// Campos do modal de aluguel
const aluguelTitulo = document.getElementById('aluguelTitulo');
const aluguelStatus = document.getElementById('aluguelStatus');
const aluguelDataInicio = document.getElementById('aluguelDataInicio');
const aluguelDataFim = document.getElementById('aluguelDataFim');

// Estado temporário
let currentBookIdAluno = null;

// 🔎 Pesquisa de livros (Aluno)
searchBookAlunoForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    const query = searchBookAlunoInput.value.trim();
    const field = searchBookAlunoField.value;

    try {
        const response = await axios.get(`http://localhost:8080/api/v1/livros`, {
            params: { query, field, status: "DISPONIVEL" } // apenas livros ativos
        });

        booksAlunoList.innerHTML = "";
        response.data.forEach(livro => {
            const li = document.createElement('li');
            li.className = "list-group-item d-flex justify-content-between align-items-start";

            li.innerHTML = `
        <div class="me-2 flex-grow-1">
          <div class="fw-bold">${livro.titulo}</div>
          <small>Autor: ${livro.autor} • ISBN: ${livro.isbn10 || livro.isbn13} • Código: ${livro.codigoInterno} • Status: ${livro.status}</small>
        </div>
        <div class="d-flex flex-column gap-1">
          <button class="btn btn-outline-success btn-sm" data-action="reservar" data-id="${livro.id}">Reservar</button>
          <button class="btn btn-outline-info btn-sm" data-action="verAluguel" data-id="${livro.id}">Ver Aluguel</button>
          <button class="btn btn-outline-warning btn-sm" data-action="renovar" data-id="${livro.id}">Renovar</button>
        </div>
      `;
            booksAlunoList.appendChild(li);
        });
    } catch (err) {
        alert("Erro ao buscar livros.");
    }
});

// 📚 Ações nos livros
booksAlunoList.addEventListener('click', async (e) => {
    const btn = e.target.closest('button');
    if (!btn) return;

    const action = btn.dataset.action;
    const id = btn.dataset.id;
    currentBookIdAluno = id;

    if (action === "reservar") {
        confirmReservaModal.show();
    }

    if (action === "verAluguel") {
        try {
            const response = await axios.get(`http://localhost:8080/api/v1/alugueis/${id}`);
            const aluguel = response.data;

            aluguelTitulo.textContent = aluguel.livro.titulo;
            aluguelStatus.textContent = aluguel.status;
            aluguelDataInicio.textContent = aluguel.dataInicio;
            aluguelDataFim.textContent = aluguel.dataFim;

            viewAluguelModal.show();
        } catch (err) {
            alert("Erro ao carregar aluguel.");
        }
    }

    if (action === "renovar") {
        try {
            await axios.put(`http://localhost:8080/api/v1/alugueis/${id}/renovar`);
            alert("Aluguel renovado com sucesso!");
            searchBookAlunoForm.dispatchEvent(new Event('submit'));
        } catch (err) {
            alert("Erro ao renovar aluguel.");
        }
    }
});

// ✅ Confirmar reserva
confirmReservaBtn.addEventListener('click', async () => {
    try {
        await axios.post(`http://localhost:8080/api/v1/reservas`, { livroId: currentBookIdAluno });
        alert("Reserva confirmada! O status do livro foi alterado para Reservado e o aluguel criado com prazo de 30 dias.");
        confirmReservaModal.hide();
        searchBookAlunoForm.dispatchEvent(new Event('submit'));
    } catch (err) {
        alert("Erro ao reservar livro.");
    }
});

// 📖 Histórico de reservas
viewReservasBtn.addEventListener('click', async () => {
    try {
        const response = await axios.get(`http://localhost:8080/api/v1/reservas/minhas`);
        const reservas = response.data;

        const reservasList = document.getElementById('reservasList');
        reservasList.innerHTML = "";
        reservas.forEach(reserva => {
            const li = document.createElement('li');
            li.className = "list-group-item";
            li.textContent = `${reserva.livro.titulo} • Status: ${reserva.status} • Data: ${reserva.dataReserva}`;
            reservasList.appendChild(li);
        });

        reservasModal.show();
    } catch (err) {
        alert("Erro ao carregar reservas.");
    }
});

// 📖 Aluguéis ativos
viewAlugueisBtn.addEventListener('click', async () => {
    try {
        const response = await axios.get(`http://localhost:8080/api/v1/alugueis/meus`);
        const alugueis = response.data;

        const alugueisList = document.getElementById('alugueisList');
        alugueisList.innerHTML = "";
        alugueis.forEach(aluguel => {
            const li = document.createElement('li');
            li.className = "list-group-item";
            li.textContent = `${aluguel.livro.titulo} • Expira em: ${aluguel.dataFim}`;
            alugueisList.appendChild(li);
        });

        alugueisModal.show();
    } catch (err) {
        alert("Erro ao carregar aluguéis.");
    }
});

// 🚪 Logout (Aluno)
logoutAlunoBtn.addEventListener('click', () => {
    document.getElementById('alunoContainer').style.display = 'none';
    loginFormContainer.style.display = 'block';
});


// ==========================
// LÓGICA DO BIBLIOTECÁRIO
// ==========================

// Referências principais
const searchBookBibForm = document.getElementById('searchBookBibForm');
const searchBookBibInput = document.getElementById('searchBookBibInput');
const searchBookBibField = document.getElementById('searchBookBibField');
const booksBibList = document.getElementById('booksBibList');

const consultBookBibModal = new bootstrap.Modal(document.getElementById('consultBookBibModal'));
const createBookBibModal = new bootstrap.Modal(document.getElementById('createBookBibModal'));
const aluguelBibModal = new bootstrap.Modal(document.getElementById('aluguelBibModal'));

const openCreateBookBibBtn = document.getElementById('openCreateBookBibBtn');
const logoutBibBtn = document.getElementById('logoutBibBtn');
const createBookBibForm = document.getElementById('createBookBibForm');

// Campos do modal de consulta
const bibCodigoInterno = document.getElementById('bibCodigoInterno');
const bibTitulo = document.getElementById('bibTitulo');
const bibAutor = document.getElementById('bibAutor');
const bibGenero = document.getElementById('bibGenero');
const bibIsbn10 = document.getElementById('bibIsbn10');
const bibIsbn13 = document.getElementById('bibIsbn13');
const bibStatus = document.getElementById('bibStatus');
const bibSinopse = document.getElementById('bibSinopse');

// Campos do modal de aluguel
const bibAluguelTitulo = document.getElementById('bibAluguelTitulo');
const bibAluguelStatus = document.getElementById('bibAluguelStatus');
const bibAluguelDataInicio = document.getElementById('bibAluguelDataInicio');
const bibAluguelDataFim = document.getElementById('bibAluguelDataFim');
const bibAluguelAluno = document.getElementById('bibAluguelAluno');
const bibAluguelMatricula = document.getElementById('bibAluguelMatricula');
const bibAluguelObservacoes = document.getElementById('bibAluguelObservacoes');

// 🔎 Pesquisa de livros
searchBookBibForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    const query = searchBookBibInput.value.trim();
    const field = searchBookBibField.value;

    try {
        const response = await axios.get(`http://localhost:8080/api/v1/livros`, {
            params: { query, field }
        });

        booksBibList.innerHTML = "";
        response.data.forEach(livro => {
            const li = document.createElement('li');
            li.className = "list-group-item d-flex justify-content-between align-items-start";

            li.innerHTML = `
        <div class="me-2 flex-grow-1">
          <div class="fw-bold">${livro.titulo}</div>
          <small>Autor: ${livro.autor} • ISBN: ${livro.isbn10 || livro.isbn13} • Código: ${livro.codigoInterno} • Status: ${livro.status}</small>
        </div>
        <div>
          <button class="btn btn-outline-info btn-sm" data-action="consultar" data-id="${livro.id}">Consultar</button>
        </div>
      `;
            booksBibList.appendChild(li);
        });
    } catch (err) {
        alert("Erro ao buscar livros.");
    }
});

// 📖 Consultar livro
booksBibList.addEventListener('click', async (e) => {
    const btn = e.target.closest('button');
    if (!btn) return;

    if (btn.dataset.action === "consultar") {
        const id = btn.dataset.id;
        try {
            const response = await axios.get(`http://localhost:8080/api/v1/livros/${id}`);
            const livro = response.data;

            bibCodigoInterno.textContent = livro.codigoInterno;
            bibTitulo.textContent = livro.titulo;
            bibAutor.textContent = livro.autor;
            bibGenero.textContent = livro.genero;
            bibIsbn10.textContent = livro.isbn10 || "-";
            bibIsbn13.textContent = livro.isbn13 || "-";
            bibStatus.textContent = livro.status;
            bibSinopse.textContent = livro.sinopse || "-";

            consultBookBibModal.show();
        } catch (err) {
            alert("Erro ao consultar livro.");
        }
    }
});

// ➕ Cadastrar livro
openCreateBookBibBtn.addEventListener('click', () => {
    createBookBibModal.show();
});

createBookBibForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    try {
        await axios.post(`http://localhost:8080/api/v1/livros`, {
            codigoInterno: document.getElementById('createBibCodigoInterno').value,
            titulo: document.getElementById('createBibTitulo').value,
            autor: document.getElementById('createBibAutor').value,
            genero: document.getElementById('createBibGenero').value,
            isbn10: document.getElementById('createBibIsbn10').value,
            isbn13: document.getElementById('createBibIsbn13').value,
            status: document.getElementById('createBibStatus').value,
            sinopse: document.getElementById('createBibSinopse').value
        });
        alert("Livro cadastrado com sucesso!");
        createBookBibModal.hide();
        searchBookBibForm.dispatchEvent(new Event('submit'));
    } catch (err) {
        alert("Erro ao cadastrar livro.");
    }
});

// 🚪 Logout
logoutBibBtn.addEventListener('click', () => {
    document.getElementById('bibliotecarioContainer').style.display = 'none';
    loginFormContainer.style.display = 'block';
});

// ==========================
// PESQUISA DE ALUNO POR MATRÍCULA
// ==========================
const searchAlunoForm = document.getElementById('searchAlunoForm');
const searchAlunoMatricula = document.getElementById('searchAlunoMatricula');
const alunoResultContainer = document.getElementById('alunoResultContainer');
const alunoNome = document.getElementById('alunoNome');
const alunoMatricula = document.getElementById('alunoMatricula');
const alunoReservasList = document.getElementById('alunoReservasList');
const alunoAlugueisList = document.getElementById('alunoAlugueisList');

searchAlunoForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    const matricula = searchAlunoMatricula.value.trim();
    if (!matricula) return;

    try {
        const response = await axios.get(`http://localhost:8080/api/v1/usuarios/matricula/${matricula}`);
        const aluno = response.data;

        alunoNome.textContent = aluno.nomeCompleto;
        alunoMatricula.textContent = aluno.matricula;

        // Reservas ativas
        const reservasResponse = await axios.get(`http://localhost:8080/api/v1/reservas?usuarioId=${aluno.id}`);
        alunoReservasList.innerHTML = "";
        reservasResponse.data.forEach(reserva => {
            const li = document.createElement('li');
            li.className = "list-group-item";
            li.textContent = `${reserva.livro.titulo} • Status: ${reserva.status} • Data: ${reserva.dataReserva}`;
            alunoReservasList.appendChild(li);
        });

        // Aluguéis ativos
        const alugueisResponse = await axios.get(`http://localhost:8080/api/v1/alugueis?usuarioId=${aluno.id}`);
        alunoAlugueisList.innerHTML = "";
        alugueisResponse.data.forEach(aluguel => {
            const li = document.createElement('li');
            li.className = "list-group-item d-flex justify-content-between align-items-center";
            li.innerHTML = `
        <span>${aluguel.livro.titulo} • Início: ${aluguel.dataInicio} • Fim: ${aluguel.dataFim} • Status: ${aluguel.status}</span>
        <button class="btn btn-outline-info btn-sm" data-id="${aluguel.id}">Ver detalhes</button>
      `;
            alunoAlugueisList.appendChild(li);
        });

        alunoResultContainer.style.display = 'block';
    } catch (err) {
        alert("Erro ao buscar aluno.");
    }
});

// 📖 Ver detalhes do aluguel
alunoAlugueisList.addEventListener('click', async (e) => {
    const btn = e.target.closest('button');
    if (!btn) return;

    const aluguelId = btn.dataset.id;
    try {
        const response = await axios.get(`http://localhost:8080/api/v1/alugueis/${aluguelId}`);
        const aluguel = response.data;

        // Preencher dados no modal
        bibAluguelTitulo.textContent = aluguel.livro.titulo;
        bibAluguelStatus.textContent = aluguel.status;
        bibAluguelDataInicio.textContent = aluguel.dataInicio;
        bibAluguelDataFim.textContent = aluguel.dataFim;
        bibAluguelAluno.textContent = aluguel.usuario.nomeCompleto;
        bibAluguelMatricula.textContent = aluguel.usuario.matricula;
        bibAluguelObservacoes.textContent = aluguel.observacoes || "-";

        // Exibir modal
        aluguelBibModal.show();
    } catch (err) {
        alert("Erro ao carregar detalhes do aluguel.");
    }
});