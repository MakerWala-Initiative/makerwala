using Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web;
using System.Web.Http.Filters;

namespace WebApp.Utility
{
    public class UserFriendlyExceptionFilterAttribute : ExceptionFilterAttribute
    {
        public override void OnException(HttpActionExecutedContext context)
        {
            var friendlyException = context.Exception.Message.ToString();
            if (friendlyException != null)
            {
               // context.Response = context.Request.CreateResponse(HttpStatusCode.BadRequest, new { Message = friendlyException });
                context.Response=context.Request.CreateResponse(new ApiResponse(-1, friendlyException, null, "failure"));
            }
        }

    }

    public class UserFriendlyException : Exception
    {
        public UserFriendlyException(string message) : base(message) { }
        public UserFriendlyException(string message, Exception innerException) : base(message, innerException) { }
    }
}