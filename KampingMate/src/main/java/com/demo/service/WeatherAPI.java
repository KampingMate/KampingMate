package com.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.demo.dto.UltraSrtFcstResponse;
import com.demo.dto.VilageFcstResponse;
import com.demo.dto.VilageFcstResponse.Item;


public class WeatherAPI {

	private static final String SERVICE_KEY = "WuMkHTh0aSvlWEtIHd7EkY%2B02m%2BOyVb6UcNDRYXc2kRCohnhAvj%2Ft11Zbjb8KuDwusQlhukBJWddx%2FsBexnBeQ%3D%3D";
	private final String BASE_URL = "http://apis.data.go.kr/1360000/";
	
	public int[] convertToKmaCoordinates(double latitude, double longitude) {
        // LCC DFS 좌표 변환 수행
        double DEGRAD = Math.PI / 180.0;

        double RE = 6371.00877; // 지구 반경(km)
        double GRID = 5.0; // 격자 간격(km)
        double SLAT1 = 30.0; // 투영 위도1(degree)
        double SLAT2 = 60.0; // 투영 위도2(degree)
        double OLON = 126.0; // 기준점 경도(degree)
        double OLAT = 38.0; // 기준점 위도(degree)
        double XO = 43; // 기준점 X좌표(GRID)
        double YO = 136; // 기준점 Y좌표(GRID)

        double re = RE / GRID;
        double slat1 = SLAT1 * DEGRAD;
        double slat2 = SLAT2 * DEGRAD;
        double olon = OLON * DEGRAD;
        double olat = OLAT * DEGRAD;

        double sn = Math.tan(Math.PI * 0.25 + slat2 * 0.5) / Math.tan(Math.PI * 0.25 + slat1 * 0.5);
        sn = Math.log(Math.cos(slat1) / Math.cos(slat2)) / Math.log(sn);
        double sf = Math.tan(Math.PI * 0.25 + slat1 * 0.5);
        sf = Math.pow(sf, sn) * Math.cos(slat1) / sn;
        double ro = Math.tan(Math.PI * 0.25 + olat * 0.5);
        ro = re * sf / Math.pow(ro, sn);

        double ra = Math.tan(Math.PI * 0.25 + (latitude) * DEGRAD * 0.5);
        ra = re * sf / Math.pow(ra, sn);
        double theta = longitude * DEGRAD - olon;
        if (theta > Math.PI) theta -= 2.0 * Math.PI;
        if (theta < -Math.PI) theta += 2.0 * Math.PI;
        theta *= sn;

        int nx = (int)Math.floor(ra * Math.sin(theta) + XO + 0.5);
        int ny = (int)Math.floor(ro - ra * Math.cos(theta) + YO + 0.5);

        return new int[]{nx, ny};
    }
	
	//
	public void fetchWeatherData(int nx, int ny) {
        String baseDate = getBaseDate();
        String baseTime = getBaseTime();
        System.out.println(baseDate);
        System.out.println(baseTime);

        RestTemplate restTemplate = new RestTemplate();

        // 초단기 예보 조회
        String ultraSrtFcstUrl = BASE_URL + "VilageFcstInfoService_2.0/getUltraSrtFcst";
        UltraSrtFcstResponse ultraSrtFcstResponse = restTemplate.getForObject(ultraSrtFcstUrl + "?serviceKey={serviceKey}&pageNo={pageNo}&numOfRows={numOfRows}&dataType={dataType}&base_date={base_date}&base_time={base_time}&nx={nx}&ny={ny}", UltraSrtFcstResponse.class, SERVICE_KEY, 1, 1000, "json", baseDate, baseTime, nx, ny);

        if (ultraSrtFcstResponse != null && ultraSrtFcstResponse.getResponse() != null && ultraSrtFcstResponse.getResponse().getBody() != null && ultraSrtFcstResponse.getResponse().getBody().getItems() != null && ultraSrtFcstResponse.getResponse().getBody().getItems().getItem() != null) {
            for (UltraSrtFcstResponse.Item item : ultraSrtFcstResponse.getResponse().getBody().getItems().getItem()) {
                if ("T1H".equals(item.getCategory())) {
                    System.out.println("Current weather: " + item.getFcstValue() + "°C");
                } else if ("SKY".equals(item.getCategory())) {
                    String skyStatusText;
                    switch (item.getFcstValue()) {
                        case "1":
                            skyStatusText = "맑음";
                            break;
                        case "3":
                            skyStatusText = "구름 많음";
                            break;
                        case "4":
                            skyStatusText = "흐림";
                            break;
                        default:
                            skyStatusText = "알 수 없음";
                            break;
                    }
                    System.out.println("Sky status: " + skyStatusText);
                }
            }
        }

        // 단기 예보 조회
        String vilageFcstUrl = BASE_URL + "VilageFcstInfoService_2.0/getVilageFcst";
        VilageFcstResponse vilageFcstResponse = restTemplate.getForObject(vilageFcstUrl + "?serviceKey={serviceKey}&pageNo={pageNo}&numOfRows={numOfRows}&dataType={dataType}&base_date={base_date}&base_time={base_time}&nx={nx}&ny={ny}", VilageFcstResponse.class, SERVICE_KEY, 1, 1000, "json", baseDate, baseTime, nx, ny);

        if (vilageFcstResponse != null && vilageFcstResponse.getResponse() != null && vilageFcstResponse.getResponse().getBody() != null && vilageFcstResponse.getResponse().getBody().getItems() != null && vilageFcstResponse.getResponse().getBody().getItems().getItem() != null) {
            for (VilageFcstResponse.Item item : vilageFcstResponse.getResponse().getBody().getItems().getItem()) {
                if ("TMX".equals(item.getCategory())) {
                    System.out.println("Max temperature: " + item.getFcstValue() + "°C");
                } else if ("TMN".equals(item.getCategory())) {
                    System.out.println("Min temperature: " + item.getFcstValue() + "°C");
                }
            }
        }
    }
	
	public void fetchWeatherDay(String day, int nx, int ny) {
		RestTemplate restTemplate = new RestTemplate();
		String baseDate = getBaseDate();
		String baseTime =getBaseTime();
		
		String vilageFcstUrl = BASE_URL + "VilageFcstInfoService_2.0/getVilageFcst";
        VilageFcstResponse vilageFcstResponse = restTemplate.getForObject(vilageFcstUrl + "?serviceKey={serviceKey}&pageNo={pageNo}&numOfRows={numOfRows}&dataType={dataType}&base_date={base_date}&base_time={base_time}&nx={nx}&ny={ny}", VilageFcstResponse.class, SERVICE_KEY, 1, 1000, "json", baseDate, baseTime, nx, ny);

        if (vilageFcstResponse != null && vilageFcstResponse.getResponse() != null && vilageFcstResponse.getResponse().getBody() != null && vilageFcstResponse.getResponse().getBody().getItems() != null && vilageFcstResponse.getResponse().getBody().getItems().getItem() != null) {
            List<Item> items = vilageFcstResponse.getResponse().getBody().getItems().getItem();
            List<String> temperatureData = new ArrayList<>();
            List<String> timeLabels = new ArrayList<>();
            String targetDate = "";

            if ("today".equals(day)) {
                targetDate = getBaseDate();
            } else if ("tomorrow".equals(day)) {
                targetDate = getTomorrowDate();
            } else if ("dayAfterTomorrow".equals(day)) {
                targetDate = getTomorrowDate();
            }

//            for (Item item : items) {
//                if (targetDate.equals(item.getFcstDate()) && "TMP".equals(item.getCategory())) {
//                    temperatureData.add(item.getFcstValue());
//                    timeLabels.add(item.getFcstTime().substring(0, 2) + "시");
//                }
//            }

//            drawTemperatureGraph(temperatureData, timeLabels);
        }
	}

    private String getTomorrowDate() {
		// TODO Auto-generated method stub
		return null;
	}

	private String getBaseDate() {
        // Implement getBaseDate logic
        return null;
    }

    private String getBaseTime() {
        // Implement getBaseTime logic
        return null;
    }
}
