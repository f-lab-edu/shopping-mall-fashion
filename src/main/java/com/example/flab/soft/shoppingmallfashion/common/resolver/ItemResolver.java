package com.example.flab.soft.shoppingmallfashion.common.resolver;

import com.example.flab.soft.shoppingmallfashion.auth.authentication.userDetails.AuthCrew;
import com.example.flab.soft.shoppingmallfashion.common.Accessible;
import com.example.flab.soft.shoppingmallfashion.exception.ApiException;
import com.example.flab.soft.shoppingmallfashion.exception.ErrorEnum;
import com.example.flab.soft.shoppingmallfashion.item.domain.Item;
import com.example.flab.soft.shoppingmallfashion.item.repository.ItemRepository;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.HandlerMapping;

@Component
@RequiredArgsConstructor
@Slf4j
public class ItemResolver implements HandlerMethodArgumentResolver {
    private final ItemRepository itemRepository;
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Accessible.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String paramName = parameter.getParameterName();
        Long id = (Long) resolveName(paramName, parameter, webRequest);

        Item item;
        if (id != null) {
            item = itemRepository.findById(id)
                    .orElseThrow(() -> new ApiException(ErrorEnum.INVALID_REQUEST));
        } else throw new ApiException(ErrorEnum.FORBIDDEN_REQUEST);

        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) webRequest.getUserPrincipal();
        AuthCrew authCrew = (AuthCrew) authentication.getPrincipal();
        if (authCrew != null && !authCrew.isCrewOf(item.getStore())) {
            throw new ApiException(ErrorEnum.FORBIDDEN_REQUEST);
        }
        log.debug("Item accessed by crew id : {}, uri: {}", authCrew.getId(),
                webRequest.getNativeRequest(HttpServletRequest.class).getRequestURI());
        return id;
    }

    @Nullable
    protected Object resolveName(String name, MethodParameter parameter, NativeWebRequest webRequest) throws Exception {
        String paramName = parameter.getParameterName();
        Map<String, String> pathVariables = (Map<String, String>) webRequest.getAttribute(
                HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, NativeWebRequest.SCOPE_REQUEST);

        if (pathVariables != null && pathVariables.containsKey(paramName)) {
            String value = pathVariables.get(paramName);
            if (parameter.getParameterType().equals(Long.class)) {
                return Long.valueOf(value);
            }
            return value;
        }
        return null;
    }
}
