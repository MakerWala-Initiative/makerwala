using Data.Repository;
using Data.Services;
using Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using WebApp.Utility;

namespace WebApp.Areas.Admin.Controllers
{
    public class forgotpasswordController : Controller
    {
        ICommon_Repository _ICommon_Repository = new Common_Repository(new dataEntities());

        // GET: /Admin/forgotpassword/
        public ActionResult Index()
        {
            return View();
        }



        public JsonResult forgot_Password(string username)
        {
            int status = 0;
            var statuspasswrod = _ICommon_Repository.Get_ResetPassword_Details(username);
            if (statuspasswrod != null)
            {
                status = MailTemplates.ResetMail(statuspasswrod);
            }
            if (status == 1)
            {
                status = 1;
            }

            var jsonResult = Json(new
            {
                aaData = status,
            }, JsonRequestBehavior.AllowGet);
            jsonResult.MaxJsonLength = Int32.MaxValue;
            return jsonResult;
        }

    }
}