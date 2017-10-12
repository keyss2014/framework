package cn.keyss.server.data;

import cn.keyss.server.data.command.DbQuery;

import java.util.Map;

/**
 * 数据库引擎
 * <p>
 * 主要为动态查询服务
 */
public interface DataEngine {

    /**
     * 解析结果
     */
    public static class ParseResult {

        private String sqlText;
        private Map<String, Object> dynamicParameters;

        /**
         * Sql脚本
         */
        public String getSqlText() {
            return sqlText;
        }

        public void setSqlText(String sqlText) {
            this.sqlText = sqlText;
        }

        /**
         * 解析参数
         *
         * @return
         */
        public Map<String, Object> getDynamicParameters() {
            return dynamicParameters;
        }

        public void setDynamicParameters(Map<String, Object> dynamicParameters) {
            this.dynamicParameters = dynamicParameters;
        }
    }

    /**
     * 从DbQuery查询解析出查询数量SQL语句
     *
     * @param query 查询
     * @return SQL语句
     */
    ParseResult parseDbQuerySelectCountSql(DbQuery query);

    /**
     * 从DbQuery查询解析出查询记录的SQL语句
     *
     * @param query 查询
     * @return SQL语句
     */
    ParseResult parseDbQuerySelectSql(DbQuery query);
}
