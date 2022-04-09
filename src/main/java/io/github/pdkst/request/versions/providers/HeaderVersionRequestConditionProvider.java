package io.github.pdkst.request.versions.providers;

import io.github.pdkst.request.versions.VersionRequestCondition;
import io.github.pdkst.request.versions.VersionRequestConditionProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * 请求头获取接口版本
 *
 * @author pdkst
 * @since 2022/4/9
 */
@Slf4j
public class HeaderVersionRequestConditionProvider implements VersionRequestConditionProvider {
    private final String[] headerNames;

    public HeaderVersionRequestConditionProvider(String... headerNames) {
        this.headerNames = headerNames;
    }

    @Override
    public VersionRequestCondition create(HttpServletRequest request) {
        if (headerNames == null || headerNames.length == 0) {
            return null;
        }
        for (String headerName : headerNames) {
            final String requestHeader = request.getHeader(headerName);
            if (StringUtils.hasLength(requestHeader)) {
                return new VersionRequestCondition(requestHeader, false, this);
            }
        }
        log.info("request header has no versions");
        return null;
    }
}
