package com.post_graduation.dto.student;

import java.util.List;

public record LoginStudentRequestDTO(String accessToken, List<String> roles) {
}
