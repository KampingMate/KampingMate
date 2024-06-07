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
		theform.attr("action", "/Notice_search");
		theform.submit();
	}
		}
	
	//상세페이지로 이동		
	 function go_view(notice_seq) {
		var theForm = document.createElement('form');
		theForm.method = "get";
		theForm.action = "/Notice_detail";

		var hiddenSeq = document.createElement('input');
		hiddenSeq.type = "hidden";
		hiddenSeq.name = "notice_seq";
		hiddenSeq.value = notice_seq;

		theForm.appendChild(hiddenSeq);
		document.body.appendChild(theForm);
		theForm.submit();
	}	
	
	//페이징
	        function go_page(page) {
            var form = document.searchForm;
            var pageInput = form.querySelector('input[name="page"]');
            pageInput.value = page;
            form.submit();
        }
        
   
   //기본정렬
    	function go_list() {
		var theForm = document.frm;
		theForm.method = "get";
		theForm.action = "Notice_list";
		theForm.submit();
	}
	
	//정렬
	function keyClick(event) {
		var url = '/category';
		var category = event.target.getAttribute('data-category');		
		url += '?category=' + encodeURIComponent(category);
		window.location.href = url;
	}     
	
		//클립보드복사
	function copyboard() {
		var copy = document.createElement('input'), text = window.location.href;

		document.body.appendChild(copy);
		copy.value = text;
		copy.select();
		document.execCommand('copy');
		document.body.removeChild(copy);

		alert('복사되었습니다');
	}
	