package ca.krasnay.sqlbuilder.orm;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

public class StringListConverterTest extends TestCase {

    private void assertConvert(StringListConverter converter, String s, List<String> list) throws SQLException {

        ResultSet rs = createMock(ResultSet.class);
        expect(rs.getString((String) anyObject())).andReturn(s);

        replay(rs);
        assertEquals(list, converter.getFieldValueFromResultSet(rs, "columnName"));
        verify(rs);

        assertEquals(s, converter.convertFieldValueToColumn(list));

    }

    public void testAll() throws Exception {

        List<String> list = new ArrayList<String>();

        StringListConverter converter = new StringListConverter();

        assertConvert(converter, "", list);

        list.add("foo");
        assertConvert(converter, "foo", list);

        list.add("bar");
        assertConvert(converter, "foo,bar", list);

        list.add("b\\a,z");
        assertConvert(converter, "foo,bar,b\\\\a\\,z", list);

    }

    public void testNullResultsInEmptyList() throws SQLException {

        ResultSet rs = createMock(ResultSet.class);
        expect(rs.getString((String) anyObject())).andReturn(null);

        replay(rs);

        StringListConverter converter = new StringListConverter();

        List<String> value = converter.getFieldValueFromResultSet(rs, "columnName");

        assertNotNull(value);
        assertEquals(0, value.size());

        verify(rs);

    }

}
