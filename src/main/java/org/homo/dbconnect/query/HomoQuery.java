package org.homo.dbconnect.query;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author wujianchuan 2019/1/3
 */
public class HomoQuery extends AbstractQuery implements Query {

    public HomoQuery(String sql, Connection connection) {
        super(sql, connection);
    }

    @Override
    public Object unique() throws SQLException {
        PreparedStatement preparedStatement = this.connection.prepareStatement(sql);
        String str = sql.replaceAll("\\s*", "").toUpperCase();
        int columnCount = str
                .substring(str.indexOf("SELECT") + 6, str.indexOf("FROM"))
                .split(",")
                .length;
        Object[] result = new Object[columnCount];
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            for (int index = 1; index <= columnCount; index++) {
                result[index - 1] = resultSet.getObject(index);
            }
        }
        return result;
    }
}
