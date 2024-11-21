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

// Função para buscar os relatórios do orientador e preencher a tabela
async function fetchAndDisplayReports() {
    const advisorId = getAdvisorIdFromToken();
    if (!advisorId) return;

    try {
        const response = await fetch(`http://localhost:8080/advisors/${advisorId}/forms`, {
            headers: {
                "Authorization": `Bearer ${localStorage.getItem("token")}`
            }
        });

        if (!response.ok) {
            throw new Error("Erro ao buscar relatórios");
        }

        const reports = await response.json();
        const tableBody = document.getElementById("reportsTable").querySelector("tbody");
        tableBody.innerHTML = "";

        reports.forEach(report => {
            const row = document.createElement("tr");

            console.log(report)

            row.innerHTML = `
                <td>${report.studentName}</td>
                <td>${report.uspNumber}</td>
                <td>${report.version}</td>
                <td>${report.advisorNote || "N/A"}</td>
            `;

            const actionCell = document.createElement("td");
            const evaluateButton = document.createElement("button");
            evaluateButton.className = "evaluate-btn";
            report.advisorNote === null ? evaluateButton.textContent = "Avaliar" : evaluateButton.textContent = "Avaliado" ;
            evaluateButton.disabled = report.advisorNote !== null;
            evaluateButton.addEventListener("click", () => {
                window.location.href = `./evaluate-reports/report-evaluation.html?formId=${report.formId}`;
            });

            actionCell.appendChild(evaluateButton);
            row.appendChild(actionCell);

            tableBody.appendChild(row);
        });
    } catch (error) {
        console.error(error);
        alert("Erro ao carregar relatórios.");
    }
}

// Carregar relatórios ao carregar a página
document.addEventListener("DOMContentLoaded", fetchAndDisplayReports);
