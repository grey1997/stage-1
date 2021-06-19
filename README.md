# stage-1

作业
在 Tomcat/TomEE 环境，编写一个 Servlet 程序，通过 JNDI 获取 JDBC DataSource，能够获取到正常的 java.sql.Connection 对象即可，输出“获取 JDBC 连接成功”等。

结果
环境：Tomcat

JndiServlet.java
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

context.xml
<?xml version="1.0" encoding="UTF-8"?>
<Context>
    <Resource name="jdbc/EmployeeDB"
              auth="Container"
              type="javax.sql.DataSource"
              username="root"
              password="123456"
              driverClassName="com.mysql.cj.jdbc.Driver"
              url="jdbc:mysql://localhost:3306/test"
              maxTotal="8"
              maxIdle="4"/>
</Context>

web.xml
<web-app ...
    <resource-ref>
        <description>
            Resource reference to a factory for java.sql.Connection
            instances that may be used for talking to a particular
            database that is configured in the Context
            configuration for the web application.
        </description>
        <res-ref-name>
            jdbc/EmployeeDB
        </res-ref-name>
        <res-type>
            javax.sql.DataSource
        </res-type>
        <res-auth>
            Container
        </res-auth>
    </resource-ref>
  ...
  </web-app>
