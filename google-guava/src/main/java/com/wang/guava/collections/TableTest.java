package com.wang.guava.collections;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.junit.Test;

import java.util.Map;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * @description: Guava Table：ArrayTable、HashBasedTable、HashBaseTable、ImmutableTable
 * @author: wei·man cui
 * @date: 2020/8/25 11:32
 */
public class TableTest {
    @Test
    public void testTableBasic() {
        // Table<R, C, V>：Row行，Column列，Value值
        Table<String, String, String> table = HashBasedTable.create();
        table.put("Language", "Java", "8");
        table.put("Language", "Scala", "29.0");
        table.put("SQL", "Oracle", "12C");
        table.put("SQL", "MySql", "8.0");
        System.out.println(table);
        Map<String, String> rowSql = table.row("SQL");
        assertThat(rowSql.containsKey("MySql"), equalTo(true));
        System.out.println(rowSql.get("Oracle"));

        Set<Table.Cell<String, String, String>> cells = table.cellSet();
        System.out.println("【CellSet】= " + cells);
    }

}
