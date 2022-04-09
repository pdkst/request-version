package io.github.pdkst.request.versions;

import org.springframework.data.util.Version;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.mvc.condition.AbstractRequestCondition;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Collections;

/**
 * @author pdkst
 */
public class VersionRequestCondition extends AbstractRequestCondition<VersionRequestCondition> implements Comparable<VersionRequestCondition> {

    private final Version version;
    private final boolean fallback;
    private final VersionRequestConditionProvider provider;

    public VersionRequestCondition(String version, boolean fallback, VersionRequestConditionProvider provider) {
        this(Version.parse(version), fallback, provider);
    }

    public VersionRequestCondition(Version version, boolean fallback, VersionRequestConditionProvider provider) {
        this.version = version;
        this.fallback = fallback;
        this.provider = provider;
    }

    @Override
    protected Collection<?> getContent() {
        return Collections.singleton(this.version);
    }

    @Override
    protected String getToStringInfix() {
        return " || ";
    }

    @Override
    public VersionRequestCondition combine(@NonNull VersionRequestCondition other) {
        // other 是方法上的配置，this是类上的配置
        // 优先使用other，这表示方法的设置覆盖类上的设置
        if (other.version != null && !other.version.is(SinceVersion.DEFAULT_VERSION)) {
            return other;
        }

        // 如果method上的是默认设置，则返回类上面的配置
        return this;
    }

    @Override
    public VersionRequestCondition getMatchingCondition(HttpServletRequest request) {
        // 如果是最新接口，即是接口版本大于请求版本也需要匹配到
        final VersionRequestCondition requestCondition = provider.create(request);
        if (requestCondition == null) {
            // 请求没有输入版本，则默认匹配所有的版本中最大的
            return this;
        }
        VersionRequestCondition matchCondition = matchCondition(requestCondition, this);
        // 客户端版本过低的时候的保底版本
        if (this.fallback && matchCondition == null) {
            return matchCondition(requestCondition, new VersionRequestCondition("0.0.0", true, provider));
        }
        return matchCondition;
    }

    private VersionRequestCondition matchCondition(VersionRequestCondition requestCondition, VersionRequestCondition matchCondition) {
        // 请求里面没有版本，直接执行最新版本
        if (requestCondition == null) {
            return null;
        }
        // 接口版本大于请求版本，不匹配
        if (matchCondition.compareTo(requestCondition) > 0) {
            return null;
        }
        // 取小于接口版本的最大的版本
        return matchCondition;
    }

    @Override
    public int compareTo(VersionRequestCondition other, HttpServletRequest request) {
        // 倒序排列，请求小于请求接口版本的所有版本中的版本最大的那个版本
        return -this.compareTo(other);
    }

    @Override
    public int compareTo(VersionRequestCondition other) {
        return this.version.compareTo(other.version);
    }
}
