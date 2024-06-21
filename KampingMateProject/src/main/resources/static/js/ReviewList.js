// 검색
function submitSearch() {
    var keyword = document.getElementById('searchKeyword').value;
    if (keyword === "") {
        alert("검색어를 입력하세요");
        document.getElementById('searchKeyword').focus();
        return false;
    } else {
        document.getElementById('searchForm').action = "/review_search";
        document.getElementById('searchForm').submit();
        return true;
    }
}

// 상세페이지로 이동
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

// 인기순 정렬
function keyClick(event) {
    var category = event.target.getAttribute('data-category');
    document.getElementById('sort').value = category;
    document.getElementById('page').value = 1;

    console.log("정렬 키 클릭 : " + category);

    var searchForm = document.getElementById('searchForm');
    searchForm.action = "/sorted_Review";  
    searchForm.submit();
}

// 페이징
function go_page(page) {
    var form = document.getElementById('searchForm');
    var sortInput = document.getElementById('sort');
    var searchType = document.getElementById('searchType').value;
    var searchKeyword = document.getElementById('searchKeyword').value;

    var params = new URLSearchParams(new FormData(form));
    params.set('page', page);
    if (sortInput.value) {
        params.set('sort', sortInput.value);
    }
    if (searchType) {
        params.set('searchType', searchType);
    }
    if (searchKeyword) {
        params.set('searchKeyword', searchKeyword);
    }

    var url = "/sorted_Review?" + params.toString();
    if (!sortInput.value) {
        url = "/review?" + params.toString();
    }
    console.log("go_page - URL: " + url);

    window.location.href = url;
}

// 최신순 정렬
function go_list() {
    var form = document.getElementById('searchForm');
    document.getElementById('sort').value = '';  
    document.getElementById('page').value = 1;

    var params = new URLSearchParams(new FormData(form));
    params.set('page', 1);

    form.action = '/review?' + params.toString();
    form.submit();
}

window.onload = function() {
    var urlParams = new URLSearchParams(window.location.search);
    var sort = urlParams.get('sort');
    if (sort) {
        document.getElementById('sort').value = sort;
    }
    renderPagination();
};

function renderPagination() {
    var paginationContainer = document.getElementById('pagination');
    if (!paginationContainer) {
        console.error('Pagination container not found');
        return;
    }

    var totalPages = parseInt(paginationContainer.getAttribute('data-total-pages'));
    var pageNumber = parseInt(paginationContainer.getAttribute('data-page-number'));

    if (isNaN(totalPages) || isNaN(pageNumber)) {
        console.error('Invalid totalPages or pageNumber');
        return;
    }

    paginationContainer.innerHTML = '';

    if (pageNumber > 1) {
        var prevPage = document.createElement('li');
        prevPage.innerHTML = '<a href="javascript:void(0);" onclick="go_page(' + (pageNumber - 1) + ')">&laquo; 이전</a>';
        paginationContainer.appendChild(prevPage);
    }

    for (var i = 1; i <= totalPages; i++) {
        var pageItem = document.createElement('li');
        pageItem.innerHTML = '<a href="javascript:void(0);" onclick="go_page(' + i + ')" class="' + (i == pageNumber ? 'active' : '') + '">' + i + '</a>';
        paginationContainer.appendChild(pageItem);
    }

    if (pageNumber < totalPages) {
        var nextPage = document.createElement('li');
        nextPage.innerHTML = '<a href="javascript:void(0);" onclick="go_page(' + (pageNumber + 1) + ')">다음 &raquo;</a>';
        paginationContainer.appendChild(nextPage);
    }
}


window.onload = function() {
    var urlParams = new URLSearchParams(window.location.search);
    var sort = urlParams.get('sort');
    if (sort) {
        document.getElementById('sort').value = sort;
    }
    renderPagination();
};

function renderPagination() {
    var paginationContainer = document.getElementById('pagination');
    if (!paginationContainer) {
        console.error('Pagination container not found');
        return;
    }

    var totalPages = parseInt(paginationContainer.getAttribute('data-total-pages'));
    var pageNumber = parseInt(paginationContainer.getAttribute('data-page-number'));

    console.log("totalPages: " + totalPages);  // 디버그 로그 추가
    console.log("pageNumber: " + pageNumber);  // 디버그 로그 추가

    if (isNaN(totalPages) || isNaN(pageNumber)) {
        console.error('Invalid totalPages or pageNumber');
        return;
    }

    paginationContainer.innerHTML = '';

    var currentBlock = Math.floor((pageNumber - 1) / 10) + 1;
    var startPage = (currentBlock - 1) * 10 + 1;
    var endPage = Math.min(currentBlock * 10, totalPages);

    if (startPage > 1) {
        var prevBlockPage = document.createElement('li');
        prevBlockPage.innerHTML = '<a href="javascript:void(0);" onclick="go_page(' + (startPage - 1) + ')">&laquo; 이전 10개</a>';
        paginationContainer.appendChild(prevBlockPage);
    }

    if (pageNumber > 1) {
        var prevPage = document.createElement('li');
        prevPage.innerHTML = '<a href="javascript:void(0);" onclick="go_page(' + (pageNumber - 1) + ')">&laquo; 이전</a>';
        paginationContainer.appendChild(prevPage);
    }

    for (var i = startPage; i <= endPage; i++) {
        var pageItem = document.createElement('li');
        pageItem.innerHTML = '<a href="javascript:void(0);" onclick="go_page(' + i + ')" class="' + (i == pageNumber ? 'active' : '') + '">' + i + '</a>';
        paginationContainer.appendChild(pageItem);
    }

    if (pageNumber < totalPages) {
        var nextPage = document.createElement('li');
        nextPage.innerHTML = '<a href="javascript:void(0);" onclick="go_page(' + (pageNumber + 1) + ')">다음 &raquo;</a>';
        paginationContainer.appendChild(nextPage);
    }

    if (endPage < totalPages) {
        var nextBlockPage = document.createElement('li');
        nextBlockPage.innerHTML = '<a href="javascript:void(0);" onclick="go_page(' + (endPage + 1) + ')">다음 10개 &raquo;</a>';
        paginationContainer.appendChild(nextBlockPage);
    }
}