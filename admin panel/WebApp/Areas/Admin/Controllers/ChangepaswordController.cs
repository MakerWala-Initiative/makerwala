using Data.Repository;
using Data.Services;
using Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using WebApp.Areas.Admin.Filters;
using WebApp.Utility;

namespace WebApp.Areas.Admin.Controllers
{
    public class ChangepaswordController : AdminSessionController
    {
        ICommon_Repository _ICommon_Repository = new Common_Repository(new dataEntities());

        // GET: /Admin/Changepasword/
        public ActionResult Index()
        {
            CommonMethods.activitylogs("web", "Change password", "Information", SessionFacade.AdminUserSession.teacherid);
           
            return View();
        }

        public JsonResult SaveUpdate_Password(string CurrenPassword, string NewPassword)
        {
            var Response = _ICommon_Repository.SaveUpdate_Password(SessionFacade.AdminUserSession.teacherid, CurrenPassword, NewPassword);

            var jsonResult = Json(new
            {
                aaData = Response
            }, JsonRequestBehavior.AllowGet);
            jsonResult.MaxJsonLength = Int32.MaxValue;
            return jsonResult;
        }
    }
}