using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http.Formatting;
using System.Net.Http.Headers;
using System.Web;
using System.Web.Http;
using System.Web.Http.Cors;
using WebApp.API;
using WebApp.Utility;

namespace WebApp.App_Start
{
    public class WebApiConfig
    {
        public static void Register(HttpConfiguration config)
        {
            //// Web API routes
            //config.MapHttpAttributeRoutes();

            //config.Routes.MapHttpRoute(
            //    name: "DefaultApi",
            //    routeTemplate: "api/{controller}/{id}",
            //    defaults: new { id = RouteParameter.Optional }
            //);

            var cors = new EnableCorsAttribute("*", "*", "*");
            // Web API routes

            config.EnableCors(cors);
            config.MapHttpAttributeRoutes();
            config.Formatters.JsonFormatter.SupportedMediaTypes.Add(new MediaTypeHeaderValue("application/json"));
            config.Routes.MapHttpRoute(
                name: "DefaultApi",
                routeTemplate: "api/{controller}/{action}/{id}",
                defaults: new { id = RouteParameter.Optional }
            );

            config.Filters.Add(new UserFriendlyExceptionFilterAttribute());
            config.MessageHandlers.Add(new Utility.CompressedFilter.EncodingDelegateHandler());



            //  GlobalConfiguration.Configuration.Formatters.XmlFormatter.SupportedMediaTypes.Clear();
            //config.Formatters.Clear();
            // config.Formatters.Add(new JsonMediaTypeFormatter()); 


            //// Adding formatter for Json   
            //config.Formatters.JsonFormatter.MediaTypeMappings.Add(
            //    new QueryStringMapping("type", "json", new MediaTypeHeaderValue("application/json")));


        }

    }
}