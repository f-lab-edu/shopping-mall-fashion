package com.example.flab.soft.shoppingmallfashion.auth.jwt.dto;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

@Getter
public class TokenBuildDto {
    private String subject;
    private Map<String, String> claims = new HashMap<>();

    public String getClaim(String key) {
        return claims.get(key);
    }

    public static SubjectClaimsDtoBuilder builder() {
        return new SubjectClaimsDtoBuilder();
    }

    public static class SubjectClaimsDtoBuilder {
        private String subject;
        private Map<String, String> claims = new HashMap<>();

        public SubjectClaimsDtoBuilder subject(String subject) {
            this.subject = subject;
            return this;
        }

        public SubjectClaimsDtoBuilder claim(String key, String value) {
            this.claims.put(key, value);
            return this;
        }

        public SubjectClaimsDtoBuilder claim(String key, Long value) {
            this.claims.put(key, value.toString());
            return this;
        }

        public TokenBuildDto build() {
            TokenBuildDto dto = new TokenBuildDto();
            dto.subject = this.subject;
            dto.claims = this.claims;
            return dto;
        }
    }
}
