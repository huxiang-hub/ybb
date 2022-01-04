package com.anaysis.utis;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @Author lzb
 * @Date 2021/3/24
 **/
@Slf4j
@Component
public class ProceduerUtil {

    @Resource(name = "secondaryJdbcTemplate")
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private TransactionTemplate template;


    public List<JSONObject> execute(String procedureName, List<String> paramList, HashMap<String, String> paramMap) {
        ProcedureReturnListTransactionCallback callback = new ProcedureReturnListTransactionCallback(procedureName, paramList, paramMap);
        return template.execute(callback);
    }

    class ProcedureReturnListTransactionCallback implements TransactionCallback<List<JSONObject>> {

        private final String procedureName;
        private final List<String> paramList;
        private final HashMap<String, String> paramMap;
        private int size = 0;

        ProcedureReturnListTransactionCallback(String procedureName, List<String> paramList, HashMap<String, String> paramMap) {
            this.procedureName = procedureName;
            this.paramList = paramList;
            this.paramMap = paramMap;
            if (paramList != null && paramList.size() > 0) {
                this.size = paramList.size();
            } else if (paramMap != null && paramMap.size() > 0) {
                this.size = paramMap.size();
            }
        }

        @Override
        public List<JSONObject> doInTransaction(TransactionStatus transactionStatus) {
            return jdbcTemplate.execute(new CallableStatementCreator() {

                @Override
                public CallableStatement createCallableStatement(Connection con) throws SQLException {
                    String procedureStr = buildProcedureStr(procedureName, size);
                    CallableStatement cs = con.prepareCall(procedureStr);
                    buildParame(cs, paramList, paramMap);
                    return cs;
                }

            }, new CallableStatementCallback<List<JSONObject>>() {
                @Override
                public List<JSONObject> doInCallableStatement(CallableStatement cs)
                        throws SQLException, DataAccessException {
                    try {
                        ResultSet rs = cs.executeQuery();
                        return resultSetToJson(rs);
                    }catch (SQLException e){
//                        cs.execute();
                        log.error(e.getMessage());
                        return null;
                    }
                }
            });
        }
    }

    private void buildParame(CallableStatement cs, List<String> paramList, HashMap<String, String> paramMap) throws SQLException {
        if (paramMap != null && paramMap.size() > 0) {
            paramMap.forEach((k, v) -> {
                try {
                    cs.setString(k, v);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            });
        } else if (paramList != null && paramList.size() > 0) {
            for (int i = 0; i < paramList.size(); i++) {
                cs.setString(i + 1, paramList.get(i));
            }
        }
    }

    private String buildProcedureStr(String procedureName, Integer size) {
        StringBuilder procedureStr = new StringBuilder();
        procedureStr.append("{call ");
        procedureStr.append(procedureName);
        if (size > 0) {
            procedureStr.append("(");
            for (int i = 0; i < size; i++) {
                if (i < size - 1) {
                    procedureStr.append("?,");
                } else {
                    procedureStr.append("?)");
                }
            }
        }
        procedureStr.append("}");
        return procedureStr.toString();
    }

    private List<JSONObject> resultSetToJson(ResultSet rs) throws SQLException, JSONException {
        ArrayList<JSONObject> jsonObjects = new ArrayList<>();
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();
        while (rs.next()) {
            JSONObject jsonObj = new JSONObject();
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnLabel(i);
                String value = rs.getString(columnName);
                jsonObj.put(columnName, value);
            }
            jsonObjects.add(jsonObj);
        }
        return jsonObjects;
    }

}
