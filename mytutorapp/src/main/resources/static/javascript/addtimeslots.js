let arrayList = [];
const activitiesList = document.getElementById("searchCourse");
activitiesList.addEventListener("input", function () {
    const selectedCourseInput = document.getElementById("selectedActivity");

    if (this.value === "") {
        selectedCourseInput.value = "";
        return;
    }

    const datalist = document.getElementById("activitiesList");
    const options = datalist.getElementsByTagName("option");

    for (const option of options) {
        if (option.text == this.text) {
            selectedCourseInput.value = this.value;
            break;
        }
    }
})

function add() {
    const startInputElement = document.querySelector(".start-time-class");
    const start = startInputElement.value;
    const endInputElement = document.querySelector(".end-time-class");
    const end = endInputElement.value;
    const slotInputElement = document.querySelector(".slot-no-class");
    const slot = slotInputElement.value;
    arrayList.push({ start, end, slot });
    console.log(arrayList);
    display();
}

function display() {
    var contentContainer = document.getElementById("display1");
    contentContainer.innerHTML = '';
    let todoHtml = '';
    for (let i = 0; i < arrayList.length; i++) {
        const todoItem = arrayList[i];
        const { start, end, slot } = todoItem;
        todoHtml += `<div> ${i + 1}. ${start}  ${end}  ${slot} 
                <button type="button" class = "delete-button" onclick = "arrayList.splice(${i}, 1); display();" >delete</button> </div>`;
    }
    let array = [];
    for (let i = 0; i < arrayList.length; i++) {

        const todoItem = arrayList[i];
        const { start, end, slot } = todoItem;
        const getTime = start + "-" + end + "-" + slot;
        array.push(getTime);
    }

    const hiddenArr = document.getElementById("inputArray");
    hiddenArr.value = array;

    contentContainer.innerHTML = todoHtml;
}