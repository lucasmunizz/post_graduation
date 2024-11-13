document.addEventListener("DOMContentLoaded", () => {
    const advisorId = getAdvisorIdFromToken();
    if (!advisorId) return;

    // Fetch para obter períodos de submissão
    fetch(`http://localhost:8080/advisors/${advisorId}/submission-period`, {
        headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
    })
    .then(response => response.json())
    .then(periods => {
        const tableBody = document.getElementById("periodTableBody");
        const createPeriodBtn = document.getElementById('createPeriodBtn');
        
        let hasActivePeriod = false;

        periods.forEach(period => {
            
            const isActive = isDatePast(period.endDate);

            if (isActive && !hasActivePeriod) {
                hasActivePeriod = true;
                createPeriodBtn.disabled = true;
                createPeriodBtn.title = "Já existe um período ativo.";
            }

            console.log(period)
            const row = document.createElement("tr");
            row.innerHTML = `
                <td>${period.startDate}</td>
                <td>${period.endDate}</td>
                <td>${isActive ? "Ativo" : "Inativo"}</td>
            `;
            tableBody.appendChild(row);
        });
    })
    .catch(error => console.error("Erro ao carregar períodos:", error));

    // Modal - abrir e fechar
    const modal = document.getElementById("modal");
    const createPeriodBtn = document.getElementById("createPeriodBtn");
    const closeModal = document.getElementById("closeModal");

    createPeriodBtn.addEventListener("click", () => {
        modal.style.display = "flex";
    });

    closeModal.addEventListener("click", () => {
        modal.style.display = "none";
    });

    window.onclick = event => {
        if (event.target == modal) {
            modal.style.display = "none";
        }
    };

    // Envio do novo período
    document.getElementById("submitPeriodBtn").addEventListener("click", () => {
        const startDate = document.getElementById("startDate").value;
        const endDate = document.getElementById("endDate").value;

        if (!startDate || !endDate) {
            alert("Por favor, preencha todas as datas.");
            return;
        }

        // Formata as datas para "dd/mm/aaaa"
        const formattedStartDate = formatDateToDDMMYYYY(startDate);
        const formattedEndDate = formatDateToDDMMYYYY(endDate);

        fetch(`http://localhost:8080/advisors/${advisorId}/submission-period`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${localStorage.getItem("token")}`,
            },
            body: JSON.stringify({ startDate: formattedStartDate, endDate: formattedEndDate })
        })
        .then(response => {
            if (response.ok) {
                alert("Período criado com sucesso!");
                modal.style.display = "none";
                location.reload(); // Recarrega a página para mostrar o novo período
            } else {
                alert("Erro ao criar o período.");
            }
        })
        .catch(error => console.error("Erro ao enviar o período:", error));
    });
});

// Função para decodificar o token JWT e extrair o ID do orientador
function getAdvisorIdFromToken() {
    const token = localStorage.getItem("token");
    if (!token) {
        alert("Usuário não autenticado.");
        return null;
    }

    const payload = JSON.parse(atob(token.split('.')[1]));
    return payload.sub;
}

// Função para formatar a data em "dd/mm/aaaa"
function formatDateToDDMMYYYY(dateString) {
    const date = new Date(dateString);
    const day = String(date.getDate()).padStart(2, '0');
    const month = String(date.getMonth() + 1).padStart(2, '0'); // Mês é zero-indexado
    const year = date.getFullYear();
    return `${day}/${month}/${year}`;
}

function isDatePast(dateString) {
    // Divide a string de data em partes de dia, mês e ano
    const [day, month, year] = dateString.split("/").map(Number);
    
    // Cria um objeto Date com o valor da data recebida
    const inputDate = new Date(year, month - 1, day); // Subtrai 1 do mês (Janeiro = 0)
    
    // Obtém a data de hoje (sem horas para comparação correta)
    const today = new Date();
    today.setHours(0, 0, 0, 0); // Reseta horas, minutos, segundos e milissegundos para 0
    
    // Retorna true se a data de entrada já passou, ou false se não passou
    return !(inputDate < today);
}