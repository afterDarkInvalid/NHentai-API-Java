package com.github.ttdyce;

import java.util.ArrayList;

import com.github.ttdyce.model.Comic;
import com.github.ttdyce.model.api.PopularType;
import com.github.ttdyce.model.api.ResponseCallback;
import com.github.ttdyce.model.factory.NHApiComicFactory;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    // very simple compile testing
    @Test
    public boolean compilation() {
        return true;
    }
    //simply fetches api root and fetches version/messages
    @Test
    public boolean api_heartbeat() {
        
    }
}
