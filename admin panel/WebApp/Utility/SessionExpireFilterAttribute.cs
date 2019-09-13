using Data.Repository;
using Data.Services;
using Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using System.Web.Routing;

namespace WebApp.Utility
{
    public class SessionExpireFilterAttribute : ActionFilterAttribute
    {
      
        public class AuthorizeWebUserAttribute : AuthorizeAttribute
        {
            protected override bool AuthorizeCore(HttpContextBase httpContext)
            {
                if (SessionFacade.AdminUserSession == null)
                {
                    return false;
                }
                else
                {
                    if (SessionFacade.AdminUserSession.isactive == false)
                    {
                        SessionFacade.AdminUserSession = null;
                    }
                    else
                    {
                        ICommon_Repository _ICommon_Repository = new Common_Repository(new dataEntities());

                        var routeData = ((MvcHandler)httpContext.Handler).RequestContext.RouteData;
                        var routeControl = (string)routeData.Values["controller"];
                        var routeAction = (string)routeData.Values["action"];

                        //activitylog al = new activitylog();
                        //al.activitydate = DateTime.Now;
                        //al.application = "web";
                        //al.details ="Used page : " + routeControl +"=>"+ routeAction;
                        //al.type = "usage";
                        //al.userid = SessionFacade.AdminUserSession.teacherid;
                        //_ICommon_Repository.SaveUpdate_Delete_activitylogs(al);

                    }
                    return true;

                }
            }


            protected override void HandleUnauthorizedRequest(AuthorizationContext filterContext)
            {
                if (SessionFacade.AdminUserSession == null)
                {
                    filterContext.Result = new RedirectToRouteResult(new RouteValueDictionary(new { controller = "Login", action = "index" }));
                }
                else
                {
                    filterContext.Result = new RedirectToRouteResult(new RouteValueDictionary(new { controller = "error", action = "Unauthorised" }));
                }
            }
        }

    }
}