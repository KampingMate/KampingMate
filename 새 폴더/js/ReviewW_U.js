/**
 * 
 */

 // reviewWrite
 
 async function wsubmitReview() {
    const title = document.getElementById('title').value;
    const kakaoName = document.getElementById('kakao_name').value;
    const kakaoId = document.getElementById('kakao_id').value;
    const content = editor.getHTML();
    const reviewData = {
        title: title,
        kakao_name: kakaoName,
        kakao_id: kakaoId,
        content: content
    };
    const response = await fetch('/reviewwritesubmit', { 
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(reviewData)
    });
    if (response.ok) {
        alert('리뷰가 성공적으로 저장되었습니다.');
        window.location.href = '/review_list';
    } else {
        alert('리뷰 저장에 실패했습니다.');
    }
}
 
    function review_campingsearch() {
        window.open('/kakao-map', '카카오 지도', 'width=800,height=600');
    }

    window.addEventListener('message', function(event) {
        if (event.data.type === 'selectedCampsite') {
            document.getElementById('kakao_name').value = event.data.name;
            document.getElementById('kakao_id').value = event.data.id; 
        }
    });
    
    
    //reviewUpdate
    

    async function usubmitReview() {
    	const review_seq = document.getElementById('review_seq').value;
        const title = document.getElementById('title').value;
        const kakaoName = document.getElementById('kakao_name').value;
        const kakaoId = document.getElementById('kakao_id').value;
        const content = editor.getHTML();
        const reviewData = {
            title: title,
            kakao_name: kakaoName,
            kakao_id: kakaoId,
            content: content,
            review_seq: review_seq
        };
        const response = await fetch('/reviewupdatesubmit', { // URL 수정
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(reviewData)
        });
        if (response.ok) {
            alert('리뷰가 성공적으로 저장되었습니다.');
            window.location.href = '/review_detail?review_seq=' + review_seq;
        } else {
            alert('리뷰 저장에 실패했습니다.');
        }
    }
 