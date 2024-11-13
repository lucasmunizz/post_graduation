package com.post_graduation.dto.advisor;

import java.util.List;

public record LoginAdvisorResponseDTO (String accessToken, List<String> roles) {
}
