using System;
using System.Collections.Generic;
using System.IO;
using System.IO.Compression;
using System.Linq;
using System.Web;
using System.Web.Http;
using System.Web.Mvc;
using System.Web.Routing;
using WebApp.App_Start;

namespace WebApp
{
    public class MvcApplication : System.Web.HttpApplication
    {
        protected void Application_Start()
        {
            AreaRegistration.RegisterAllAreas();

            GlobalConfiguration.Configure(WebApiConfig.Register);

            RouteConfig.RegisterRoutes(RouteTable.Routes);
        }
        void Application_PreRequestHandlerExecute(object sender, EventArgs e)
        {
            HttpApplication app = sender as HttpApplication;
            string acceptEncoding = app.Request.Headers["Accept-Encoding"];
            Stream prevUncompressedStream = app.Response.Filter;

            if (app.Context.CurrentHandler == null)
                return;

            if (!(app.Context.CurrentHandler is System.Web.UI.Page ||
                app.Context.CurrentHandler.GetType().Name == "SyncSessionlessHandler") ||
                app.Request["HTTP_X_MICROSOFTAJAX"] != null)
                return;

            if (acceptEncoding == null || acceptEncoding.Length == 0)
                return;

            acceptEncoding = acceptEncoding.ToLower();

            if (acceptEncoding.Contains("deflate") || acceptEncoding == "*")
            {
                // deflate
                app.Response.Filter = new DeflateStream(prevUncompressedStream,
                    CompressionMode.Compress);
                app.Response.AppendHeader("Content-Encoding", "deflate");
            }
            else if (acceptEncoding.Contains("gzip"))
            {
                // gzip
                app.Response.Filter = new GZipStream(prevUncompressedStream,
                    CompressionMode.Compress);
                app.Response.AppendHeader("Content-Encoding", "gzip");
            }
        } 
    }
}
