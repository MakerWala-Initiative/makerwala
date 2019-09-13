using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using WebApp.Areas.Admin.Filters;
namespace WebApp.Areas.Admin.Controllers
{
    public partial class DashboardController : AdminSessionController
    {

        //[HttpPost]
        //public JsonResult GetEventList(String Type = "")
        //{
        //    // Initialization.
        //    var data = _IDashboard_Repository.Get_Event_List(SessionFacade.AdminUserSession.UserID, Type);
        //    //result = this.Json(data, JsonRequestBehavior.AllowGet);


        //    // Return info.

        //    var jsonResult = Json(new
        //    {
        //        aaData = data,
        //    }, JsonRequestBehavior.AllowGet);
        //    return jsonResult;

        //}
        //[HttpPost]
        //public JsonResult Save_Update_Delete_Event(EventMaster UE, Boolean Is_Delete = false)
        //{
        //    UE.UserID = SessionFacade.AdminUserSession.UserID;
        //    var existUsers = _IDashboard_Repository.Save_Update_Delete_Event(UE, Is_Delete);

        //    var jsonResult = Json(new
        //    {
        //        aaData = existUsers,
        //    }, JsonRequestBehavior.AllowGet);
        //    return jsonResult;
        //}



    }
}