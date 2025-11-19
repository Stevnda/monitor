package nnu.edu.back.common.utils;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Custom Date Type Handler for handling SQLite timestamp format
 */
@MappedJdbcTypes(JdbcType.TIMESTAMP)
@MappedTypes(Date.class)
public class CustomDateTypeHandler extends BaseTypeHandler<Date> {
    
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Date parameter, JdbcType jdbcType) throws SQLException {
        ps.setTimestamp(i, new Timestamp(parameter.getTime()));
    }

    @Override
    public Date getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return parseDate(rs.getString(columnName));
    }

    @Override
    public Date getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return parseDate(rs.getString(columnIndex));
    }

    @Override
    public Date getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return parseDate(cs.getString(columnIndex));
    }
    
    private Date parseDate(String dateString) throws SQLException {
        if (dateString == null) {
            return null;
        }
        
        try {
            return DATE_FORMAT.parse(dateString);
        } catch (ParseException e) {
            throw new SQLException("Error parsing time stamp: " + dateString, e);
        }
    }
}