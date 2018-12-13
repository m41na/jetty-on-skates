from locust import HttpLocust, TaskSet, task
import random, string

class TodosBehavior(TaskSet):
    def on_start(self):
        """ on_start is called when a Locust start before any task is scheduled """
        self.index()

    def on_stop(self):
        """ on_stop is called when the TaskSet is stopping """
        self.clear()
    
    def index(self):
        self.client.get("/app/todos")
    
    def clear(self):
        self.client.delete("/app/todos/all")

    def randomword(self, length):
        letters = string.ascii_lowercase
        return ''.join(random.choice(letters) for i in range(length))

    @task(1)
    def createTask(self):
        task = self.randomword(10)
        ''' create task '''
        self.client.post("/app/todos", {"task": task})
        ''' update task '''
        self.client.put("/app/todos/done", {"task": task,"completed": "true"})
        ''' locust -f lib/locustfile.py --host=http://localhost:8080 '''    

class WebsiteUser(HttpLocust):
    task_set = TodosBehavior
    min_wait = 5000
    max_wait = 9000