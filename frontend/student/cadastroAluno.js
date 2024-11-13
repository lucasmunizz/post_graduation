function formatDateToDDMMYYYY(date) {
    const [year, month, day] = date.split("-");
    return `${day}/${month}/${year}`;
}


document.addEventListener("DOMContentLoaded", async () => {
    const approvedsSelect = document.getElementById("approvedsSubjectIds");
    const repprovedsSelect = document.getElementById("repprovedSubjectIds");
    const advisorSelect = document.getElementById("advisorEmail")

    // Fetch para obter disciplinas e adicionar como opções
    try {
        const response = await fetch("http://localhost:8080/api/subjects/");
        const subjects = await response.json();
        subjects.forEach(subject => {
            const option = new Option(subject.name, subject.id);
            approvedsSelect.add(option.cloneNode(true));
            repprovedsSelect.add(option);
        });
    } catch (error) {
        console.error("Erro ao buscar disciplinas:", error);
    }

    try {
        const responseAdvisors = await fetch("http://localhost:8080/advisors/");
        const advisors = await responseAdvisors.json();
        console.log(advisors);
        advisors.forEach(advisor => {
            const option = new Option(advisor.email, advisor.email);
            advisorSelect.add(option);
        });
    } catch (error) {
        console.error("Erro ao buscar orientadores:", error);
    }

    // Função para envio do formulário
    document.getElementById("studentForm").addEventListener("submit", async (event) => {
        event.preventDefault();

        const password = document.getElementById("password").value.trim();
        const confirmPassword = document.getElementById("confirmPassword").value.trim();

        if (password !== confirmPassword) {
            document.getElementById("statusMessage").textContent = "As senhas não coincidem.";
            return;
        }

        const data = {
            uspNumber: document.getElementById("uspNumber").value,
            firstName: document.getElementById("fullName").value.split(' ')[0],
            lastName: document.getElementById("fullName").value.split(' ').slice(1).join(' '),
            email: document.getElementById("email").value,
            password: document.getElementById('password').value,
            birthDate: formatDateToDDMMYYYY(document.getElementById("birthDate").value),
            RG: document.getElementById("RG").value,
            birthSpot: document.getElementById("birthSpot").value,
            nacionality: document.getElementById("nacionality").value,
            discipline: document.getElementById("discipline").value,
            advisorEmail: document.getElementById("advisorEmail").value,
            lattesLink: document.getElementById("lattesLink").value,
            registrationDate: formatDateToDDMMYYYY(document.getElementById("registrationDate").value),
            qualifyingExamDate: formatDateToDDMMYYYY(document.getElementById("qualifyingExamDate").value) || null,
            proficiencyExamDate: formatDateToDDMMYYYY(document.getElementById("proficiencyExamDate").value) || null,
            //deadlineDissertation: formatDateToDDMMYYYY(document.getElementById("deadlineDissertation").value),
            approvedsSubjectIds: Array.from(approvedsSelect.selectedOptions).map(opt => opt.value),
            repprovedSubjectIds: Array.from(repprovedsSelect.selectedOptions).map(opt => opt.value)
        };

        console.log(data)

        try {
            const response = await fetch("http://localhost:8080/students/register", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(data)
            });

            if (response.ok) {
                document.getElementById("statusMessage").textContent = "Cadastro realizado com sucesso";
                document.getElementById("studentForm").reset();

                const responseData = await response.json();
                console.log("Token de autenticação:", responseData.accessToken);
                localStorage.setItem('token', responseData.accessToken);
                
            } else {
                document.getElementById("statusMessage").textContent = "Erro ao cadastrar. Verifique os dados.";
            }
        } catch (error) {
            console.error("Erro ao enviar formulário:", error);
        }
    });
});
