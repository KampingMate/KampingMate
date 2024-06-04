// OpenWeatherMap API로부터 받은 날씨 정보에서 아이콘 코드를 가져와서 해당 아이콘을 화면에 표시합니다.
function displayWeatherIcon(data) {
    // data.weather[0].icon은 날씨 아이콘 코드를 나타냅니다. 예를 들어 '01d' 또는 '02n' 등입니다.
    const iconCode = data.weather[0].icon;

    // 아이콘 이미지의 URL을 구성합니다.
    const iconUrl = `http://openweathermap.org/img/wn/${iconCode}.png`;

    // w-icon 클래스를 가진 이미지 요소를 선택합니다.
    const iconElement = document.querySelector('.w-icon');

    // 선택된 이미지 요소의 src 속성을 아이콘 이미지의 URL로 설정하여 아이콘을 표시합니다.
    iconElement.src = iconUrl;
}

document.addEventListener('DOMContentLoaded', function() {
	const api_key = "03c3f57d56196c8aa8025401a86c11ee";

	// 위치 정보 가져오기
	if (navigator.geolocation) {
		navigator.geolocation.getCurrentPosition(function(position) {
			const lat = position.coords.latitude;
			const lng = position.coords.longitude;

			// 카카오맵 API를 로드하는 스크립트
			kakao.maps.load(function() {
				const container = document.getElementById('map');
				const options = {
					center: new kakao.maps.LatLng(lat, lng),
					level: 6,
					mapTypeId: kakao.maps.MapTypeId.ROADMAP
				};
				const map = new kakao.maps.Map(container, options);
				const mapTypeControl = new kakao.maps.MapTypeControl();
				map.addControl(mapTypeControl, kakao.maps.ControlPosition.TOPRIGHT);
				const zoomControl = new kakao.maps.ZoomControl();
				map.addControl(zoomControl, kakao.maps.ControlPosition.RIGHT);
				const markerPosition = new kakao.maps.LatLng(lat, lng);
				const marker = new kakao.maps.Marker({
					position: markerPosition
				});
				marker.setMap(map);
			});

			// 날씨 정보 가져오기
			fetch(`https://api.openweathermap.org/data/2.5/weather?lat=${lat}&lon=${lng}&appid=${api_key}&units=metric&lang=kr`)
				.then(response => response.json())
				.then(data => {
					document.querySelector('.temperature').innerText = `현재 온도: ${data.main.temp} °C`;
					document.querySelector('.place').innerText = `현 위치: ${data.name}`;
					document.querySelector('.description').innerText = `하늘 상태: ${data.weather[0].description}`;
					// 아이콘 표시 함수 호출
                    displayWeatherIcon(data);
				})
				.catch(error => alert(error));
			// 일별 날씨 정보 가져오기
            fetch(`https://api.openweathermap.org/data/2.5/onecall?lat=${lat}&lon=${lng}&appid=${api_key}&units=metric&lang=kr`)
                .then(response => response.json())
                .then(data => {
                    const dailyForecast = data.daily;
                    const forecastContainer = document.querySelector('.daily-forecast');

                    dailyForecast.forEach(day => {
                        const date = new Date(day.dt * 1000); // Unix 시간을 JavaScript 날짜 객체로 변환
                        const dateString = `${date.getMonth() + 1}/${date.getDate()}`; // 날짜 문자열 생성

                        const forecastElement = document.createElement('div');
                        forecastElement.classList.add('forecast');

                        // 날짜와 날씨 요약 정보 표시
                        forecastElement.innerHTML = `
                            <p>${dateString}</p>
                            <p>${day.weather[0].description}</p>
                            <p>일출 시간: ${new Date(day.sunrise * 1000).toLocaleTimeString()}</p>
                            <p>일몰 시간: ${new Date(day.sunset * 1000).toLocaleTimeString()}</p>
                            <p>최저 온도: ${day.temp.min} °C</p>
                            <p>최고 온도: ${day.temp.max} °C</p>
                            <img src="http://openweathermap.org/img/wn/${day.weather[0].icon}.png" alt="Weather Icon">
                        `;

                        // 날씨 정보를 담은 요소를 화면에 추가
                        forecastContainer.appendChild(forecastElement);
                    });
                })
                .catch(error => alert(error));
		}, function() {
			alert("좌표를 받아올 수 없음");
		});
	} else {
		alert("Geolocation이 지원되지 않습니다.");
	}
});