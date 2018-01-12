package ca.krasnay.sqlbuilder.orm;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

import junit.framework.TestCase;

public class StringMapConverterTest extends TestCase {

    private void assertConvert(StringMapConverter converter, String s, Map<String, String> map) throws SQLException {

        ResultSet rs = createMock(ResultSet.class);
        expect(rs.getString((String) anyObject())).andReturn(s);

        replay(rs);
        assertMapEquals(map, converter.getFieldValueFromResultSet(rs, "columnName"));
        verify(rs);

        assertEquals(s, converter.convertFieldValueToColumn(map));

    }

    private void assertMapEquals(Map<String, String> expected, Map<String, String> actual) {

        assertEquals(expected.size(), actual.size());

        for (String key : expected.keySet()) {
            assertEquals("Checking key " + key, expected.get(key), actual.get(key));
        }
    }


    public void testAll() throws Exception {

        Map<String, String> map = new LinkedHashMap<String, String>();

        StringMapConverter converter = new StringMapConverter();

        assertConvert(converter, "", map);

        map.put("foo", "bar");
        assertConvert(converter, "foo=bar", map);

        map.put("b\\a,z", "q\\uu,x");
        assertConvert(converter, "foo=bar,b\\\\a\\,z=q\\\\uu\\,x", map);

    }

    public void testNullResultsInEmptyMap() throws SQLException {

        ResultSet rs = createMock(ResultSet.class);
        expect(rs.getString((String) anyObject())).andReturn(null);

        replay(rs);

        StringMapConverter converter = new StringMapConverter();

        Map<String, String> value = converter.getFieldValueFromResultSet(rs, "columnName");

        assertNotNull(value);
        assertEquals(0, value.size());

        verify(rs);

    }


}
