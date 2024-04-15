/* Toggle between showing and hiding the navigation menu links when the user clicks on the hamburger menu / bar icon */

/*The updated topscript*/ 
function myFunction() {
    var x = document.getElementById("myLinks");
    if (x.style.display === "block") {
        x.style.display = "none";
    } else {
        x.style.display = "block";
    }
}
function toggleShow() {
    var x = document.getElementById("divFeedback");
    var check = document.getElementById("isEnd");
    if (check.checked) {
        x.style.display = "block";
    } else {
        x.style.display = "none";
    }
}
function submitLogout() {
    document.getElementById("logoutForm").submit();
}

function mytopnavfunction()
{
    var x = document.getElementById("topnav");
    if(x.className == "topnav")
    {
        x.className+=" responsive";
    }
    else{
        x.className="topnav";
    }
}
/*method to toggle the menu*/
$(function(){
    $(".toggle").on("click",function(){
        if($(".menu").hasClass("active")){
            $(".menu").removeClass("active");
            $(this).find("a").html("<ion-icon name='menu-outline'></ion-icon>");
        }
        else{
            $(".menu").addClass("active");
            $(this).find("a").html("<ion-icon name='close-outline'></ion-icon>")
        }
        });
});

function show_hide(){
    var click=document.getElementById("list-items");
    if(click.style.display==="none")
    {
        click.style.display="block";
    }
    else{
        click.style.display="none";
    }
}
/*manageCorses*/
function showNewSiteTemplate() {
    document.getElementById("myCourseForm").style.display = "inline-block";
    document.getElementById("courseUpdate").style.display = "none";

}

function submitSelectedValue() {
    var selectedValue = document.getElementById("currentCourseInput").value;
    document.getElementById("selectedSite").value = selectedValue;
    document.getElementById("selectCoursesForm").submit();
}


function confirmDelete() {
    var deleteForm = document.getElementById("delete_form");

    var txt;
    if (confirm("Are you sure you want to this Course and its related details, activities, activity sessions and signups?")) {
        deleteForm.submit();
    }
}

