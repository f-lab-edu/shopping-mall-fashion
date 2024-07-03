package com.example.flab.soft.shoppingmallfashion.auth.jwt.refreshToken;

import com.example.flab.soft.shoppingmallfashion.auth.AuthService;
import com.example.flab.soft.shoppingmallfashion.auth.AuthUser;
import com.example.flab.soft.shoppingmallfashion.auth.jwt.TokenProvider;
import com.example.flab.soft.shoppingmallfashion.auth.jwt.TokensDto;
import com.example.flab.soft.shoppingmallfashion.common.SuccessResult;
import com.example.flab.soft.shoppingmallfashion.exception.ApiException;
import com.example.flab.soft.shoppingmallfashion.exception.ErrorEnum;
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
    private final AuthService authService;
    private final TokenProvider tokenProvider;

    @PostMapping("/refresh-token")
    public SuccessResult<TokensDto> refreshToken(@RequestBody TokenRefreshRequest request) {
        String oldToken = parseToken(request);
        if (oldToken == null || !tokenProvider.validateToken(oldToken)) {
            throw new ApiException(ErrorEnum.INVALID_TOKEN);
        }
        String subject = tokenProvider.getSubjectFromToken(oldToken);
        AuthUser authUser = (AuthUser) authService.loadUserByUsername(subject);

        refreshTokenService.validate(oldToken);
        refreshTokenService.delete(oldToken);

        TokensDto newTokens = createNewTokens(authUser);
        return SuccessResult.<TokensDto>builder()
                .response(newTokens).build();
    }

    private static String parseToken(TokenRefreshRequest request) {
        return request.getRefreshToken().split(" ")[0];
    }

    private TokensDto createNewTokens(AuthUser authUser) {
        return TokensDto.builder()
                .accessToken(tokenProvider.createAccessToken(authUser))
                .refreshToken(refreshTokenService.getNewToken(authUser))
                .build();
    }
}
