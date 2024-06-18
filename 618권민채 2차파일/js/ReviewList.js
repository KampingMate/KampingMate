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
	
	    function go_next_page() {
        var nextPage = Math.floor((pageNumber - 1) / 10) * 10 + 11;
        if (nextPage > totalPages) {
            nextPage = totalPages;
        }
        go_page(nextPage);
    }
    
    function go_pri_page() {
        var prevPage = Math.floor((pageNumber - 1) / 10) * 10 - 9;
        if (prevPage < 1) {
            prevPage = 1;
        }
        go_page(prevPage);
    }
    
	   function go_page(page) {
    var form = document.forms['searchForm'];
    var pageInput = form.querySelector('input[name="page"]');
    var sortInput = form.querySelector('input[name="sort"]');
    
    if (!sortInput) {
        sortInput = document.createElement('input');
        sortInput.type = 'hidden';
        sortInput.name = 'sort';
        sortInput.value = 'cnt_sort'; // 기본값 설정
        form.appendChild(sortInput);
    }
    
    pageInput.value = page;
    form.submit();
}



    
        document.addEventListener('DOMContentLoaded', function() {
        var pageNumber = /*[[${pageNumber}]]*/ 1;
        var totalPages = /*[[${totalPages}]]*/ 1;


        var nextButton = document.getElementById('nextButton');
        if (nextButton) {
            nextButton.addEventListener('click', function() {
                console.log("Next button clicked");
                var nextPage = Math.floor((pageNumber - 1) / 10) * 10 + 11;
                console.log("Next page calculated:", nextPage);
                if (nextPage <= totalPages) {
                    go_page(nextPage);
                } else {
                    go_page(totalPages);
                }
            });
        }

        console.log("pageNumber:", pageNumber);
        console.log("totalPages:", totalPages);
    });
        
   
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
    var form = document.forms['searchForm'];
    var sortInput = form.querySelector('input[name="sort"]');

    if (!sortInput) {
        sortInput = document.createElement('input');
        sortInput.type = 'hidden';
        sortInput.name = 'sort';
        form.appendChild(sortInput);
    }
    sortInput.value = category;

    form.action = '/sorted_Review';
    form.submit();
}



	