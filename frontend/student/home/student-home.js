// Função para decodificar o token JWT e extrair o ID do orientador
function getStudentIdFromToken() {
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
    window.location.href = "../../index.html";
}

function navigateToPeriods() {
    window.location.href = "../periods/submission-management.html";
}


function navigateToReports() {
    window.location.href = "../reports/report-management.html";
}


// Exibir informações do orientador na página
async function fetchStudentData() {
    const studentId = getStudentIdFromToken();
    console.log(studentId)
    if (!studentId) return;

    try {
        const response = await fetch(`http://localhost:8080/students/${studentId}`, {
            headers: {
                "Authorization": `Bearer ${localStorage.getItem("token")}`,
                "Content-Type": "application/json"
            }
        });
        if (!response.ok) throw new Error("Erro ao buscar dados do estudante.");

        const studentData = await response.json();
        
        // Inserir dados na página
        document.getElementById("studentName").innerText = studentData.name
        document.getElementById("studentEmail").innerText = studentData.email;
    } catch (error) {
        console.error("Erro ao carregar dados do estudante:", error);
    }
}

// Chamar a função ao carregar a página
window.onload = fetchStudentData;
