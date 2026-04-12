package com.github.ttdyce;

import org.junit.Test;

import com.github.ttdyce.model.api.NHAPI;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    // very simple compile testing
    @Test
    public void compilation() {
        System.out.println("Compiled properly!");
        assert true;
    }
    //simply fetches api root and fetches version/messages
    @Test
    public void api_heartbeat() throws Exception {
        System.out.println(new NHAPI().fetch_api_root());
    }
}
