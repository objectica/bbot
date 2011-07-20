package com.objectica.bbot.rumblepad;

import com.objectica.bbot.rumblepad.actions.ButtonAction;
import com.objectica.bbot.rumblepad.actions.ControlAction;
import com.objectica.bbot.rumblepad.parser.RumblepadActionParser;
import com.objectica.bbot.rumblepad.utils.HexUtils;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class TestRumblepadActionParser {
    private static final Logger logger = LoggerFactory.getLogger(TestRumblepadActionParser.class);
    private RumblepadActionParser parser = new RumblepadActionParser();
    private static Map<ControlAction[], byte[]> buttonsDataSet = new LinkedHashMap<ControlAction[], byte[]>();

    static {
        ButtonAction button1 = new ButtonAction(1);
        ButtonAction button2 = new ButtonAction(2);
        ButtonAction button3 = new ButtonAction(3);
        ButtonAction button4 = new ButtonAction(4);
        ButtonAction button5 = new ButtonAction(5);
        ButtonAction button6 = new ButtonAction(6);
        ButtonAction button7 = new ButtonAction(7);
        ButtonAction button8 = new ButtonAction(8);
        ButtonAction button9 = new ButtonAction(9);
        ButtonAction button10 = new ButtonAction(10);

        buttonsDataSet.put(new ControlAction[]{},        new byte[]{-128, -128, -128, -128, 8, 0, 8, -4});
        buttonsDataSet.put(new ControlAction[]{button1}, new byte[]{-128, -128, -128, -128, 24, 0, 8, -4});
        buttonsDataSet.put(new ControlAction[]{button2}, new byte[]{-128, -128, -128, -128, 40, 0, 8, -4});
        buttonsDataSet.put(new ControlAction[]{button3}, new byte[]{-128, -128, -128, -128, 72, 0, 8, -4});
        buttonsDataSet.put(new ControlAction[]{button4}, new byte[]{-128, -128, -128, -128, -120, 0, 8, -4});
        buttonsDataSet.put(new ControlAction[]{button5}, new byte[]{-128, -128, -128, -128, 8, 1, 8, -4});
        buttonsDataSet.put(new ControlAction[]{button6}, new byte[]{-128, -128, -128, -128, 8, 2, 8, -4});
        buttonsDataSet.put(new ControlAction[]{button7}, new byte[]{-128, -128, -128, -128, 8, 4, 8, -4});
        buttonsDataSet.put(new ControlAction[]{button8}, new byte[]{-128, -128, -128, -128, 8, 8, 8, -4});
        buttonsDataSet.put(new ControlAction[]{button9}, new byte[]{-128, -128, -128, -128, 8, 16, 8, -4});
        buttonsDataSet.put(new ControlAction[]{button10}, new byte[]{-128, -128, -128, -128, 8, 32, 8, -4});

        buttonsDataSet.put(new ControlAction[]{button1, button2}, new byte[]{-128, -128, -128, -128, 56, 0, 8, -4});
        buttonsDataSet.put(new ControlAction[]{button2, button4}, new byte[]{-128, -128, -128, -128, -88, 0, 8, -4});
        buttonsDataSet.put(new ControlAction[]{button3, button4}, new byte[]{-128, -128, -128, -128, -56, 0, 8, -4});
    }

    @Test
    public void testButtonsDataSet() {
        for (ControlAction[] actions : buttonsDataSet.keySet()) {
            byte[] data = buttonsDataSet.get(actions);
            logger.info("Testing "+ HexUtils.toByteString(data));
            Collection<ControlAction> resultActions = parser.parseActions(data);
            Assert.assertNotNull(resultActions);
            Assert.assertEquals(actions.length, resultActions.size());
            Assert.assertArrayEquals(actions, resultActions.toArray());
        }
    }
}
