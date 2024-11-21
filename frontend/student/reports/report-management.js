// Função para decodificar o token JWT e extrair o ID do orientador
function getStudentIdFromToken() {
    const token = localStorage.getItem("token");
    if (!token) {
        alert("Usuário não autenticado.");
        return null;
    }

    const payload = JSON.parse(atob(token.split('.')[1]));
    return payload.sub; // Assume que o ID está no campo 'sub'
}

// Buscar relatórios do backend e renderizar na tabela
async function fetchReports() {
    
    const studentId = getStudentIdFromToken();
    if (!studentId) return;

    try {
        const response = await fetch(`http://localhost:8080/students/${studentId}/forms`, {
            headers: {
                "Content-Type": "application/json"
            }
        });

        if (!response.ok) throw new Error("Erro ao buscar relatórios.");

        const reports = await response.json();
        console.log(reports)
        renderReports(reports);
    } catch (error) {
        console.error("Erro ao carregar relatórios:", error);
    }
}

// Renderizar relatórios na tabela
function renderReports(reports) {
    const tableBody = document.getElementById("reportTableBody");
    tableBody.innerHTML = ""; // Limpa o conteúdo anterior

    reports.forEach(report => {
        const row = document.createElement("tr");

        // Adiciona células
        row.innerHTML = `
            <td>${report.version}</td>
            <td>${report.advisorEmail}</td>
            <td>${report.submissionDate}</td>
            <td>${report.advisorNote || "Aguardando avaliação"}</td>
        `;

        // Botão de ação
        const actionCell = document.createElement("td");
        const actionButton = document.createElement("button");
        actionButton.className = "action-btn";
        actionButton.textContent = "Reenviar";

        // Habilitar/desabilitar botão
        if (report.version !== 1 || report.advisorNote !== "Insatisfatório") {
            actionButton.disabled = true;
        }

        actionButton.onclick = () => {
            window.location.href = `./update-report/update-report.html?formId=${report.formId}`;
        };

        actionCell.appendChild(actionButton);
        row.appendChild(actionCell);

        tableBody.appendChild(row);
    });
}

// Navegar para a página de cadastro de relatório
document.getElementById("newReportButton").onclick = function () {
    window.location.href = "./create-report/create-report.html";
};

// Chamar a função ao carregar a página
window.onload = fetchReports;
