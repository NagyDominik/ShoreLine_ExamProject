/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shoreline_examproject.Utility;

import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Dominik
 */
public class EventLoggerTest {

    /**
     * Test of log method, of class EventLogger.
     */
    @Test
    public void testLog() {
        System.out.println("log");
        EventLogger.Level level = EventLogger.Level.NOTIFICATION;
        String desc = "LogUnittest";
        EventLogger.log(level, desc);
        
        int expSize = 1;
        String expLogType = "NOTIFICATION";
        String expDesc = "LogUnittest";
        
        int resSize = EventLogger.getLog().size();
        String resLogType = EventLogger.getLog().get(0).getType().name();
        String resDesc = EventLogger.getLog().get(0).getDescription();
        assertEquals(expSize, resSize);
    }

    /**
     * Test of logIncognito method, of class EventLogger.
     */
    @Test
    public void testLogIncognito() {
        EventLogger.getLog().addListener(new ListChangeListener<EventLog>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends EventLog> c) {
                if (EventLogger.isSetUp()) {
                    while (c.next()) {
                        if (c.wasAdded() && EventLogger.isObservable()) {
                            fail("New EventLog has been detected!");
                        }
                    }
                }
            }
        });
        
        System.out.println("logIncognito");
        EventLogger.Level level = EventLogger.Level.ERROR;
        String desc = "LogIncognitoUnittest";
        EventLogger.setIsSetUp(Boolean.TRUE);
        EventLogger.logIncognito(level, desc);
        
        int expSize = 2;
        int resSize = EventLogger.getLog().size();
        
        assertEquals(expSize, resSize);
    }
  
}
