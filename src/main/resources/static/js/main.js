

// roomSearch - detailInfo - carousel
let slideIndex = 1;

function showDetail(obj){
    console.log(".mySlides.length Test :: "+$(obj).parent().parent().parent().parent().next().children().children(".mySlides").length)

    let displayForShow = $(obj).parent().parent().parent().parent().next().css("display");
    let mySlides =  $(obj).parent().parent().parent().parent().next().children().children(".mySlides");

    console.log("mySlides.length :: "+mySlides.length)
    if(displayForShow === "none") {
        $(obj).parent().parent().parent().parent().next().css("display", "block");
        slideIndex =1 ;
        if (slideIndex === 1) {
            for(let i=0; i<mySlides.length; i++){
                mySlides[i].style.display = "none";
            }
           mySlides[0].style.display = "block";
        }
    }else {
        $(obj).parent().parent().parent().parent().next().css("display", "none");
    }
}

function plusSlides(obj,n) {
    arrowShowSlides(obj,slideIndex += n);
}

function arrowShowSlides(obj,n) {
    console.log(".mySlides.length : "+$(obj).parent().children(".mySlides").length);
    let i;
    let slides = $(obj).parent().children(".mySlides");
    if (n > slides.length) {slideIndex = 1}
    if (n < 1) {
        slideIndex = slides.length
    }
    for (i = 0; i < slides.length; i++) {
        slides[i].style.display = "none";
    }
    slides[slideIndex-1].style.display = "block";
}

// campSearch - 전체보기 선택시 모든 체크 박스 선택
function checkAll(){

    if($("#cateCheckAll").prop("checked")){
        $("input[class=cateChkBox]").prop("checked",true);
    }
    else{
        $("input[class=cateChkBox]").prop("checked",false);
    }

}

/* button click -> scroll down */
function scrollDown(){
    window.scrollTo(0,document.body.scrollHeight);
}

/* textArea autoSize */
function resize(obj) {
    obj.style.height = "1px";
    obj.style.height = (12+obj.scrollHeight)+"px";
}






