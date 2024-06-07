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
		theForm.action = "review_list";
		theForm.submit();
	}
	
	//인기도 정렬
	function keyClick(event) {
		var category = event.target.getAttribute('data-category');
		var url = '/category';
		if (category === 'cnt_sort' || category === 'goodpoint_sort'
				|| category === 'bookmark_sort') {
			url = '/sorted_Review';
			url += '?sort=' + category;
		} else {
			url += '?category=' + encodeURIComponent(category);
		}
		window.location.href = url;
	}     
	