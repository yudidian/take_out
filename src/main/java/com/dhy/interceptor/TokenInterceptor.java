package com.dhy.interceptor;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.dhy.common.BaseContext;
import com.dhy.utils.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;


@Slf4j
public class TokenInterceptor implements HandlerInterceptor {
  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
    // 获取请求头中token
    HashMap<String, Object> map = new HashMap<>();
    String token = request.getHeader("token");
    try {
      DecodedJWT info = JwtUtils.getTokenInfo(token);
      String userId = String.valueOf(info.getClaim("userId")).replace("\"", "");
      Long id = Long.valueOf(userId);
      request.getSession().setAttribute("userId",userId);
      BaseContext.setCurrentId(id);
      return true;
    } catch (SignatureVerificationException e) {
      map.put("msg", "无效签名");
    } catch (TokenExpiredException e) {
      map.put("msg", "token过期");
    } catch (AlgorithmMismatchException e) {
      map.put("msg", "token算法不匹配");
    } catch (Exception e) {
      map.put("msg", "无token");
    }
    map.put("code", 0);
    String json = new ObjectMapper().writeValueAsString(map);
    response.setContentType("application/json;charset=UTF-8");
    response.getWriter().println(json);
    return false;
  }
}
