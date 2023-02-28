package io.github.dbstarll.utils.spring.security;

import io.github.dbstarll.utils.spring.security.ExtendWebAuthenticationDetailsSource.ExtendWebAuthenticationDetails;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;
import java.util.StringJoiner;

public class ExtendWebAuthenticationDetailsSource
        implements AuthenticationDetailsSource<HttpServletRequest, ExtendWebAuthenticationDetails> {
    public static final class ExtendWebAuthenticationDetails extends WebAuthenticationDetails {
        private static final long serialVersionUID = -8798951602862583760L;

        private final String userAgent;

        private ExtendWebAuthenticationDetails(final HttpServletRequest request) {
            super(request);
            this.userAgent = request.getHeader(HttpHeaders.USER_AGENT);
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
            } else if (!(o instanceof ExtendWebAuthenticationDetails)) {
                return false;
            }
            final ExtendWebAuthenticationDetails that = (ExtendWebAuthenticationDetails) o;
            return new EqualsBuilder().appendSuper(super.equals(o))
                    .append(getUserAgent(), that.getUserAgent()).isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder().appendSuper(super.hashCode()).append(getUserAgent()).toHashCode();
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", super.toString() + "[", "]")
                    .add("userAgent='" + userAgent + "'")
                    .toString();
        }
    }

    @Override
    public ExtendWebAuthenticationDetails buildDetails(final HttpServletRequest context) {
        return new ExtendWebAuthenticationDetails(context);
    }
}
