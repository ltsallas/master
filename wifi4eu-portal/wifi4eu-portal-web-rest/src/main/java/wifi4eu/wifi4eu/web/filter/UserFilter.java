package wifi4eu.wifi4eu.web.filter;


import eu.cec.digit.ecas.client.jaas.DetailedUser;
import wifi4eu.wifi4eu.common.dto.security.RoleDTO;
import wifi4eu.wifi4eu.common.security.UserContext;
import org.springframework.web.filter.OncePerRequestFilter;

import wifi4eu.wifi4eu.common.Constant;
import wifi4eu.wifi4eu.common.ecas.UserHolder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedList;

public class UserFilter extends OncePerRequestFilter {

    @Override
    protected String getAlreadyFilteredAttributeName() {
        return "UserFilter";
    }

    @Override
    protected void initFilterBean() throws ServletException {
        super.initFilterBean();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {

            DetailedUser ecasPrincipal = (DetailedUser) request.getUserPrincipal();

            UserContext user = new UserContext(ecasPrincipal.getDomainUsername());
            user.setEmail(ecasPrincipal.getEmail());
            user.setDomain(ecasPrincipal.getDomain());
            if (ecasPrincipal.getEmployeeNumber() != null) {
                user.setPerId(new Long(ecasPrincipal.getEmployeeNumber()));
            }
            user.setDetailedUser(ecasPrincipal);
            user.setFirstName(ecasPrincipal.getFirstName());
            user.setLastName(ecasPrincipal.getLastName());

            user.setRoleList(new LinkedList<RoleDTO>());

            /*
            String userId = (String) request.getSession(true).getAttribute(Constant.USER);;
            UserContext user = new UserContext(userId);
            */

            if (user == null) {
                user = UserHolder.getUser();
                request.getSession().setAttribute(Constant.USER, user);
            }

            UserHolder.setUser(user);
            filterChain.doFilter(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(401);
        } finally {
            UserHolder.clearUser();
        }
    }
}