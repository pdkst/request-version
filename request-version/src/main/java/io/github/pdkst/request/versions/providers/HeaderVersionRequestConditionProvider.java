package io.github.pdkst.request.versions.providers;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

/**
 * 请求头获取接口版本
 *
 * @author pdkst
 * @since 2022/4/9
 */
@Slf4j
public class HeaderVersionRequestConditionProvider extends AbstractNameVersionRequestConditionProvider {

    public HeaderVersionRequestConditionProvider(String... headerNames) {
        super(headerNames);
    }

    @Override
    protected String getVersionValue(HttpServletRequest request, String name) {
        return request.getHeader(name);
    }

}
