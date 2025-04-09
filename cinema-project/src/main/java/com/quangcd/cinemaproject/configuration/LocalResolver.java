package com.quangcd.cinemaproject.configuration;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.List;
import java.util.Locale;

public class LocalResolver extends AcceptHeaderLocaleResolver
        implements WebMvcConfigurer {

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        String languageHeade = request.getHeader("Accept-Language");

        return StringUtils.hasLength(languageHeade) ? Locale.lookup(Locale.LanguageRange.parse(languageHeade),
                List.of(new Locale("en"),new Locale("vi")))
                : Locale.getDefault();
    }
}
