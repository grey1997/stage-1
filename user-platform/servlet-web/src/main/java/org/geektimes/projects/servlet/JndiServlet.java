package org.geektimes.projects.servlet;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/jndi")
public class JndiServlet extends HttpServlet {

    private static final String SUCCESS_MESSAGE = "获取 JDBC 连接成功";
    private static final String FAILED_MESSAGE = "获取 JDBC 连接失败";

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=utf-8");
        try (Connection connection = getConnection()) {
            if(connection != null) {
                response.getWriter().write(SUCCESS_MESSAGE);
            } else {
                response.getWriter().write(FAILED_MESSAGE);
            }
        } catch (SQLException throwables) {
            response.getWriter().write(FAILED_MESSAGE);
            throwables.printStackTrace();
        } catch (NamingException e) {
            response.getWriter().write(FAILED_MESSAGE);
            e.printStackTrace();
        } catch (IOException e) {
            response.getWriter().write(FAILED_MESSAGE);
            e.printStackTrace();
        }
    }

    private Connection getConnection () throws SQLException, NamingException {
        Context initCtx = new InitialContext();
        Context envCtx = (Context) initCtx.lookup("java:comp/env");
        DataSource ds = (DataSource) envCtx.lookup("jdbc/EmployeeDB");

        Connection conn = ds.getConnection();
        return conn;
    }
}
