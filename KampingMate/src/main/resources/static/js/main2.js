document.addEventListener('DOMContentLoaded', function() {
	const weatherApiKey = "WuMkHTh0aSvlWEtIHd7EkY%2B02m%2BOyVb6UcNDRYXc2kRCohnhAvj%2Ft11Zbjb8KuDwusQlhukBJWddx%2FsBexnBeQ%3D%3D";
	const kakaoApiKey = "03c3f57d56196c8aa8025401a86c11ee";

	// 위치 정보 가져오기
	if (navigator.geolocation) {
		navigator.geolocation.getCurrentPosition(function(position) {
			const latitude = position.coords.latitude;
			const longitude = position.coords.longitude;

			// 카카오맵 API를 로드하는 스크립트
			kakao.maps.load(function() {
				const container = document.getElementById('map');
				const options = {
					center: new kakao.maps.LatLng(latitude, longitude),
					level: 6,
					mapTypeId: kakao.maps.MapTypeId.ROADMAP
				};
				const map = new kakao.maps.Map(container, options);
				const mapTypeControl = new kakao.maps.MapTypeControl();
				map.addControl(mapTypeControl, kakao.maps.ControlPosition.TOPRIGHT);
				const zoomControl = new kakao.maps.ZoomControl();
				map.addControl(zoomControl, kakao.maps.ControlPosition.RIGHT);
				const markerPosition = new kakao.maps.LatLng(latitude, longitude);
				const marker = new kakao.maps.Marker({
					position: markerPosition
				});
				marker.setMap(map);
			});

			// 날씨 API 요청을 위해 좌표 변환 후 처리
			const nxNy = convertToKmaCoordinates(latitude, longitude);
			fetchWeatherData(nxNy.nx, nxNy.ny);
		});
	}
});
// LCC DFS 좌표변환을 위한 기초 자료
var RE = 6371.00877; // 지구 반경(km)
var GRID = 5.0; // 격자 간격(km)
var SLAT1 = 30.0; // 투영 위도1(degree)
var SLAT2 = 60.0; // 투영 위도2(degree)
var OLON = 126.0; // 기준점 경도(degree)
var OLAT = 38.0; // 기준점 위도(degree)
var XO = 43; // 기준점 X좌표(GRID)
var YO = 136; // 기1준점 Y좌표(GRID)

// 현재 위치 정보를 기상청 API에 쓸 격자 좌표로 변환
function convertToKmaCoordinates(latitude, longitude) {
	// LCC DFS 좌표 변환 수행
	var rs = dfs_xy_conv("toXY", latitude, longitude);
	// 반환
	return { nx: rs.x, ny: rs.y };
}

// 변환된 좌표값을 nx, ny 좌표로 변환
function dfs_xy_conv(code, v1, v2) {
	var DEGRAD = Math.PI / 180.0;
	var RADDEG = 180.0 / Math.PI;

	var re = RE / GRID;
	var slat1 = SLAT1 * DEGRAD;
	var slat2 = SLAT2 * DEGRAD;
	var olon = OLON * DEGRAD;
	var olat = OLAT * DEGRAD;

	var sn = Math.tan(Math.PI * 0.25 + slat2 * 0.5) / Math.tan(Math.PI * 0.25 + slat1 * 0.5);
	sn = Math.log(Math.cos(slat1) / Math.cos(slat2)) / Math.log(sn);
	var sf = Math.tan(Math.PI * 0.25 + slat1 * 0.5);
	sf = Math.pow(sf, sn) * Math.cos(slat1) / sn;
	var ro = Math.tan(Math.PI * 0.25 + olat * 0.5);
	ro = re * sf / Math.pow(ro, sn);
	var rs = {};
	if (code == "toXY") {
		rs['lat'] = v1;
		rs['lng'] = v2;
		var ra = Math.tan(Math.PI * 0.25 + (v1) * DEGRAD * 0.5);
		ra = re * sf / Math.pow(ra, sn);
		var theta = v2 * DEGRAD - olon;
		if (theta > Math.PI) theta -= 2.0 * Math.PI;
		if (theta < -Math.PI) theta += 2.0 * Math.PI;
		theta *= sn;
		rs['x'] = Math.floor(ra * Math.sin(theta) + XO + 0.5);
		rs['y'] = Math.floor(ro - ra * Math.cos(theta) + YO + 0.5);
	}
	return rs;
}

function fetchWeatherData(nx, ny) {
	// 기상청 API 호출 및 처리
	var apiKey = "WuMkHTh0aSvlWEtIHd7EkY%2B02m%2BOyVb6UcNDRYXc2kRCohnhAvj%2Ft11Zbjb8KuDwusQlhukBJWddx%2FsBexnBeQ%3D%3D";
	var baseDate = getBaseDate();
	var baseTime = getBaseTime();

	// 초단기 예보 조회
	$.ajax({
		url: `http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtFcst`,
		type: 'GET',
		data: {
			serviceKey: apiKey,
			pageNo: '1',
			numOfRows: '1000',
			dataType: 'json',
			base_date: baseDate,
			base_time: baseTime,
			nx: nx,
			ny: ny
		},
		success: function(data) {
			var items = data.response.body.items.item;
			for (var i = 0; i < items.length; i++) {
				var item = items[i];
				if (item.category == "T1H") {
					document.getElementById("currentWeather").innerText = item.fcstValue + "°C";
				} else if (item.category == "SKY") {
					var skyStatus = item.fcstValue;
					var skyStatusText = skyStatus <= 5 ? "맑음" : skyStatus <= 8 ? "구름 많음" : "흐림";
					document.getElementById("skyStatus").innerText = skyStatusText;
				}
			}
		},
		error: function(error) {
			console.log("초단기 실황 조회 api 반환 중 오류...", error);
		}
	});

	// 단기 예보 조회
	$.ajax({
		url: `https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst`,
		type: 'GET',
		data: {
			serviceKey: apiKey,
			pageNo: '1',
			numOfRows: '1000',
			dataType: 'json',
			base_date: baseDate,
			base_time: baseTime,
			nx: nx,
			ny: ny
		},
		success: function(data) {
			var items = data.response.body.items.item;
			for (var i = 0; i < items.length; i++) {
				var item = items[i];
				if (item.category == "TMX") {
					document.getElementById("maxTemperature").innerText = item.fcstValue + "°C";
				} else if (item.category == "TMN") {
					document.getElementById("minTemperature").innerText = item.fcstValue + "°C";
				}
			}
		},
		error: function(error) {
			console.log("단기 예보 조회 api 반환 중 오류...", error);
		}
	});
}

function getBaseDate() {
	var date = new Date();
	return date.toISOString().slice(0, 10).replace(/-/g, '');
}

function getBaseTime() {
	var date = new Date();
	var hours = date.getHours();
	if (hours < 2) {
		date.setDate(date.getDate() - 1);
		return "2300";
	}
	return ("0" + (Math.floor((hours - 1) / 3) * 3 + 2)).slice(-2) + "00";
}

$(document).ready(function() {
	getLocation();
});
