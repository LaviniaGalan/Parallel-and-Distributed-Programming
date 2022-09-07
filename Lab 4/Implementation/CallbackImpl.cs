using System;
using System.Collections.Generic;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading;

namespace Lab4.Implementation
{
    class CallbackImpl
    {
        public CallbackImpl(List<string> hosts)
        {
            foreach (string h in hosts)
            {
                this.Start(h);
                Thread.Sleep(500);
            }
            
        }

        private void Start(string host)
        {
            var ipAdress = Dns.GetHostEntry(host.Split('/')[0]).AddressList[0];
            var endPoint = new IPEndPoint(ipAdress, 80);

            var clientSocket = new Socket(ipAdress.AddressFamily,
                SocketType.Stream, ProtocolType.Tcp);

            SocketWrapper socketWrapper = new SocketWrapper(clientSocket, host, endPoint);

            clientSocket.BeginConnect(endPoint, this.ConnectCallback, socketWrapper);
        }

        private void ConnectCallback(IAsyncResult asyncResult)
        {
            var socketWrapper = (SocketWrapper)asyncResult.AsyncState;
            socketWrapper.socket.EndConnect(asyncResult);

            var bytes = Encoding.ASCII.GetBytes(Utils.GetRequest(socketWrapper.host));

            socketWrapper.socket.BeginSend(bytes, 0, bytes.Length, 0, SendCallback, socketWrapper);
        }

        private void SendCallback(IAsyncResult asyncResult)
        {
            var socketWrapper = (SocketWrapper)asyncResult.AsyncState;
            socketWrapper.socket.EndSend(asyncResult);

            var buffer = socketWrapper.buffer;

            socketWrapper.socket.BeginReceive(buffer, 0, buffer.Length, 0, this.ReceiveCallback, socketWrapper);

        }

        private void ReceiveCallback(IAsyncResult asyncResult)
        {
            var socketWrapper = (SocketWrapper)asyncResult.AsyncState;
            socketWrapper.socket.EndReceive(asyncResult);

            socketWrapper.socket.Shutdown(SocketShutdown.Both);
            socketWrapper.socket.Close();

            Utils.ParseBuffer(socketWrapper.buffer, socketWrapper.host);
        }
    }
}
