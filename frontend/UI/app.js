import { main as processWithAI } from "./openAi.js";

// authentication check on page-load
(function checkAuth() {
    const userData = localStorage.getItem('userData');
    if (!userData) {
        window.location.replace('login.html');
        return;
    }
})();


document.addEventListener('DOMContentLoaded', () => {
    const userData = localStorage.getItem("userData")
    if (!userData) {
        window.location.replace("login.html")
        return;
    }
    getTasksFromDB(userData.id, 'all')
    const voiceBtn = document.querySelector('.voice-btn')
    if (voiceBtn) {
        voiceBtn.addEventListener('click', startListening)
    }
    clearTaskOutput();
    updateTaskList();

    const logoutBtn = document.getElementById('logoutBtn');
    if (logoutBtn) {
        logoutBtn.addEventListener('click', () => {
            localStorage.removeItem('userData');

            // Redirect to login page
            window.location.href = 'login.html';
        });
    }

    window.filterTasks = filterTasks;
})

function getUserData() {
    const userData = localStorage.getItem('userData');
    return userData ? JSON.parse(userData) : null;
}

function clearTaskOutput() {
    const taskInfo = document.querySelector('.task-info');
    if (taskInfo) {
        document.getElementById('operation').textContent = ''
        document.getElementById('task').textContent = ''
        document.getElementById('urgency').textContent = ''
        document.getElementById('datetime').textContent = ''
    }
    const confirmationArea = document.getElementById('confirmation-area');
    if (confirmationArea) {
        confirmationArea.innerHTML = '';
    }
}


function getDataFromLocalStorage() {
    const userData = localStorage.getItem("userData");
    return userData;
}
function startListening() {
    if ('webkitSpeechRecognition' in window) {
        const recognition = new webkitSpeechRecognition();
        recognition.continuous = false;
        recognition.interimResults = false;
        recognition.lang = 'en-US';

        recognition.onstart = function () {
            console.log('Listening started...')
            clearTaskOutput();
        }

        recognition.onresult = function (event) {
            const transcript = event.results[0][0].transcript
            processVoiceCommand(transcript)
        }

        recognition.onerror = function (event) {
            console.log('Speech recognition error', event.error);
        }
        recognition.start();
    } else {
        alert('Speech recognition not supported in this browser');
    }
    // processVoiceCommand("Add Clean car on 25th march 2025 highly urgent")
}

function getUrgencyColor(urgency) {
    if (urgency == null) {
        return
    }
    switch (urgency.toLowerCase()) {
        case 'high':
            return '#FF0000';
        case 'medium':
            return '#FFA500';
        case 'low':
            return '#008000';
        default:
            return '#808080';
    }
}


function updateTaskList(filterDays = 'all') {
    const todoList = document.getElementById("todo-list");
    todoList.innerHTML = ''

    // const taskStore = new Map();
    getTasksFromDB(null, filterDays).then(tasks => {
        tasks.forEach((taskData, index) => {
            const listItem = document.createElement("div");
            listItem.classList.add('todo-item');

            const statusIndicator = document.createElement("div")
            statusIndicator.classList.add('status-indicator')
            statusIndicator.style.backgroundColor = getUrgencyColor(taskData.urgency);

            const taskContent = document.createElement("div");
            taskContent.classList.add('task-content');

            const taskTitle = document.createElement("div");
            taskTitle.classList.add('task-title');
            taskTitle.innerHTML = `
                <span class="operation-badge" style="background-color : ${getUrgencyColor(taskData.urgency)}">${taskData.operation}</span>
                <span class="task-name">${taskData.task} </span>
            `
            const taskDetails = document.createElement("div");
            taskDetails.classList.add('task-details-line');
            taskDetails.innerHTML = `
                <span class="urgency-badge" style="background-color : ${getUrgencyColor(taskData.urgency)}"> ${taskData.urgency}</span>
                ${taskData.datetime ? `<span class="datetime"> ${taskData.datetime}</span>` : ''}
            `;

            taskContent.appendChild(taskTitle);
            taskContent.appendChild(taskDetails);

            const completeButton = document.createElement("button");
            completeButton.classList.add('complete-btn');
            completeButton.innerHTML = '';
            completeButton.textContent = 'Completed';
            completeButton.onclick = async () => {
                try {
                    const response = await fetch(`http://localhost:8080/api/tasks/${taskData.id}`, {
                        method: 'DELETE',
                        headers: {
                            "Content-Type": "application/json",
                            "Accept": "application/json"
                        }
                    });

                    if (!response.ok) {
                        throw new Error(`HTTP error! status: ${response.status}`);
                    }

                    // makingapi call to backend to delete it
                    updateTaskList(filterDays);
                } catch (error) {
                    console.error("Error deleting task:", error);
                    alert("Failed to delete task. Please try again.");
                }
            };

            listItem.appendChild(statusIndicator)
            listItem.appendChild(taskContent)
            listItem.appendChild(completeButton)

            todoList.appendChild(listItem)
        });

        // Show message if no tasks found
       if (tasks.length === 0) {
            const noTasksMessage = document.createElement("div");
            noTasksMessage.classList.add('no-tasks-message');
            noTasksMessage.textContent = filterDays === 'all'
                ? 'No tasks found'
                : `No tasks found for the next ${filterDays} days`;
            todoList.appendChild(noTasksMessage);
        }
    }).catch(error => {
        console.log("Error updating task list", error);
    });
}


async function processVoiceCommand(command) {
    try {
        console.log(command)
        const aiResp = await processWithAI(command)
        const aiResponse = JSON.parse(aiResp.choices[0].message.content);
        console.log(aiResponse)

        const userData = getUserData();
        if (!userData) {
            throw new Error('User not authenticated');
        }
        const requestBody = {
            operation: aiResponse.operation,
            task: aiResponse.task,
            urgency: aiResponse.urgency,
            datetime: aiResponse.datetime,
            userId: userData.id
        }
        var operation, task, urgency, datetime;
        operation = document.getElementById("operation")
        task = document.getElementById("task")
        urgency = document.getElementById("urgency")
        datetime = document.getElementById("datetime")
        if (!(aiResponse == null || aiResponse.operation == null)) {
            operation.textContent = aiResponse.operation
        }
        else {
            console.log("Got null 1")
        }
        if (!(aiResponse == null || aiResponse.task == null)) {
            task.textContent = aiResponse.task
        }
        else {
            console.log("Got null 2")
        }
        if (!(aiResponse == null || aiResponse.urgency == null)) {
            urgency.textContent = aiResponse.urgency
        }
        else {
            console.log("Got null 3")
        }
        if (!(aiResponse == null || aiResponse.datetime == null)) {
            datetime.textContent = aiResponse.datetime
        }
        else {
            console.log("Got null 4")
        }

        const confirmationArea = document.getElementById("confirmation-area")
        confirmationArea.innerHTML = `
            <div class="confirmation-button">
                <p> Is it correct </p>
                <button onclick="window.confirmTask(true)" class="confirm-btn"> YES </button>
                <button onclick="window.confirmTask(false)" class="confirm-btn"> NO </button>
            </div>
        `;

        window.confirmTask = async (isCorrect) => {
            if (isCorrect) {
                console.log("Correct...")
                confirmationArea.innerHTML = ''
                operation.innerHTML = ''
                task.innerHTML = ''
                urgency.innerHTML = ''
                datetime.innerHTML = ''
                const response = await fetch("http://localhost:8080/api/tasks", {
                    method: "POST",
                    headers: {
                        "Content-type": "application/json",
                        "Accept": "application/json"
                    },
                    body: JSON.stringify(requestBody)
                })
                if (!response.ok) {
                    console.log("Request unsuccessfull")
                    throw new Error(`Http error with status code: ${response.status}`);
                }
                console.log("Call success populating values")
                const responseData = await response.json()
                updateTaskList('all')
                return responseData;
            }
            else {
                console.log("User told not correct")
                confirmationArea.innerHTML = '';
                operation.innerHTML = '';
                task.innerHTML = '';
                urgency.innerHTML = '';
                datetime.innerHTML = '';
                startListening();
            }
        }

    } catch (error) {
        console.log("Error processing voice command", error)
        startListening()
        return null
    }
}

async function getTasksFromDB(userId, filterDays = 'all') {
    try {
        const userData = getUserData();
        if (!userData) {
            throw new Error('User not authenticated');
        }

        // New code: Use the filter endpoint
        const url = new URL('http://localhost:8080/api/tasks/filter');
        url.searchParams.append('userId', userData.id);
        if (filterDays !== 'all') {
            url.searchParams.append('days', filterDays);
        }

        const response = await fetch(url, {
            method: 'GET',
            headers: {
                "Content-type": "application/json",
                "Accept": "application/json"
            }
        });

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const data = await response.json();
        return Array.isArray(data) ? data : []; // Always return an array
    } catch (error) {
        console.log("Error fetching tasks", error)
        return []
    }
}

function filterTasks() {
    const filterValue = document.getElementById('dateFilter').value;
    const userData = getUserData();

    if (!userData) {
        console.error('User not authenticated');
        return;
    }
    const url = new URL('http://localhost:8080/api/tasks/filter');
    url.searchParams.append('userId', userData.id);
    if (filterValue !== 'all') {
        url.searchParams.append('days', filterValue);
    }
    fetch(url, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        }
    }).then(response => {
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        return response.json();
    }).then(tasks => {
        updateTaskListWithFilteredTasks(tasks);
    }).catch(error => {
        console.log('Error fetching filtered tasks', error);
        alert('Failed to fetch tasks, plesae try again');
    })
}

function updateTaskListWithFilteredTasks(tasks) {
    const todoList = document.getElementById("todo-list");
    todoList.innerHTML = '';
    if (tasks.length === 0) {
        const noTaskMessage = document.createElement("div");
        noTaskMessage.classList.add('no-tasks-message');
        const filterValue = document.getElementById('dataFilter').value;
        noTaskMessage.textContent = filterValue === 'all' 
        ? 'No Tasks found'
         : `No Tasks found for the next ${filterValue} days`;
        todoList.appendChild(noTaskMessage);
        return;
    }

    tasks.forEach(taskData => {
        const listItem = document.createElement("div");
        listItem.classList.add('todo-item');

        const statusIndicator = document.createElement("div");
        statusIndicator.classList.add('status-indicator');
        statusIndicator.style.backgroundColor = getUrgencyColor(taskData.urgency);

        const taskContent = document.createElement("div");
        taskContent.classList.add('task-content');

        const taskTitle = document.createElement('div');
        taskTitle.classList.add('task-title');
        taskTitle.innerHTML = `
        <span class="operation-badge" style="background-color: ${getUrgencyColor(taskData.urgency)}">${taskData.operation}</span>
        <span class="task-name">${taskData.task}</span>
        `;

        const taskDetails = document.createElement("div");
        taskDetails.classList.add('task-details-line');
        taskDetails.innerHTML = `
        <span class="urgency-badge" style="background-color: ${getUrgencyColor(taskData.urgency)}">${taskData.urgency} </span>
        ${taskData.dateTime ? `<span class="dateTime"> ${taskData.dateTime} </span>` : ''}
        `;

        taskContent.appendChild(taskTitle);
        taskContent.appendChild(taskDetails);

        const completeButton = document.createElement("button");
        completeButton.classList.add('complete-btn');
        completeButton.innerHTML = '';
        completeButton.textContent = 'Completed';
        completeButton.onclick = async () => {
            try {
                const response = await fetch(`http://localhost:8080/api/tasks/${taskData.id}`, {
                    method: 'DELETE',
                    headers: {
                        "Content-Type": "application/json",
                        "Accept": "application/json"
                    }
                });
                if (!response.ok) {
                    throw new Error(`Http error status: ${response.status}`);
                }
                filterTasks();
            }
            catch (error) {
                console.log("Error deleting task", error);
                alert("Failed to delete task, Please try again")
            }
        };

        listItem.appendChild(statusIndicator)
        listItem.appendChild(taskContent)
        listItem.appendChild(completeButton)

        todoList.appendChild(listItem);
    });
}

