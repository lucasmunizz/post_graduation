// Função para decodificar o token JWT e extrair o ID do orientador
function getAdvisorIdFromToken() {
    const token = localStorage.getItem("token");
    if (!token) {
        alert("Usuário não autenticado.");
        return null;
    }

    // Decodificar o token JWT (somente o payload, sem validação)
    const payload = JSON.parse(atob(token.split('.')[1]));
    return payload.sub; // Assume que o ID está no campo 'sub'
}

function logout() {
    localStorage.removeItem("token");
    window.location.href = "index.html";
}

function navigateToPeriods() {
    window.location.href = "../periods/submission-management.html";
}


function navigateToReports() {
    window.location.href = "../reports/report-management.html";
}


// Exibir informações do orientador na página
async function fetchAdvisorData() {
    const advisorId = getAdvisorIdFromToken();
    console.log(advisorId)
    if (!advisorId) return;

    try {
        const response = await fetch(`http://localhost:8080/advisors/${advisorId}`, {
            headers: {
                "Authorization": `Bearer ${localStorage.getItem("token")}`,
                "Content-Type": "application/json"
            }
        });
        if (!response.ok) throw new Error("Erro ao buscar dados do orientador.");

        const advisorData = await response.json();
        
        // Inserir dados na página
        document.getElementById("advisorName").innerText = advisorData.name
        document.getElementById("advisorEmail").innerText = advisorData.email;
    } catch (error) {
        console.error("Erro ao carregar dados do orientador:", error);
    }
}

// Chamar a função ao carregar a página
window.onload = fetchAdvisorData;
