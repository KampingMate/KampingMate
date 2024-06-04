package com.demo.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.demo.dto.CampingItem;

public class GoCampingAPI {
    private static final String SERVICE_KEY = "fnb%2FPvsKoyOP9rj3vWkyGBtUun17J%2BDKEakIEuGJgj6Swq1nm6ILNuPBpz0S6hmW3sI8Jc54%2FR%2FB1k5heAIuig%3D%3D";
    private static final String ENDPOINT = "http://apis.data.go.kr/B551011/GoCamping/basedList";

    public static class CampingApiResponse {
        private int totalCount;
        private List<CampingItem> items;

        public CampingApiResponse(int totalCount, List<CampingItem> items) {
            this.totalCount = totalCount;
            this.items = items;
        }

        public int getTotalCount() {
            return totalCount;
        }

        public List<CampingItem> getItems() {
            return items;
        }
    }


    public static CampingApiResponse getCampingSites(int page, int pageSize) throws Exception {
        String urlStr = ENDPOINT +
                "?serviceKey=" + SERVICE_KEY +
                "&numOfRows=" + pageSize +
                "&pageNo=" + page +
                "&MobileOS=AND" +
                "&MobileApp=appName" +
                "&_type=json";
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        int responseCode = conn.getResponseCode();
        System.out.println("Response Code : " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) { // 200
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Parse JSON response
            JSONObject jsonObj = new JSONObject(response.toString());
            JSONObject responseBody = jsonObj.getJSONObject("response").getJSONObject("body");

            // totalCount
            int totalCount = responseBody.getInt("totalCount");

            // items에서 필요한 정보 추출
            List<CampingItem> items = new ArrayList<>();
            JSONArray jsonItems = responseBody.getJSONObject("items").getJSONArray("item");
            for (int i = 0; i < jsonItems.length(); i++) {
                JSONObject item = jsonItems.getJSONObject(i);
                CampingItem campingItem = new CampingItem(
                        item.optString("facltNm", "N/A"),
                        item.optString("lineIntro", "N/A"),
                        item.optString("mgcDiv", "N/A"),
                        item.optString("featureNm", "N/A"),
                        item.optString("addr1", "N/A"),
                        item.optString("addr2", "N/A"),
                        item.optString("tel", "N/A"),
                        item.optString("homepage", "N/A"),
                        item.optString("firstImageUrl", "N/A"),
                        item.optString("createdtime", "N/A"),
                        item.optString("modifiedtime", "N/A"),
                        item.optString("extshrCo", "N/A")
                );
                items.add(campingItem);
            }

            return new CampingApiResponse(totalCount, items);
        } else {
            throw new RuntimeException("API request failed with response code: " + responseCode);
        }
    }
}
