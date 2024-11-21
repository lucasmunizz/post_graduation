function getStudentIdFromToken() {
    const token = localStorage.getItem("token");
    if (!token) {
        alert("Usuário não autenticado.");
        return null;
    }

    const payload = JSON.parse(atob(token.split('.')[1]));
    return payload.sub; // Assume que o ID está no campo 'sub'
}

// Obter o formId da URL
const urlParams = new URLSearchParams(window.location.search);
const formId = urlParams.get("formId");

async function loadStudentData() {
    const studentId = getStudentIdFromToken();
    if (!studentId) return;

    try {
        const response = await fetch(`http://localhost:8080/api/forms/${formId}`, {
            headers: {
                
            }
        });

        if (!response.ok) throw new Error("Erro ao buscar dados do estudante.");

        const student = await response.json();

        document.getElementById("studentEmail").value = student.studentEmail;
        document.getElementById("studentName").value = student.studentName;
        document.getElementById("advisorEmail").value = student.advisorEmail;
        document.getElementById("uspNumber").value = student.uspNumber;
        document.getElementById("lattesLink").value = student.lattesLink;
        document.getElementById("lattesUpdateDate").value = student.lattesUpdateDate;
        document.getElementById("discipline").value = student.discipline;
        document.getElementById("entryDate").value = student.entryDate;
        document.getElementById("lastReportResult").value = student.lastReportResult;
        document.getElementById("approvalsFromTheBegginigOfTheCourse").value = student.approvalsFromTheBegginigOfTheCourse;
        document.getElementById("repprovalsOnSecondSemester").value = student.repprovalsOnSecondSemester;
        document.getElementById("repprovalsFromTheBegginigOfTheCourse").value = student.repprovalsFromTheBegginigOfTheCourse;
        document.getElementById("proficiencyExam").value = student.proficiencyExam;
        document.getElementById("qualifyingExam").value = student.qualifyingExam;
        document.getElementById("maximumRegistrationDeadline").value = student.maximumRegistrationDeadline;
        document.getElementById("deadlineDissertation").value = student.deadlineDissertation;
        document.getElementById("articlesWritingPhase").value = student.articlesWritingPhase;
        document.getElementById("articlesInEvaluation").value = student.articlesInEvaluation;
        document.getElementById("acceptedArticles").value = student.acceptedArticles;
        document.getElementById("activities").value = student.activities;
        document.getElementById("researchActivitiesResume").value = student.researchActivitiesResume;
        document.getElementById("additionalComments").value = student.additionalComments;
        document.getElementById("hasDifficulty").value = student.hasDifficulty;
        
        if (student.deadlineDissertation) {
            const date = new Date(student.deadlineDissertation);
            document.getElementById("deadlineDissertation").value = date.toISOString().split("T")[0];
        }
    } catch (error) {
        console.error("Erro ao carregar dados do estudante:", error);
    }
}

function formatDateToString(date) {
    console.log(date)
    const [year, month, day] = date.split("-");
    return `${day}/${month}/${year}`;
}

document.getElementById("reportForm").onsubmit = async function (event) {
    event.preventDefault();

    const formData = new FormData(event.target);
    const data = Object.fromEntries(formData.entries());

    // Formatar datas
    data.lattesUpdateDate = formatDateToString(data.lattesUpdateDate);
    data.deadlineDissertation = formatDateToString(data.deadlineDissertation);
    data.entryDate = formatDateToString(data.entryDate);
    data.maximumRegistrationDeadline = formatDateToString(data.maximumRegistrationDeadline);

    console.log(data)

    try {
        const response = await fetch(`http://localhost:8080/api/forms/${formId}`, {
            method: "PUT",
            headers: {
                //"Authorization": `Bearer ${localStorage.getItem("token")}`,
                "Content-Type": "application/json"
            },
            body: JSON.stringify(data)
        });

         if (!response.ok) throw new Error("Erro ao enviar relatório.");
        alert("Relatório enviado com sucesso!");
        window.location.href = "../report-management.html";
    } catch (error) {
        console.error("Erro ao enviar relatório:", error);
    }
};

window.onload = loadStudentData;
