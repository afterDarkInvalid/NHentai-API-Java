package com.github.ttdyce.model.api;

import java.io.IOException;
import java.util.Locale;

import com.github.ttdyce.model.Request;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

public class NHAPI {
    private static final String TAG = "NHAPI";

    public NHAPI() {
    }
    
    public String fetch_api_root() throws Exception {
        // simply queries the api root and verifies it still exists
        String api_root_url = URLs.API_ROOT;
        Request root_fetch = new Request();
        String response = root_fetch.get(api_root_url);
        System.out.println(response);
        return response;
    }

    public void getComicList(final ResponseCallback callback) throws IOException {
        getComicList("", 1, false, callback);
    }

    public void getComicList(String query, final ResponseCallback callback) throws IOException {
        getComicList(query, 1, false, callback);
    }

    public void getComicList(String query, int page, final ResponseCallback callback) throws IOException {
        getComicList(query, page, false, callback);
    }

    public void getComicList(String query, boolean sortedPopular, final ResponseCallback callback) throws IOException {
        getComicList(query, 1, sortedPopular, callback);
    }

    /*
     * Return a JsonArray string containing 25 Comic object, as [ {"id":
     * 284928,"media_id": "1483523",...}, ...]
     */

    public void getComicList(String query, int page, boolean sortedPopular, final ResponseCallback callback)
            throws IOException {
        // TODO: 2019/10/1 Function is limited if query == "" && language == all, by the API (2019/12/22)
        getComicList("", query, page, sortedPopular, callback);

    }

    public void getComicList(String language, String query, int page, boolean sortedPopular,
            final ResponseCallback callback) throws IOException {
        getComicList(language, query, page, sortedPopular, PopularType.allTime, callback);
    }

    public void getComicList(String language, String query, int page, boolean sortedPopular, PopularType popularType,
            final ResponseCallback callback) throws IOException {
        String languageQuery = "language:" + language;
        if (language.equals("") || language.equals("all"))
            languageQuery = "";

        String fullQuery = languageQuery + "" + query;
        String url = URLs.search(fullQuery, page, sortedPopular, popularType);
        if (languageQuery.equals("") && query.equals(""))
            url = URLs.getIndex(page);// forward to index if language, query == ""

        System.out.println("");
        System.out.println(TAG + " getComicList: loading from url " + url);
        System.out.println("");

        Request request = new Request();
        String response = request.get(url);
        
        // Check for 503 response. This means that Cloudflare is refusing the play nice
        // 503 example: " 503, Service Temporarily Unavailable"
        // Send an error with the response
        // swapping out contains so it doesn't trip if the body text contains 503, not yet tested but hey
        if (response == null || response.startsWith(" 503")) {
            
            callback.onError(response.toString());
            
        } else {
            
            // Otherwise, compose the result as a json array and return it
            // System.out.println("Response raw: " + response);
            JsonArray result = new JsonParser().parse(response).getAsJsonObject().get("result").getAsJsonArray();
            callback.onReponse(result.toString());
            
        }

    }

    public void getComic(int id, final ResponseCallback callback) throws IOException {
        String url = URLs.getComic(id);

        System.out.println("");
        System.out.println(TAG + " getComic: loading from url " + url);
        System.out.println("");

        Request request = new Request();
        String response = request.get(url);
        
        // Check for 503, same as with getComicList.
        if ("503".equals(response.substring(0, 3))) {
            
            callback.onError(response.toString());
            
        } else {
            
            callback.onReponse(response);
            
        }
        
    }

    // https://nhentai.net/api/galleries/search?query=language:chinese&page=1&sort=popular
    // https://nhentai.net/api/gallery/284987
    public static class URLs {
        public final static String API_ROOT = "https://nhentai.net/api/v2";
        private final static String searchPrefix = API_ROOT + "/galleries?query=";
        private final static String getComicPrefix = API_ROOT + "/gallery/";
        private final static String[] types = { "jpg", "png" };

        public static String search(String query, int page, boolean sortedPopular) {
            return URLs.search(query, page, sortedPopular, PopularType.allTime);
        }

        public static String search(String query, int page, boolean sortedPopular, PopularType popularType) {
            if (!sortedPopular)
                return searchPrefix + query + "&page=" + page;

            if (popularType == PopularType.allTime || popularType == null)
                return searchPrefix + query + "&page=" + page + "&sort=popular";
            if (popularType == PopularType.year)
                return searchPrefix + query + "&page=" + page + "&sort=popular-year";
            if (popularType == PopularType.month)
                return searchPrefix + query + "&page=" + page + "&sort=popular-month";
            if (popularType == PopularType.week)
                return searchPrefix + query + "&page=" + page + "&sort=popular-week";
            if (popularType == PopularType.today)
                return searchPrefix + query + "&page=" + page + "&sort=popular-today";

            return "";// should be not needed
        }

        public static String getComic(int id) {
            return getComicPrefix + id;
        }

        public static String getThumbnail(String mid, String type) {
            for (String t : types) {
                if (t.charAt(0) == type.charAt(0))
                    return String.format(Locale.ENGLISH, "https://t.nhentai.net/galleries/%s/thumb.%s", mid, t);
            }

            return "";// should be not needed
        }

        public static String getPage(String mid, int page, String type) {
            for (String t : types) {
                if (t.charAt(0) == type.charAt(0))
                    return String.format(Locale.ENGLISH, "https://i.nhentai.net/galleries/%s/%d.%s", mid, page, t);
            }

            return "";// should be not needed
        }

        public static String getIndex(int page) {
            return API_ROOT + "/galleries?page=" + page;
        }
    }
}
