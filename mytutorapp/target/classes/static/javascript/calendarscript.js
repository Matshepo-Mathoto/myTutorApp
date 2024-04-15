
// Convert Thymeleaf model attribute to JavaScript variable
var events = /*[[${events}]]*/[];

document.addEventListener('DOMContentLoaded', function () {
    var calendarEl = document.getElementById('calendar');

    var calendar = new FullCalendar.Calendar(calendarEl, {
        initialView: 'dayGridMonth',
        events: events, // Use the events variable for event data
        headerToolbar: {
            left: 'prev,next today',
            center: 'title',
            right: 'dayGridMonth,timeGridWeek,timeGridDay'
        },
        eventContent: function (info) {
            const eventLocation = info.event.extendedProps.location; // Get the location
            const eventStart = info.event.start;
            const eventEnd = info.event.end;
            const eventContent = document.createElement('div');
            eventContent.classList.add('event-content');

            const eventTitle = document.createElement('div');
            eventTitle.classList.add('event-title');
            eventTitle.textContent = info.event.title;
            eventContent.appendChild(eventTitle);

            if (eventLocation) {
                const eventLocationElem = document.createElement('div');
                eventLocationElem.classList.add('event-location');
                eventLocationElem.textContent = `Location: ${eventLocation}`;
                eventContent.appendChild(eventLocationElem);
            }

            if (eventStart && eventEnd) {
                const eventTimeElem = document.createElement('div');
                eventTimeElem.classList.add('event-time');
                eventTimeElem.textContent = `Time: ${formatEventTime(eventStart)} - ${formatEventTime(eventEnd)}`;
                eventContent.appendChild(eventTimeElem);
            }

            return { domNodes: [eventContent] };
        }
    });

    calendar.render();
});

function formatEventTime(date) {
    return date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
}
