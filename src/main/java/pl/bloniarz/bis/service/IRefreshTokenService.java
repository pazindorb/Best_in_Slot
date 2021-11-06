package pl.bloniarz.bis.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface IRefreshTokenService {
    void refresh(HttpServletRequest request, HttpServletResponse response);
}
