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
        String api_root_fetch = new NHAPI().fetch_api_root();
        // don't feel like doing an entire Gson app, so just going to do a simple indexof
        assert api_root_fetch.contains("Abandon all hope");
    }
    @Test
    public void basic_search() throws Exception {
        
    }
}
