package io.github.pdkst.request.versions;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;
import org.springframework.data.util.Version;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * @author pdkst
 * @since 2022/3/23 17:45
 */
@UtilityClass
public class UserAgentUtils {

    public UserAgentHolder parseUserAgent(String userAgent) {
        final StringTokenizer tokenizer = new StringTokenizer(userAgent, " /()", true);
        List<UserAgent> userAgents = new ArrayList<>();
        UserAgent ua = null;
        TokenState state = TokenState.NAME;
        while (tokenizer.hasMoreTokens()) {
            final String token = tokenizer.nextToken();
            switch (token) {
                case "/":
                    state = TokenState.VALUE;
                    break;
                case "(":
                    state = TokenState.DESC;
                    break;
                case ")":
                    state = TokenState.NAME;
                    break;
                case " ":
                    if (state == TokenState.VALUE) {
                        state = TokenState.NAME;
                    }
                    break;
                default:
                    switch (state) {
                        case NAME:
                            if (ua == null || StringUtils.isNotBlank(ua.getValue())) {
                                ua = new UserAgent();
                                userAgents.add(ua);
                            }
                            final String name = StringUtils.joinWith(" ", ua.getName(), token);
                            ua.setName(name);
                            break;
                        case VALUE:
                            Assert.notNull(ua, "ua错误");
                            final String value = StringUtils.joinWith(" ", ua.getValue(), token).trim();
                            ua.setValue(value);
                            break;
                        default:
                            Assert.notNull(ua, "ua错误");
                            final String desc = StringUtils.joinWith(" ", ua.getDescription(), token).trim();
                            ua.setDescription(desc);
                    }


            }
        }
        return new UserAgentHolder(userAgents);
    }

    @Data
    public static class UserAgent {
        private String name;
        private String value;
        private String description;

        /**
         * 是否有效
         *
         * @return value不为空返回true
         */
        public boolean valid() {
            return StringUtils.isNotBlank(value);
        }

        public boolean greaterEqual(String version) {
            if (!valid()) {
                return false;
            }
            final Version currentVersion = Version.parse(value);
            return currentVersion.isGreaterThanOrEqualTo(Version.parse(version));
        }
    }

    @Data
    @RequiredArgsConstructor
    public static class UserAgentHolder {
        private final List<UserAgent> userAgents;

        public UserAgent findByName(String name) {
            if (CollectionUtils.isEmpty(userAgents)) {
                return null;
            }
            for (UserAgent userAgent : userAgents) {
                if (StringUtils.equalsIgnoreCase(userAgent.getName(), name)) {
                    return userAgent;
                }
            }
            return null;
        }

        public String findValueByName(String name, String defaultValue) {
            final UserAgent agent = findByName(name);
            if (agent == null) {
                return defaultValue;
            }
            final String value = agent.getValue();
            if (StringUtils.isBlank(value)) {
                return defaultValue;
            }
            return value;
        }

        public boolean greaterEqualOrNull(String name, String version) {
            if (StringUtils.isBlank(version)) {
                return true;
            }
            final UserAgent userAgent = findByName(name);
            // UserAgent为空，或者value为空，返回true
            if (userAgent == null || !userAgent.valid()) {
                return true;
            } else {
                return userAgent.greaterEqual(version);
            }
        }

        public boolean greaterEqual(String name, String version) {
            final UserAgent userAgent = findByName(name);
            if (userAgent == null) {
                return false;
            } else {
                return userAgent.greaterEqual(version);
            }
        }
    }

    public enum TokenState {
        /**
         * 名称
         */
        NAME,
        /**
         * ua值
         */
        VALUE,
        /**
         * 描述
         */
        DESC,
    }
}
