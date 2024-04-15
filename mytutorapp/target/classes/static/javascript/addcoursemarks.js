let arrayList = [];
function add() {
    const courseElement = document.querySelector(".course-class");
    const course = courseElement.value;
    const markElement = document.querySelector(".mark-class");
    const mark = markElement.value;
    arrayList.push({ course, mark });
    console.log(arrayList);
    display();

}
function display() {
    var contentContainer = document.getElementById("display1");
    contentContainer.innerHTML = '';
    let todoHtml = '';
    for (let i = 0; i < arrayList.length; i++) {
        const todoItem = arrayList[i];
        const { course, mark } = todoItem;
        todoHtml += `<div> ${i + 1}. ${course}  ${mark}
            <button type="button" class = "delete-button" onclick = "arrayList.splice(${i}, 1); display();" >Delete</button> </div>`;
    }
    let array = [];
    for (let i = 0; i < arrayList.length; i++) {

        const todoItem = arrayList[i];
        const { course, mark } = todoItem;
        const getCourse = course + "##" + mark;
        array.push(getCourse);
    }
    const arraySent = document.getElementById('courses');
    arraySent.value = array;
    contentContainer.innerHTML = todoHtml;
}

