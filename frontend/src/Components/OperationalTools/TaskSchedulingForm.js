import React, { useState, useEffect } from 'react';
import './TaskSchedulingForm.css';
import { FaBars, FaChevronLeft, FaChevronRight, FaPlus, FaTimes } from 'react-icons/fa';
import eventService from '../../services/eventService';

const TaskSchedulingForm = ({ setTotalCalendarEvents }) => {
    const [currentDate, setCurrentDate] = useState(new Date());
    const [selectedEvent, setSelectedEvent] = useState(null);
    const [showCreateEventForm, setShowCreateEventForm] = useState(false); // New state for showing the create event form
    const [selectedDate, setSelectedDate] = useState(null); // New state for selected date

    // Sample event data. In a real application, this would come from an API.
    const [events, setEvents] = useState([]); // Initialize as empty, data will be fetched

    // Fetch events from backend on component mount
    useEffect(() => {
        const fetchEvents = async () => {
            try {
                const fetchedEvents = await eventService.getAllEvents();
                console.log('Fetched Events:', fetchedEvents); // Log fetched events
                // Format fetched events to match the expected structure if needed
                const formattedEvents = fetchedEvents.map(event => ({
                    date: event.date,
                    title: event.title,
                    description: event.description || '',
                    highlighted: event.highlighted || false // Ensure highlighted exists
                }));
                setEvents(formattedEvents);
                if (setTotalCalendarEvents) {
                    setTotalCalendarEvents(formattedEvents.length);
                }
            } catch (error) {
                console.error('Error fetching events:', error);
                // alert('Failed to fetch events.'); // Commented out to prevent excessive alerts during debugging
            }
        };
        fetchEvents();
    }, [setTotalCalendarEvents]); // Add setTotalCalendarEvents to dependency array

    const getDaysInMonth = (year, month) => {
        return new Date(year, month + 1, 0).getDate();
    };

    const getFirstDayOfMonth = (year, month) => {
        return new Date(year, month, 1).getDay(); // 0 for Sunday, 1 for Monday, etc.
    };

    const renderDays = () => {
        const year = currentDate.getFullYear();
        const month = currentDate.getMonth();
        const daysInMonth = getDaysInMonth(year, month);
        const firstDay = getFirstDayOfMonth(year, month);

        const days = [];
        // Fill in leading empty days
        for (let i = 0; i < firstDay; i++) {
            days.push(<span key={`empty-pre-${i}`} className="inactive"></span>);
        }

        // Fill in days of the month
        for (let i = 1; i <= daysInMonth; i++) {
            const dateString = `${year}-${(month + 1).toString().padStart(2, '0')}-${i.toString().padStart(2, '0')}`;
            const eventForDay = events.find(event => event.date === dateString);
            const isHighlighted = eventForDay ? eventForDay.highlighted : false; // Check highlighted property

            console.log(`Date: ${dateString}, Event Present: ${!!eventForDay}, Is Highlighted: ${isHighlighted}`); // Log highlighting status

            const isToday = i === new Date().getDate() &&
                            month === new Date().getMonth() &&
                            year === new Date().getFullYear();

            const dayDate = new Date(year, month, i);
            const isPastDate = dayDate < new Date().setHours(0, 0, 0, 0); // Check if the date is in the past

            days.push(
                <span 
                    key={i} 
                    className={`${isToday ? "today" : ""} ${eventForDay ? "has-event" : ""} ${isHighlighted ? "highlighted-event" : ""} ${isPastDate ? "past-date" : ""} ${selectedDate === dateString ? "is-selected" : ""}`}
                    onClick={() => {
                        if (!isPastDate) {
                            setSelectedEvent(eventForDay);
                            setSelectedDate(dateString); // Set the selected date
                        }
                    }}
                >
                    {i}
                </span>
            );
        }

        // Fill in trailing empty days
        const totalCells = days.length;
        const remainingCells = 42 - totalCells; // 6 rows * 7 days
        for (let i = 0; i < remainingCells; i++) {
            days.push(<span key={`empty-post-${i}`} className="inactive"></span>);
        }
        return days;
    };

    const goToPreviousMonth = () => {
        setCurrentDate(prevDate => {
            const newDate = new Date(prevDate);
            newDate.setMonth(prevDate.getMonth() - 1);
            return newDate;
    });
  };

    const goToNextMonth = () => {
        setCurrentDate(prevDate => {
            const newDate = new Date(prevDate);
            newDate.setMonth(prevDate.getMonth() + 1);
            return newDate;
    });
  };

    const months = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "July", "Aug", "Sep", "Oct", "Nov", "Dec"];
    const weekdays = ["SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"];

    const handleCreateEventClick = () => {
        setShowCreateEventForm(true);
    };

    const handleBackToCalendar = () => {
        setShowCreateEventForm(false);
    };

    // Simple form for creating an event
    const CreateEventForm = () => {
        const [eventTitle, setEventTitle] = useState('');
        const [eventDate, setEventDate] = useState('');
        const [eventDescription, setEventDescription] = useState('');

        const handleSubmit = async (e) => {
            e.preventDefault();
            try {
                // Create event object to send to backend
                const newEvent = {
                    title: eventTitle,
                    date: eventDate, // Date format YYYY-MM-DD matches backend
                    description: eventDescription,
                    highlighted: true // Set highlighted to true for new events
                };

                // Send event data to backend via eventService
                const createdEvent = await eventService.createEvent(newEvent);
                console.log('Event created successfully:', createdEvent);

                // Update local state with the new event and navigate back
                setEvents(prevEvents => [...prevEvents, createdEvent]);
                setShowCreateEventForm(false);
                setSelectedDate(eventDate); // Set the selected date to the newly created event's date
                setEventTitle('');
                setEventDate('');
                setEventDescription('');

            } catch (error) {
                console.error('Error creating event:', error);
                // Handle error, e.g., show a toast message
                alert('Failed to create event. Please try again.');
            }
        };

        return (
            <div className="create-event-form-container">
                <h2 style={{ textAlign: 'center', marginBottom: '20px', color: '#333' }}>Create New Event</h2>
                <form className="create-event-form" onSubmit={handleSubmit}>
        <div className="form-row">
                        <label htmlFor="eventTitle">Event Title:</label>
          <input
            type="text"
                            id="eventTitle"
                            value={eventTitle}
                            onChange={(e) => setEventTitle(e.target.value)}
            required
          />
        </div>
        <div className="form-row">
                        <label htmlFor="eventDate">Date:</label>
          <input
            type="date"
                            id="eventDate"
                            value={eventDate}
                            onChange={(e) => setEventDate(e.target.value)}
            required
          />
        </div>
        <div className="form-row">
                        <label htmlFor="eventDescription">Description:</label>
                        <textarea
                            id="eventDescription"
                            value={eventDescription}
                            onChange={(e) => setEventDescription(e.target.value)}
          />
        </div>
        <div className="button-row">
                        <button type="submit">Add Event</button>
                        <button type="button" onClick={handleBackToCalendar}>Back to Calendar</button>
        </div>
      </form>
            </div>
        );
    };

    return (
        <div className="calendar-wrapper">
            {!showCreateEventForm ? (
                <>
                    <div className="calendar-left">
                        <div className="calendar-left-header">
                            <FaBars className="menu-icon" />
                        </div>
                        <div className="calendar-date">
                            <div className="day-number">{currentDate.getDate()}</div>
                            <div className="day-name">{currentDate.toLocaleString('en-US', { weekday: 'long' }).toUpperCase()}</div>
                        </div>
                        <div className="calendar-events">
                            <h3>Current Events</h3>
                            <ul>
                                {events.filter(event => {
                                    const eventDate = new Date(event.date);
                                    return eventDate.getFullYear() === currentDate.getFullYear() &&
                                           eventDate.getMonth() === currentDate.getMonth() &&
                                           eventDate.getDate() === currentDate.getDate();
                                }).map((event, index) => (
                                    <li key={index}>{event.title}</li>
                                ))}
                            </ul>
                            <div className="total-events">
                                Total number of Events: {events.length}
                            </div>
                        </div>
                        <div className="create-event" onClick={handleCreateEventClick}>
                            <span>Schedule</span>
                            <FaPlus className="plus-icon" />
                        </div>
                    </div>
                    <div className="calendar-right">
                        <div className="calendar-right-header">
                            <div className="year-navigation">
                                <FaChevronLeft className="arrow-icon" onClick={goToPreviousMonth} />
                                <span>{currentDate.getFullYear()}</span>
                                <FaChevronRight className="arrow-icon" onClick={goToNextMonth} />
                            </div>
                        </div>
                        <div className="calendar-months">
                            {months.map((month, index) => (
                                <span key={month} className={index === currentDate.getMonth() ? "active" : ""}>
                                    {month}
                                </span>
                            ))}
                        </div>
                        <div className="calendar-weekdays">
                            {weekdays.map(day => <span key={day}>{day}</span>)}
                        </div>
                        <div className="calendar-days">
                            {renderDays()}
                        </div>
                    </div>
                </>
            ) : (
                <CreateEventForm />
            )}

            {selectedEvent && (
                <div className="event-card-overlay" onClick={() => setSelectedEvent(null)}>
                    <div className="event-card" onClick={(e) => e.stopPropagation()}>
                        <div className="event-card-header">
                            <h3>Event Details</h3>
                            <FaTimes className="close-icon" onClick={() => setSelectedEvent(null)} />
                        </div>
                        {selectedEvent.title ? (
                            <>
                                <p><strong>Date:</strong> {selectedEvent.date}</p>
                                <p><strong>Title:</strong> {selectedEvent.title}</p>
                                <p><strong>Description:</strong> {selectedEvent.description}</p>
                            </>
                        ) : (
                            <p>No event!</p>
                        )}
                    </div>
                </div>
            )}
    </div>
  );
};

export default TaskSchedulingForm; 