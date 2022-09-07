using System;
using System.Collections.Generic;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading.Tasks;

namespace Lab4.Implementation
{
    class TaskImpl
    {
        public TaskImpl(List<string> hosts)
        {
            List<Task> tasks = new List<Task>();
            foreach(string h in hosts)
            {
                tasks.Add(Task.Factory.StartNew(this.Start, h));
            }
            Task.WaitAll(tasks.ToArray());
        }

        private void Start(object h)
        {
            string host = (string)h;

            var ipAdress = Dns.GetHostEntry(host.Split('/')[0]).AddressList[0];
            var endPoint = new IPEndPoint(ipAdress, 80);

            var clientSocket = new Socket(ipAdress.AddressFamily,
                SocketType.Stream, ProtocolType.Tcp);

            this.Connect(clientSocket, endPoint).Wait();
            this.Send(clientSocket, host).Wait();

            byte[] buffer = new byte[1024*10];
            this.Receive(clientSocket, buffer).Wait();

            clientSocket.Shutdown(SocketShutdown.Both);
            clientSocket.Close();

            Utils.ParseBuffer(buffer, host);
        }

        private Task Connect(Socket clientSocket, IPEndPoint endPoint)
        {
            var promise = new TaskCompletionSource<int>();
            clientSocket.BeginConnect(endPoint,
                (IAsyncResult asyncResult) => promise.SetResult(this.EndConnect(clientSocket, asyncResult)), null);

            return promise.Task;
        }

        private int EndConnect(Socket clientSocket, IAsyncResult asyncResult)
        {
            clientSocket.EndConnect(asyncResult);
            return 0;
        }

        private Task Send(Socket clientSocket, string host)
        {
            var promise = new TaskCompletionSource<int>();
            var bytes = Encoding.ASCII.GetBytes(Utils.GetRequest(host));

            clientSocket.BeginSend(bytes, 0, bytes.Length, 0,
                (IAsyncResult asyncResult) => promise.SetResult(clientSocket.EndSend(asyncResult)), null);
            return promise.Task;
        }

        private Task Receive(Socket clientSocket, byte[] buffer)
        {
            var promise = new TaskCompletionSource<int>();

            clientSocket.BeginReceive(buffer, 0, buffer.Length, 0,
                 (IAsyncResult asyncResult) => promise.SetResult(clientSocket.EndReceive(asyncResult)), null);

            return promise.Task;
        }
    }
}
