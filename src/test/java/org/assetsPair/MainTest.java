package org.assetsPair;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import static org.junit.Assert.*;

public class MainTest {
    private final InputStream systemIn = System.in;

    @Before
    public void setUpInput() {
        // Before each test, we redirect the input stream
        String input = "/q\n"; // Input string to test
        ByteArrayInputStream testIn = new ByteArrayInputStream(input.getBytes());
        System.setIn(testIn);
    }

    @After
    public void restoreSystemInputOutput() {
        // After each test, we restore system input/output
        System.setIn(systemIn);
    }

    @Test
    public void testIsQuitSignal() {
        assertTrue(Main.isQuitSignal("/q"));
        assertFalse(Main.isQuitSignal("USDT/PLN"));
    }

    @Test
    public void testIsValidAssetPair() {
        assertTrue(Main.isValidAssetPair("USDT/PLN"));
        assertFalse(Main.isValidAssetPair("USDT/GBP"));
    }

    @Test
    public void testPromptAssetPair() {
        assertEquals("/q", Main.promptAssetPair());
    }
}

