package com.lzk.toolboxes.config.filter;

import com.alibaba.druid.util.PatternMatcher;
import com.alibaba.druid.util.ServletPathMatcher;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.PostConstruct;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.TimeUnit;

/**
 * @author
 * @module
 * @date 2021/6/5 16:43
 */
//@Component
public class TokenFilterRegistrationBean extends FilterRegistrationBean<Filter> {
    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RedisTemplate redisTemplate;
    @Value("${token.head}")
    private String HEAD;

    @PostConstruct
    public void init() {
        setFilter(new TokenFilter());
        addInitParameter("exclusions",
                "/goods/get/*");
        addUrlPatterns("/*");
        setOrder(10);
    }

    class TokenFilter implements Filter {

        private PatternMatcher pathMatcher = new ServletPathMatcher();
        private final String EXCLUSIONS_NAME = "exclusions";
        private List<String> exclusionsParam;

        @Override
        public void init(FilterConfig filterConfig) throws ServletException {
            String parameter = filterConfig.getInitParameter(EXCLUSIONS_NAME);
            log.info("exclusions:{}", parameter);
            if (StringUtils.isNotBlank(parameter)) exclusionsParam = Arrays.asList(parameter.split(","));
        }

        @Override
        public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            String uri = request.getRequestURI();
            log.info("请求URI:{}", "  " + uri);
            if (isExclusions(uri)) {
                filterChain.doFilter(servletRequest, servletResponse);
            } else {
                String header = request.getHeader(HEAD);
                if (StringUtils.isNotBlank(header) && checkToken(header))
                    filterChain.doFilter(servletRequest, servletResponse);
                returnJSON(response, 1000, "登录已过期");
            }
        }

        private boolean isExclusions(String uri) {
            if (exclusionsParam == null) return false;
            ListIterator<String> iterator = exclusionsParam.listIterator();
            while (iterator.hasNext()) {
                String s = iterator.next();
                if (pathMatcher.matches(s, uri)) {
                    return true;
                }
            }
            return false;
        }

        private boolean checkToken(String token) {
            if (!redisTemplate.hasKey(token)) return false;
            redisTemplate.expire(token, 2, TimeUnit.HOURS);
            return true;
        }

    }

    private void returnJSON(HttpServletResponse response, Integer code, String msg) {
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=utf-8");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", code);
        jsonObject.put("msg", msg);
        String jsonString = jsonObject.toJSONString();
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            writer.write(jsonString);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) writer.close();
        }
    }
}
