package io.github.dbstarll.utils.spring.security;

import io.github.dbstarll.utils.spring.security.ExtendWebAuthenticationDetailsSource.ExtendWebAuthenticationDetails;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;

public class ExtendWebAuthenticationDetailsSource
        implements AuthenticationDetailsSource<HttpServletRequest, ExtendWebAuthenticationDetails> {
    public static final class ExtendWebAuthenticationDetails extends WebAuthenticationDetails {
        private static final long serialVersionUID = -8798951602862583760L;

        private final String userAgent;

        private ExtendWebAuthenticationDetails(final HttpServletRequest request) {
            super(request);
            this.userAgent = request.getHeader("user-agent");
        }

        /**
         * 获得user-agent头信息.
         *
         * @return user-agent
         */
        public String getUserAgent() {
            return userAgent;
        }
    }

    @Override
    public ExtendWebAuthenticationDetails buildDetails(final HttpServletRequest context) {
        return new ExtendWebAuthenticationDetails(context);
    }
}
