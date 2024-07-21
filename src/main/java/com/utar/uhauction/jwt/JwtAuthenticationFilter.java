package com.utar.uhauction.jwt;

import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final PathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            if(isProtectedUrl(request)) {
                if(!request.getMethod().equals("OPTIONS"))
                    request = JwtUtil.validateTokenAndAddUserIdToHeader(request);
            }
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
            return;
        }
        filterChain.doFilter(request, response);
    }

    private boolean isProtectedUrl(HttpServletRequest request) {
        List<String> protectedPaths = new ArrayList<String>();
        protectedPaths.add("/admin/login/status");
        protectedPaths.add("/ums/user/info");
        protectedPaths.add("/ums/user/update");
        protectedPaths.add("/item/create");
        protectedPaths.add("/item/pay");
        protectedPaths.add("/item/setEnd");
        protectedPaths.add("/item/update");
        protectedPaths.add("/item/delete/*");
        protectedPaths.add("/fund/update");
        protectedPaths.add("/fund/add");
        protectedPaths.add("/comment/add_comment");
        protectedPaths.add("/relationship/subscribe/*");
        protectedPaths.add("/relationship/unsubscribe/*");
        protectedPaths.add("/relationship/validate/*");
        protectedPaths.add("/bid/add_bid");
        protectedPaths.add("/bid/update_bid");
        protectedPaths.add("/uhauction/item/img/*");
        protectedPaths.add("/favorite/*");

        boolean bFind = false;
        for( String passedPath : protectedPaths ) {
            bFind = pathMatcher.match(passedPath, request.getServletPath());
            if( bFind ) {
                break;
            }
        }
        return bFind;
    }

}