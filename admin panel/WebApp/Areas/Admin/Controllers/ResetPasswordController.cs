using Data.Repository;
using Data.Repository.Services;
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
    public class ResetPasswordController : Controller
    {
        ICommon_Repository _ICommon_Repository = new Common_Repository(new dataEntities());



        // GET: /Admin/ResetPassword/
        public ActionResult Index(string id)
        {
            id = CommonMethods.DecryptString(id);
            var ids = id.Split('*');

            ViewBag.User_Id = ids[0];
            if (Convert.ToDateTime(ids[1]).AddMinutes(15) > DateTime.Now)
            {
                ViewBag.IsActive = true;
            }
            else
            {
                ViewBag.IsActive = false;
            }

            return View();
        }


        public JsonResult SaveUpdate_ResetPassword(int User_Id, string NewPassword)
        {

            var Response = _ICommon_Repository.SaveUpdate_ResetPassword(User_Id, NewPassword);

            var jsonResult = Json(new
            {
                aaData = Response
            }, JsonRequestBehavior.AllowGet);
            jsonResult.MaxJsonLength = Int32.MaxValue;
            return jsonResult;
        }
    }
}