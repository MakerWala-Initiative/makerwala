
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
    public class LoginController : Controller
    {

        ICommon_Repository _ICommon_Repository = new Common_Repository(new dataEntities());



        // GET: /Admin/Login/
        public ActionResult Index()
        {

         
            CommonMethods.activitylogs("web", "login view", "Information", 0);
           
            return View();
        }


        [HttpPost]
        public int User_Login(string User_Name, string Password)
        {
            int Response_Id = 0;
            var existUsers = _ICommon_Repository.Login_User(User_Name, Password);
            SessionFacade.AdminUserSession = existUsers;
            if (existUsers != null )
            {
                if (existUsers.isactive == true && existUsers.rolename != "users")
                {
                    Response_Id = existUsers.teacherid;
                }
                else
                {
                    Response_Id = 0;
                }
            }
            else
            {
                Response_Id = -1;
            }
            return Response_Id;
        }


        public ActionResult Logout()
        {
            SessionFacade.AdminUserSession = null;
            return View("Index");
        }
	}
}