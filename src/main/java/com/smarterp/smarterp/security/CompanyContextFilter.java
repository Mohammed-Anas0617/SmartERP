package com.smarterp.smarterp.security;

import com.smarterp.smarterp.repository.UserCompanyRepository;
import com.smarterp.smarterp.entity.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class CompanyContextFilter extends OncePerRequestFilter {

    private final UserCompanyRepository userCompanyRepository;

    public CompanyContextFilter(UserCompanyRepository userCompanyRepository) {
        this.userCompanyRepository = userCompanyRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        try {
            String companyIdHeader = request.getHeader("X-Company-Id");

            if (companyIdHeader != null) {
                Long companyId = Long.parseLong(companyIdHeader);

                Object principal = SecurityContextHolder.getContext().getAuthentication() != null
                        ? SecurityContextHolder.getContext().getAuthentication().getPrincipal()
                        : null;

                if (principal instanceof User currentUser) {
                    boolean belongsToUser = userCompanyRepository
                            .existsByUserIdAndCompanyIdAndIsActiveTrue(currentUser.getId(), companyId);

                    if (belongsToUser) {
                        CompanyContextHolder.setCompanyId(companyId);
                    } else {
                        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid company access");
                        return;
                    }
                }
            }

            filterChain.doFilter(request, response);

        } finally {
            CompanyContextHolder.clear();
        }
    }
}
