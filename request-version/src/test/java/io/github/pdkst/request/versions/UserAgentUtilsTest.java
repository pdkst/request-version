package io.github.pdkst.request.versions;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author pdkst.zhang
 * @since 2022/3/23 18:19
 */
@Slf4j
public class UserAgentUtilsTest {


    @Test
    public void testParseUserAgent() {
        final String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/99.0.4844.82 Safari/537.36";
        final UserAgentUtils.UserAgentHolder holder = UserAgentUtils.parseUserAgent(userAgent);
        log.info("holder={}", holder);
        Assert.assertEquals(holder.findByName("Mozilla").getValue(), "5.0");
        Assert.assertEquals(holder.findByName("AppleWebKit").getValue(), "537.36");
        Assert.assertEquals(holder.findByName("Chrome").getValue(), "99.0.4844.82");
    }

    @Test
    public void testParseUserAgent2() {
        final String userAgent = "Client/1.5.0 iPhone iOS/15.3.1 Network/1.0.3";
        final UserAgentUtils.UserAgentHolder holder = UserAgentUtils.parseUserAgent(userAgent);
        log.info("holder={}", holder);
        Assert.assertEquals(holder.findByName("Client").getValue(), "1.5.0");
        Assert.assertEquals(holder.findByName("iPhone iOS").getValue(), "15.3.1");
        Assert.assertEquals(holder.findByName("Network").getValue(), "1.0.3");
    }
}