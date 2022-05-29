package __package__.common.mybatisplus.enums;

/**
 * @author ALazyDogXD
 * @date 2022/5/29 20:00
 * @description 约束相关语句
 */

public enum ReferenceMethod {

    /** 约束相关方法 */
    // INSERT INTO [slave] [(id, ...)] SELECT [id, ...] FROM (SELECT [#{id} id, ...]) [slave] [WHERE EXISTS(SELECT m_id FROM master1 WHERE m_id = #{id}) AND ...]
    SAVE_WITH_CHECK_REFERENCE("saveWithCheckReference", "插入时检查约束", "<script>INSERT INTO %s %s SELECT %s FROM (SELECT %s) %s %s</script>"),
    // [SELECT 'master1' FROM master1 WHERE m_id = #{id} HAVING count(m_id) = 0 UNION ALL SELECT 'master2' FROM master2 WHERE m_id = #{id} HAVING count(m_id) = 0 ...]
    CHECK_REFERENCE("checkReference", "检查约束", "<script>%s</script>"),
    // UPDATE FROM [master] [WHERE id = #{id}] [AND EXISTS(SELECT slave.s_id FROM slave WHERE slave.s_id = master.m_id) ...]
    UPDATE_WITH_CHECK_REFERENCE_RESTRICT("updateWithCheckReference", "更新时检查约束(严格)", "<script>UPDATE FROM %s %s %s</script>");
//    // DELETE FROM [master] [WHERE EXISTS(SELECT s_id)]
//    DELETE_WITH_CHECK_REFERENCE_RESTRICT("deleteWithCheckReference", "删除时检查约束(严格)", "<script>DELETE FROM %s %s</script>");

    private final String method;
    private final String desc;
    private final String sql;

    ReferenceMethod(String method, String desc, String sql) {
        this.method = method;
        this.desc = desc;
        this.sql = sql;
    }

    public String getMethod() {
        return method;
    }

    public String getDesc() {
        return desc;
    }

    public String getSql() {
        return sql;
    }

}
