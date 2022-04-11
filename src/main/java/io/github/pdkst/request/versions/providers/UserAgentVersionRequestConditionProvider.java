package io.github.pdkst.request.versions.providers;

import io.github.pdkst.request.versions.UserAgentUtils;
import io.github.pdkst.request.versions.VersionRequestCondition;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * 从UA中获取版本
 *
 * @author pdkst
 * @since 2022/4/11
 */
@Slf4j
public class UserAgentVersionRequestConditionProvider extends AbstractNameVersionRequestConditionProvider {
    public static final String USER_AGENT_HEADER_NAME = "User-Agent";
    private final String[] agentNames;

    public UserAgentVersionRequestConditionProvider(String... agentNames) {
        this.agentNames = agentNames;
    }

    @Override
    public VersionRequestCondition create(HttpServletRequest request) {
        final String[] agentNames = this.agentNames;
        if (agentNames == null || agentNames.length == 0) {
            return null;
        }
        final String agent = request.getHeader(USER_AGENT_HEADER_NAME);
        if (agent == null) {
            return null;
        }
        final UserAgentUtils.UserAgentHolder holder = UserAgentUtils.parseUserAgent(agent);
        for (String agentName : agentNames) {
            final UserAgentUtils.UserAgent userAgent = holder.findByName(agentName);
            if (userAgent == null) {
                continue;
            }
            return new VersionRequestCondition(userAgent.getValue(), false, this);
        }
        log.info("cant find user-agent settings;url={}, names={}", request.getRequestURL(), Arrays.toString(agentNames));
        return null;
    }
}
