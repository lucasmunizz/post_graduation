
// Função para buscar os relatórios do orientador e preencher a tabela
async function fetchAndDisplayReports() {

    try {
        const response = await fetch(`http://localhost:8080/api/forms/evaluated-forms`, {
            headers: {
                //"Authorization": `Bearer ${localStorage.getItem("token")}`
            }
        });

        console.log(response)

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
                <td>${report.advisorEmail}</td>
                <td>${report.advisorNote}</td>
                <td>${report.ccpOpinion || "N/A"}</td>
            `;

            const actionCell = document.createElement("td");
            const evaluateButton = document.createElement("button");
            evaluateButton.className = "evaluate-btn";
            report.ccpOpinion === null ? evaluateButton.textContent = "Avaliar" : evaluateButton.textContent = "Avaliado" ;
            evaluateButton.disabled = report.ccpOpinion !== null;
            evaluateButton.addEventListener("click", () => {
                window.location.href = `./evaluate-reports/report-evaluation.html?formId=${report.formId}`;
            });

            actionCell.appendChild(evaluateButton);
            row.appendChild(actionCell);

            tableBody.appendChild(row);
        });

        toggleOpinionButton(reports);
        
    } catch (error) {
        console.error(error);
        alert("Erro ao carregar relatórios.");
    }
}

// Função para ativar ou desativar o botão de opinião
function toggleOpinionButton(reports) {
    const opinionButton = document.getElementById("opinion-button");

    // Verifica se todos os relatórios têm o campo ccpOpinion preenchido
    const allReportsHaveCCPOpinion = reports.every(report => report.ccpOpinion !== null);

    // Ativa ou desativa o botão com base na verificação
    opinionButton.disabled = !allReportsHaveCCPOpinion;
}


// Carregar relatórios ao carregar a página
document.addEventListener("DOMContentLoaded", () => {
    // Carregar relatórios ao carregar a página
    fetchAndDisplayReports();

    // Adicionar evento ao botão "opinion-button"
    const opinionButton = document.getElementById("opinion-button");

    opinionButton.addEventListener("click", async () => {
        try {
            const response = await fetch("http://localhost:8080/ccp/general-opinion", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    // "Authorization": `Bearer ${localStorage.getItem("token")}` // Descomente se necessário autenticação
                },
            });

            if (!response.ok) {
                throw new Error("Erro ao enviar parecer geral");
            }

            alert("Parecer geral enviado com sucesso!");
            opinionButton.disabled = true;
        } catch (error) {
            console.error("Erro ao enviar parecer geral:", error);
            alert("Falha ao enviar parecer geral.");
        }
    });
});

