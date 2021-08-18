package io.github.dbstarll.utils.spring.security;

import io.github.dbstarll.utils.spring.security.ExtendWebAuthenticationDetailsSource.ExtendWebAuthenticationDetails;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

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

        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            } else if (o == null) {
                return false;
            } else if (!(o instanceof ExtendWebAuthenticationDetails)) {
                return false;
            } else if (!super.equals(o)) {
                return false;
            } else {
                final ExtendWebAuthenticationDetails that = (ExtendWebAuthenticationDetails) o;
                return Objects.equals(userAgent, that.userAgent);
            }
        }

        @Override
        public int hashCode() {
            return Objects.hash(super.hashCode(), userAgent);
        }
    }

    @Override
    public ExtendWebAuthenticationDetails buildDetails(final HttpServletRequest context) {
        return new ExtendWebAuthenticationDetails(context);
    }
}
