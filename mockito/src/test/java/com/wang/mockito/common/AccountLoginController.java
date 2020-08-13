package com.wang.mockito.common;

import javax.servlet.http.HttpServletRequest;

/**
 * @description: 用户登录 控制器测试
 * @author: wei·man cui
 * @date: 2020/8/13 10:37
 */
public class AccountLoginController {

    private final AccountDao accountDao;

    public AccountLoginController(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    public String login(HttpServletRequest request) {
        final String userName = request.getParameter("userName");
        final String password = request.getParameter("password");
        try {
            Account account = accountDao.findAccount(userName, password);
            if (account == null) {
                return "login";
            } else {
                return "index";
            }
        } catch (Exception e) {
            // DB 不可用了
            return "505";
        }
    }


}
