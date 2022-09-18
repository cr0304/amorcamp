<!-- 캠핑장중복등록방지 -->
function chkCampFalse(){
    $.ajax({
        url : "/campFormChk",
        type : 'GET',
        contentType : "application/json",
        success : function(result, status){
            if (result === "NO"){
                alert('캠핑장이 이미 등록되었습니다.');
            } else {
                location.href='/campForm';
            }
        },
        error : function(jqXHR, status, error){
            if(jqXHR.status == '401'){
                alert('로그인 후 이용해주세요.');
                location.href='/members/login';
            }else{
                alert(jqXHR.responseText);
            }
        }
    });
}

<!-- 캠핑장 수정 제한 -->
function chkCampTrue(){
    $.ajax({
        url : "/campFormChk2",
        type : 'GET',
        contentType : "application/json",
        success : function(result, status){
            var campId = result;
            if (campId === "YES"){
                alert('캠핑장을 먼저 등록해주세요.');
            } else {
                location.href='/campFormUpdate/'+campId;
            }
        },
        error : function(jqXHR, status, error){
            if(jqXHR.status == '401'){
                alert('로그인 후 이용해주세요.');
                location.href='/members/login';
            }else{
                alert(jqXHR.responseText);
            }
        }
    });
}
<!-- 객실 등록 진입 제한-->
function chkCampTrue2(){
    $.ajax({
        url : "/campFormChk",
        type : 'GET',
        contentType : "application/json",
        success : function(result, status){
            if (result === "YES"){
                alert('캠핑장을 먼저 등록해주세요.');
            } else {
                location.href='/roomForm';
            }
        },
        error : function(jqXHR, status, error){
            if(jqXHR.status == '401'){
                alert('로그인 후 이용해주세요.');
                location.href='/members/login';
            }else{
                alert(jqXHR.responseText);
            }
        }
    });
}

<!-- 객실 관리 진입 제한 임시-->
function chkRoomTrue(){
    $.ajax({
        url : "/campFormChk",
        type : 'GET',
        contentType : "application/json",
        success : function(result, status){
            if (result === "YES"){
                alert('객실을 먼저 등록해주세요.');
            } else {
                location.href='/myRoomList';
            }
        },
        error : function(jqXHR, status, error){
            if(jqXHR.status == '401'){
                alert('로그인 후 이용해주세요.');
                location.href='/members/login';
            }else{
                alert(jqXHR.responseText);
            }
        }
    });
}


<!-- 예약 내역 진입 제한 -->
function reservationTrue(){
    $.ajax({
        url : "/campFormChk2",
        type : 'GET',
        contentType : "application/json",
        success : function(result, status){
            var campId = result;
            if (campId === "YES"){
                alert('캠핑장을 먼저 등록해주세요.');
            } else {
                location.href='/reservation';
            }
        },
        error : function(jqXHR, status, error){
            if(jqXHR.status == '401'){
                alert('로그인 후 이용해주세요.');
                location.href='/reservation';
            }else{
                alert(jqXHR.responseText);
            }
        }
    });
}