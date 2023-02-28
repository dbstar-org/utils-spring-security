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
        private final String method;
        private final String scheme;
        private final String host;
        private final int port;
        private final String uri;

        private ExtendWebAuthenticationDetails(final HttpServletRequest request) {
            super(request);
            this.userAgent = request.getHeader(HttpHeaders.USER_AGENT);
            this.method = request.getMethod();
            this.scheme = request.getScheme();
            this.host = request.getServerName();
            this.port = request.getServerPort();
            this.uri = request.getRequestURI();
        }

        /**
         * 获得user-agent头信息.
         *
         * @return user-agent
         */
        public String getUserAgent() {
            return userAgent;
        }

        /**
         * Returns the name of the HTTP method with which this
         * request was made, for example, GET, POST, or PUT.
         * Same as the value of the CGI variable REQUEST_METHOD.
         *
         * @return a <code>String</code>
         * specifying the name
         * of the method with which
         * this request was made
         */
        public String getMethod() {
            return method;
        }

        /**
         * Returns the name of the scheme used to make this request,
         * for example,
         * <code>http</code>, <code>https</code>, or <code>ftp</code>.
         * Different schemes have different rules for constructing URLs,
         * as noted in RFC 1738.
         *
         * @return a <code>String</code> containing the name
         * of the scheme used to make this request
         */
        public String getScheme() {
            return scheme;
        }

        /**
         * Returns the host name of the server to which the request was sent.
         * It is the value of the part before ":" in the <code>Host</code>
         * header value, if any, or the resolved server name, or the server IP
         * address.
         *
         * @return a <code>String</code> containing the name of the server
         */
        public String getHost() {
            return host;
        }

        /**
         * Returns the port number to which the request was sent.
         * It is the value of the part after ":" in the <code>Host</code>
         * header value, if any, or the server port where the client connection
         * was accepted on.
         *
         * @return an integer specifying the port number
         */
        public int getPort() {
            return port;
        }

        /**
         * Returns the part of this request's URL from the protocol
         * name up to the query string in the first line of the HTTP request.
         * The web container does not decode this String.
         *
         * @return a <code>String</code> containing
         * the part of the URL from the
         * protocol name up to the query string
         */
        public String getUri() {
            return uri;
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
                    .append(getUserAgent(), that.getUserAgent())
                    .append(getMethod(), that.getMethod())
                    .append(getScheme(), that.getScheme())
                    .append(getHost(), that.getHost())
                    .append(getPort(), that.getPort())
                    .append(getUri(), that.getUri())
                    .isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder().appendSuper(super.hashCode())
                    .append(getUserAgent())
                    .append(getMethod())
                    .append(getScheme())
                    .append(getHost())
                    .append(getPort())
                    .append(getUri())
                    .toHashCode();
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", super.toString() + "[", "]")
                    .add("userAgent='" + getUserAgent() + "'")
                    .add("method='" + getMethod() + "'")
                    .add("scheme='" + getScheme() + "'")
                    .add("host='" + getHost() + "'")
                    .add("port='" + getPort() + "'")
                    .add("uri='" + getUri() + "'")
                    .toString();
        }
    }

    @Override
    public ExtendWebAuthenticationDetails buildDetails(final HttpServletRequest context) {
        return new ExtendWebAuthenticationDetails(context);
    }
}
