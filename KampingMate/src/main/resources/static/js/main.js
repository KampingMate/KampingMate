document.addEventListener('DOMContentLoaded', function() {
    // 위치 정보 가져오기
    function getLocation() {
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(position, showError);
        } else {
            alert("현재 위치를 가져올 수 없습니다.");
        }
    }

    // 위치 정보를 받아와 처리하는 함수
    function position(position) {
        var latitude = position.coords.latitude;
        var longitude = position.coords.longitude;
        console.log("현재 위치: 위도 " + latitude + ", 경도 " + longitude);

        // 변환된 좌표 값 (rs.lat, rs.lng)를 사용하여 nx, ny 좌표로 변환하는 함수
        var nxNy = convertToKmaCoordinates(latitude, longitude);
        console.log("기상청 API에 쓸 격자 좌표 값: nx " + nxNy.nx + ", ny " + nxNy.ny);

        fetchWeatherData(nxNy.nx, nxNy.ny);
        
		// 클릭 이벤트 핸들러 내에서 fetchWeatherDay 호출
		$("#today").click(function() {
			fetchWeatherDay("today", nxNy.nx, nxNy.ny);
		});

		$("#tomorrow").click(function() {
			fetchWeatherDay("tomorrow", nxNy.nx, nxNy.ny);
		});

		$("#dayAfterTomorrow").click(function() {
			fetchWeatherDay("dayAfterTomorrow", nxNy.nx, nxNy.ny);
		});
    }

    // 위치 정보를 가져오는 중 에러 처리
    function showError(error) {
        switch(error.code) {
            case error.PERMISSION_DENIED:
                alert("사용자가 위치 정보 접근을 거부했습니다.");
                break;
            case error.POSITION_UNAVAILABLE:
                alert("위치 정보를 사용할 수 없습니다.");
                break;
            case error.TIMEOUT:
                alert("위치 정보를 가져오는 시간이 초과되었습니다.");
                break;
            case error.UNKNOWN_ERROR:
                alert("알 수 없는 오류가 발생했습니다.");
                break;
        }
    }

    // 위치 정보 가져오기 함수 호출
    getLocation();
});

// LCC DFS 좌표변환을 위한 기초 자료
var RE = 6371.00877; // 지구 반경(km)
var GRID = 5.0; // 격자 간격(km)
var SLAT1 = 30.0; // 투영 위도1(degree)
var SLAT2 = 60.0; // 투영 위도2(degree)
var OLON = 126.0; // 기준점 경도(degree)
var OLAT = 38.0; // 기준점 위도(degree)
var XO = 43; // 기준점 X좌표(GRID)
var YO = 136; // 기준점 Y좌표(GRID)

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
    var apiKey = "WuMkHTh0aSvlWEtIHd7EkY%2B02m%2BOyVb6UcNDRYXc2kRCohnhAvj%2Ft11Zbjb8KuDwusQlhukBJWddx%2FsBexnBeQ%3D%3D";
    var baseDate = getBaseDate();
    var baseTime = getBaseTime();
    console.log(baseDate);
    console.log(baseTime);

    // 초단기 예보 조회
    $.ajax({
        url: `http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtFcst`,
        type: 'GET',
        data: {
            serviceKey: apiKey,
            pageNo: 1,
            numOfRows: 1000,
            dataType: 'json',
            base_date: baseDate,
            base_time: baseTime,
            nx: nx,
            ny: ny
        },
        success: function (data) {
            var items = data.response.body.items.item;
            for (var i = 0; i < items.length; i++) {
                var item = items[i];
                if (item.category == "T1H") {
                    document.getElementById("currentWeather").innerText = item.fcstValue + "°C";
                } else if (item.category == "SKY") {
                    var skyStatus = item.fcstValue;
                    var skyStatusText;

                    if (skyStatus == 1) {
                        skyStatusText = "맑음";
                    } else if (skyStatus == 3) {
                        skyStatusText = "구름 많음";
                    } else if (skyStatus == 4) {
                        skyStatusText = "흐림";
                    } else {
                        skyStatusText = "알 수 없음";
                    }

                    document.getElementById("skyStatus").innerText = skyStatusText;
                }
            }
        },
        error: function (error) {
            console.log("초단기 실황 조회 api 반환 중 오류...", error);
        }
    });

    // 단기 예보 조회
    $.ajax({
        url: `http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst`,
        type: 'GET',
        data: {
            serviceKey: apiKey,
            pageNo: 1,
            numOfRows: 1000,
            dataType: 'json',
            base_date: baseDate,
            base_time: baseTime,
            nx: nx,
            ny: ny
        },
        success: function (data) {
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
        error: function (error) {
            console.log("단기 예보 조회 api 반환 중 오류...", error);
        }
    });
}

// 특정 날짜의 날씨 데이터를 가져와서 차트로 그리는 함수
function fetchWeatherDay(day, nx, ny) {
	var apiKey = "WuMkHTh0aSvlWEtIHd7EkY%2B02m%2BOyVb6UcNDRYXc2kRCohnhAvj%2Ft11Zbjb8KuDwusQlhukBJWddx%2FsBexnBeQ%3D%3D";
	var baseDate = getBaseDate();
	var baseTime = getBaseTime();
	
	$.ajax({
		url: `http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst`,
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
			var temperatureData = [];
			var timeLabels = [];
			var targetDate;
			if (day === "today") {
				targetDate = getBaseDate();
			} else if (day === "tomorrow") {
				targetDate = getTomorrowDate();
			} else if (day === "dayAfterTomorrow") {
				targetDate = getDayAfterTomorrowDate();
			}
			for (var i = 0; i < items.length; i++) {
				var item = items[i];
				if (item.fcstDate === targetDate && item.category === "TMP") {
					temperatureData.push(item.fcstValue);
					timeLabels.push(item.fcstTime.slice(0, 2) + "시");
				}
			}
			drawTemperatureGraph(temperatureData, timeLabels);
		},
		error: function(error) {
			console.error("날씨 데이터 가져오기 실패:", error);
		}
	});
}

// 기온 그래프 그리기
function drawTemperatureGraph(temperature, w_time) {
	var ctx = document.getElementById('temperatureChartCanvas').getContext('2d');

	var existingChart = Chart.getChart(ctx);
	if (existingChart) {
		existingChart.destroy();
	}

	var chartData = {
		labels: w_time,
		datasets: [{
			label: '기온(°C)',
			data: temperature,
			borderColor: 'rgba(255, 99, 132, 1)',
			borderWidth: 1,
			fill: false
		}]
	};

	var options = {
		responsive: true,
		scales: {
			x: {
				title: {
					display: true,
					text: '시간'
				}
			},
			y: {
				display: false // y 축 표시 비활성화
			}
		},
		plugins: {
			tooltip: {
				enabled: true,
				padding: 20, // 패딩 조절
				backgroundColor: 'rgba(0, 0, 0, 0.8)', // 배경색 설정
				titleColor: '#fff', // 제목 색상 설정
				bodyColor: '#fff', // 내용 색상 설정
			},
			scrollbar: {
				axis: 'x',
				limit: 6
			}
		}
	};

	new Chart(ctx, {
		type: 'line',
		data: chartData,
		options: options
	});
}

/*// 기본 날짜와 시간을 얻는 함수
function getBaseDate() {
	var date = new Date();
	return date.toISOString().slice(0, 10).replace(/-/g, '');
}

function getTomorrowDate() {
	var tomorrow = new Date();
	tomorrow.setDate(tomorrow.getDate() + 1);
	return tomorrow.toISOString().slice(0, 10).replace(/-/g, '');
}

function getDayAfterTomorrowDate() {
	var dayAfterTomorrow = new Date();
	dayAfterTomorrow.setDate(dayAfterTomorrow.getDate() + 2);
	return dayAfterTomorrow.toISOString().slice(0, 10).replace(/-/g, '');
}

function getBaseTime() {
	var date = new Date();
	var hours = date.getHours();
	if (hours < 2) {
		date.setDate(date.getDate() - 1);
		return "2300";
	}
	return ("0" + (Math.floor((hours - 1) / 3) * 3 + 2)).slice(-2) + "00";
}*/




// 한국 시간(KST)으로 현재 날짜를 얻는 함수
function getKSTDate(date) {
    // UTC 시간 기준으로 변환
    var utcDate = date.getTime() + (date.getTimezoneOffset() * 60000);
    // UTC+9 시간으로 변환
    var koreaTimeOffset = 9 * 60 * 60 * 1000;
    var koreaDate = new Date(utcDate + koreaTimeOffset);
    return koreaDate;
}

// 기본 날짜 (한국 시간 기준)를 얻는 함수
function getBaseDate() {
    var date = getKSTDate(new Date());
    var year = date.getFullYear();
    var month = ('0' + (date.getMonth() + 1)).slice(-2);
    var day = ('0' + date.getDate()).slice(-2);
    return year + month + day;
}

// 내일 날짜 (한국 시간 기준)를 얻는 함수
function getTomorrowDate() {
    var date = getKSTDate(new Date());
    date.setDate(date.getDate() + 1);
    var year = date.getFullYear();
    var month = ('0' + (date.getMonth() + 1)).slice(-2);
    var day = ('0' + date.getDate()).slice(-2);
    return year + month + day;
}

// 모레 날짜 (한국 시간 기준)를 얻는 함수
function getDayAfterTomorrowDate() {
    var date = getKSTDate(new Date());
    date.setDate(date.getDate() + 2);
    var year = date.getFullYear();
    var month = ('0' + (date.getMonth() + 1)).slice(-2);
    var day = ('0' + date.getDate()).slice(-2);
    return year + month + day;
}

// 기본 시간 (한국 시간 기준)를 얻는 함수
function getBaseTime() {
    var date = getKSTDate(new Date());
    var hours = date.getHours();
    if (hours < 2) {
        date.setDate(date.getDate() - 1);
        return "2300";
    }
    return ("0" + (Math.floor((hours - 1) / 3) * 3 + 2)).slice(-2) + "00";
}
