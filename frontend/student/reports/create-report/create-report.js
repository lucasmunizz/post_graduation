function getStudentIdFromToken() {
    const token = localStorage.getItem("token");
    if (!token) {
        alert("Usuário não autenticado.");
        return null;
    }

    const payload = JSON.parse(atob(token.split('.')[1]));
    return payload.sub; // Assume que o ID está no campo 'sub'
}

async function loadStudentData() {
    const studentId = getStudentIdFromToken();
    if (!studentId) return;

    try {
        const response = await fetch(`http://localhost:8080/students/${studentId}`, {
            headers: {
                
            }
        });

        if (!response.ok) throw new Error("Erro ao buscar dados do estudante.");

        const student = await response.json();

        document.getElementById("studentEmail").value = student.email;
        document.getElementById("studentName").value = student.name;
        document.getElementById("advisorEmail").value = student.advisorEmail;
        document.getElementById("uspNumber").value = student.uspNumber;
        document.getElementById("lattesLink").value = student.lattesLink;
        document.getElementById("discipline").value = student.discipline;
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
        const response = await fetch("http://localhost:8080/api/forms/", {
            method: "POST",
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
