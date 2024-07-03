package com.example.flab.soft.shoppingmallfashion.auth.jwt.refreshToken;

import com.example.flab.soft.shoppingmallfashion.auth.jwt.dto.TokensDto;
import com.example.flab.soft.shoppingmallfashion.common.SuccessResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class RefreshTokenController {
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/refresh-token")
    public SuccessResult<TokensDto> refreshToken(@RequestBody TokenRefreshRequest request) {
        String oldToken = parseToken(request);
        TokensDto newTokens = refreshTokenService.renew(oldToken);
        return SuccessResult.<TokensDto>builder()
                .response(newTokens).build();
    }

    private String parseToken(TokenRefreshRequest request) {
        return request.getRefreshToken().split(" ")[1];
    }
}
