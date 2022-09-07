using Lab4.Implementation;
using System;
using System.Collections.Generic;

namespace Lab4
{
    class Program
    {
        static void Main(string[] args)
        {
            List<string> hosts = new List<string>();
            hosts.Add("www.cs.ubbcluj.ro/~rlupsa/edu/pdp/");
            hosts.Add("www.cs.ubbcluj.ro/~forest/");
            hosts.Add("www.cs.ubbcluj.ro/~rlupsa/edu/pdp/progs/futures-demo2-cascade1.cs");
            hosts.Add("www.columbia.edu/~fdc/sample.html");

            TaskImpl taskImpl = new TaskImpl(hosts);
            // AsyncTaskImpl asyncTaskImpl = new AsyncTaskImpl(hosts);
            // CallbackImpl callbackImpl = new CallbackImpl(hosts);
        }
    }
}
