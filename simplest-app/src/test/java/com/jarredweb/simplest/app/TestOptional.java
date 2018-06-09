package com.jarredweb.simplest.app;

import java.util.Optional;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestOptional {

    @Test
    public void testOptionalIfNull() {
        String value = "10";
        Long id = Long.valueOf(Optional.ofNullable(value).get());
        assertEquals(10l, id.longValue());

        value = null;
        try {
            Long.valueOf(Optional.ofNullable(value).get());
            fail("did not expect to pass");
        } catch (Exception e) {
            assertTrue(e != null);
        }
    }
}
