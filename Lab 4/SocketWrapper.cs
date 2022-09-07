using System;
using System.Collections.Generic;
using System.Net;
using System.Net.Sockets;
using System.Text;

namespace Lab4
{
    class SocketWrapper
    {
        public Socket socket { get; set; }
        public string host { get; set; }
        public IPEndPoint endPoint { get; set; }

        public byte[] buffer = new byte[1024*10];

        public SocketWrapper(Socket socket, string host, IPEndPoint endPoint)
        {
            this.socket = socket;
            this.host = host;
            this.endPoint = endPoint;
        }
    }
}
