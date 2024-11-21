// Função para extrair o ID do advisor do token JWT
function getAdvisorIdFromToken() {
    const token = localStorage.getItem("token");
    if (!token) {
        alert("Usuário não autenticado.");
        return null;
    }
    const payload = JSON.parse(atob(token.split('.')[1]));
    return payload.sub;
}

// Obter o formId da URL
const urlParams = new URLSearchParams(window.location.search);
const formId = urlParams.get("formId");

// Função para buscar dados do formulário e preencher a tabela
async function loadFormData() {
    try {
        const response = await fetch(`http://localhost:8080/api/forms/${formId}`);
        if (!response.ok) {
            throw new Error("Erro ao buscar dados do formulário.");
        }
        const formData = await response.json();

        const tableBody = document.getElementById("evaluationTable").querySelector("tbody");

        const questions = {
            "E-mail": formData.studentEmail,
            "Nome do(a) aluno(a)": formData.studentName,
            "Email do orientador": formData.advisorEmail,
            "Número USP": formData.uspNumber,
            "Link para o Lattes": formData.lattesLink,
            "Data da última atualização do lattes": formData.lattesUpdateDate,
            "Curso": formData.discipline,
            "Mês e o ano de ingresso como aluno(a) regular": formData.entryDate,
            "Resultado da avaliação do último relatório": formData.lastReportResult,
            "Aprovações em disciplinas desde o início do curso": formData.approvalsFromTheBegginigOfTheCourse,
            "Reprovações no 2o semestre de 2023": formData.repprovalsOnSecondSemester,
            "Reprovações em disciplinas desde o início do curso": formData.repprovalsFromTheBegginigOfTheCourse,
            "Já realizou exame de proficiência em idiomas?": formData.proficiencyExam,
            "Já realizou exame de qualificação?": formData.qualifyingExam,
            "Prazo máximo para inscrição no exame de qualificação": formData.maximumRegistrationDeadline,
            "Prazo máximo para depósito da dissertação": formData.deadlineDissertation,
            "Artigos em fase de escrita": formData.articlesWritingPhase,
            "Artigos submetidos e em período de avaliação": formData.articlesInEvaluation,
            "Artigos aceitos ou publicados": formData.acceptedArticles,
            "Atividades ou eventos acadêmicos que participou no 1o semestre de 2024": formData.activities,
            "Resumo das suas atividades de pesquisa até o momento": formData.researchActivitiesResume,
            "Algo adicional a declarar para a CCP - PPgSI": formData.additionalComments,
            "Está enfrentando alguma dificuldade que precisa de apoio": formData.hasDifficulty,
        };

        for (const [question, answer] of Object.entries(questions)) {
            const row = document.createElement("tr");
            row.innerHTML = `<td>${question}</td><td>${answer || "N/A"}</td>`;
            tableBody.appendChild(row);
        }
    } catch (error) {
        console.error(error);
        alert("Erro ao carregar os dados do formulário.");
    }
}

// Função para enviar a avaliação
document.getElementById("evaluationForm").addEventListener("submit", async (e) => {
    e.preventDefault();
    const advisorNote = document.querySelector('input[name="advisorNote"]:checked').value;

    try {
        const response = await fetch(`http://localhost:8080/api/forms/${formId}/advisor-note`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${localStorage.getItem("token")}`
            },
            body: JSON.stringify({ advisorNote })
        });

        if (response.ok) {
            alert("Avaliação salva com sucesso!");
            window.location.href = "../report-management.html"; // Redireciona para a página anterior
        } else {
            throw new Error("Erro ao salvar a avaliação.");
        }
    } catch (error) {
        console.error(error);
        alert("Erro ao salvar a avaliação.");
    }
});

// Carregar dados do formulário ao abrir a página
loadFormData();
