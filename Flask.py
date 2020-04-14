##Group A
##Dustin, Joe, Tyler



from flask import Flask, request, jsonify
import mysql.connector

mydb = mysql.connector.connect(
  host="96.30.194.99",
  user="myhu",
  password="password",
  database="CISC349GroupA",
  auth_plugin='mysql_native_password'
)

app = Flask(__name__)

def shutdown_server():
    func = request.environ.get('werkzeug.server.shutdown')
    if func is None:
        raise RuntimeError('Not running with the Werkzeug Server')
    func()

@app.route('/shutdown', methods=['GET'])
def shutdown():
    shutdown_server()
    return 'Server shutting down...'

@app.route('/')
def index():
    return '<p style="color:red">Hello, Flask!</p>'

@app.route('/testquery', methods=['GET'])
def testquery():
    query = "select * from User"
    cursor = mydb.cursor()
    cursor.execute(query)
    results = cursor.fetchall()
    results = jsonify(results)
    return results


@app.route('/signup', methods=['GET'])
##example http://myserver:myport/signup?username="myuser"&password="mypwd"
def signup():
    username = request.args.get('username', None)
    password = request.args.get('password', None)
    query = "insert into User (Username, Password) Values ('%s', '%s')" % (username, password)
    cursor = mydb.cursor()
    cursor.execute(query)
    mydb.commit()
    return (query)

@app.route('/login', methods=['GET'])
##example http://myserver:myport/login?username="myuser"&password="mypwd"
def login():
    username = request.args.get('username', None)
    password = request.args.get('password', None)
    query = "select * from User where Username = '%s' and Password = '%s'" % (username, password)
    cursor = mydb.cursor()
    cursor.execute(query)
    results = cursor.fetchall()
    results = jsonify(results)
    if results.calculate_content_length()!= 3:
        print(results.get_data())
        return (results)
    else:
        results = jsonify({"invalid":"invalid"})
        print (results)
        return ({})

@app.route('/newtask', methods=['GET'])
def newtask():
    taskname = request.args.get('taskname', None)
    User_ID = request.args.get('User_ID', None)
    ###REMINDER Send USER_ID FROM ANDROID APP
    query = "insert into Task (Task_Name, User_ID) Values ('%s', '%d')" % (taskname, int(User_ID))
    cursor = mydb.cursor()
    cursor.execute(query)
    mydb.commit()
    return (query)

@app.route('/deletetask', methods=['GET'])
def deletetask():
    taskname = request.args.get('taskname', None)
    User_ID = request.args.get('User_ID', None)
    query = "Delete from Task where Task_Name = '%s' and User_ID = '%d'" % ((taskname),int(User_ID))
    cursor = mydb.cursor()
    cursor.execute(query)
    mydb.commit()
    return (query)
@app.route('/gettasks', methods=['GET'])
def gettasks():
    User_ID = request.args.get('User_ID', None)
    query = "Select Task_Name from Task where User_ID = '%d'" % (int(User_ID))
    cursor = mydb.cursor()
    cursor.execute(query)
    results = cursor.fetchall()
    results = jsonify(results)
    return results



if __name__ == '__main__':
    app.run(host="10.0.0.8", port=5000)
