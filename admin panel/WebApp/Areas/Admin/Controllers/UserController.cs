
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using WebApp.Areas.Admin.Filters;
namespace WebApp.Areas.Admin.Controllers
{
    public partial class UserController : AdminSessionController
    {

       // IMaster_Repository _IMaster_Repository = new Master_Repository(new uvtechto_mis_demoEntities());
      //  ICommon_Repository _ICommon_Repository = new Common_Repository(new uvtechto_mis_demoEntities());

        // GET: /Admin/User/
        public ActionResult Index()
        {
            return View();

        }

        //
        // GET: /Admin/User/
        public ActionResult UserForm(string id = "0")
        {
            ViewBag.UserID = id;
            return View();

        }

        public ActionResult UserGroup()
        {
            return View();
        }

        public ActionResult UserGroupForm(string id = "0")
        {
            ViewBag.GroupID = id;
            return View();
        }
        
	}
}