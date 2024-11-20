package com.post_graduation.dto.ccp;

import java.util.List;

public record CCPLoginResponseDTO(String accessToken, List<String> roles) {
}
