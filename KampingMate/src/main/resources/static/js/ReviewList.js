/**
 * 
 */

     //검색
    	function submitSearch() {	
	if ($("#searchKeyword").val() == "") {
		alert("검색어를 입력하세요");
		$("#searchKeyword").focus();
		return false;
	}  else{
		var theform = $("#searchForm");
		theform.attr("method", "get");
		theform.attr("action", "/review_search");
		theform.submit();
	}
		}
	
	//상세페이지로 이동		
	 function go_view(review_seq) {
		var theForm = document.createElement('form');
		theForm.method = "get";
		theForm.action = "/review_detail";

		var hiddenSeq = document.createElement('input');
		hiddenSeq.type = "hidden";
		hiddenSeq.name = "review_seq";
		hiddenSeq.value = review_seq;

		theForm.appendChild(hiddenSeq);
		document.body.appendChild(theForm);
		theForm.submit();
	}	
	