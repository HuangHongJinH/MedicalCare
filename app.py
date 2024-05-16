import threading

from flask import Flask
import socket
app = Flask(__name__)

# 这里默认的是get请求方式
@app.route('/hello')
def hello_world():
#这里面就是你想要返回给前端的值， 切记，这里只能返回字符串，如果是个json数据，你的通过json.dumps(你的json数据)
 return 'Hello World!'

@app.route('/sendmessage')
def auth():
    # 当按钮被点击时，发送授权请求
    client_socket.sendall("AUTH_REQUEST\n".encode())

# 用于监听Socket连接的函数
def listen_for_auth_response(client_socket):
    while True:
        auth_response = client_socket.recv(1024).decode()
        if auth_response == "AUTH_RESPONSE:GRANTED":
            print("授权成功")
            break

if __name__ == '__main__':
    # 创建一个Socket服务器
    server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server_socket.bind(('192.168.1.2', 5173))
    server_socket.listen()
    # 接受来自Android客户端的连接
    client_socket, address = server_socket.accept()
    # 在新线程中监听授权结果
    thread = threading.Thread(target=listen_for_auth_response, args=(client_socket,))
    thread.start()
    # debug = True ,表示的是，调试模式，每次修改代码后不用重新启动服务
    app.run(host='192.168.1.2', port=80, debug=True)
