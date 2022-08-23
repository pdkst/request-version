package io.github.pdkst.request.versions.providers;

import io.github.pdkst.request.versions.VersionRequestCondition;
import io.github.pdkst.request.versions.VersionRequestConditionProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * 抽象获取接口版本提供者
 *
 * @author pdkst
 * @since 2022/4/9
 */
@Slf4j
public abstract class AbstractNameVersionRequestConditionProvider implements VersionRequestConditionProvider {
    private final String[] parameterNames;

    public AbstractNameVersionRequestConditionProvider(String... parameterNames) {
        this.parameterNames = parameterNames;
    }

    @Override
    public VersionRequestCondition create(HttpServletRequest request) {
        if (parameterNames == null || parameterNames.length == 0) {
            return null;
        }
        for (String name : parameterNames) {
            final String versionValue = getVersionValue(request, name);
            if (StringUtils.hasLength(versionValue)) {
                return new VersionRequestCondition(versionValue, false, this);
            }
        }
        log.info("cant find user-agent settings;url={}, names={}", request.getRequestURL(), Arrays.toString(parameterNames));
        return null;
    }

    /**
     * 从请求中获取请求版本
     *
     * @param request http请求
     * @param name    预先设置的字段
     * @return 版本信息, 如果没有查找到版本，则为nulll
     */
    protected String getVersionValue(HttpServletRequest request, String name) {
        return null;
    }
}
