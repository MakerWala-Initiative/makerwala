using Data.Repository;
using Data.Services;
using Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using WebApp.Utility;

namespace WebApp.Areas.Admin.Filters
{
    [SessionExpireFilter]
    [WebApp.Utility.SessionExpireFilterAttribute.AuthorizeWebUser]
    public class AdminSessionController : Controller
    {
        ICommon_Repository _ICommon_Repository = new Common_Repository(new dataEntities());

      

   

        protected override void OnException(ExceptionContext filterContext)
        {
            string action = filterContext.RouteData.Values["action"].ToString();
            Exception e = filterContext.Exception;
            filterContext.HttpContext.Response.Clear(); 
            //filterContext.ExceptionHandled = false;
            //filterContext.Result = new ViewResult()
            //{
            //    ViewName = "Dashboard"
            //}; 
            //filterContext.Result = RedirectToAction("Index", "ErrorHandler");
            //// OR 
            //filterContext.Result = new ViewResult
            //{
            //    ViewName = "~/Views/ErrorHandler/Index.cshtml"
            //};

            string errormsg="";
            if (e.InnerException != null)
            {
                errormsg = e.InnerException.ToString();
            }
            else{
            errormsg =e.ToString();
            }


            activitylog al = new activitylog();
            al.activitydate = DateTime.Now;
            al.application = "web";
            al.details = errormsg;
            al.type = "exception";
            al.appversion = System.Configuration.ConfigurationManager.AppSettings["webpages:Version"];
            al.userid = SessionFacade.AdminUserSession.teacherid;
            _ICommon_Repository.SaveUpdate_Delete_activitylogs(al);
        }
    }
}
