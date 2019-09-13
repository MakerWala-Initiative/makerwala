
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using WebApp.Areas.Admin.Filters;
namespace WebApp.Areas.Admin.Controllers
{
    public partial class DashboardController :  AdminSessionController
    {

        //public JsonResult Get_City_List()
        //{
        //    var Customer_Contact = _ICommon_Repository.Get_City_List();

        //    var jsonResult = Json(new
        //    {
        //        aaData = Customer_Contact
        //    }, JsonRequestBehavior.AllowGet);
        //    jsonResult.MaxJsonLength = Int32.MaxValue;
        //    return jsonResult;
        //}

        //public JsonResult Get_Department_List()
        //{
        //    var Designation_List = _ICommon_Repository.Get_Department_List();

        //    var jsonResult = Json(new
        //    {
        //        aaData = Designation_List
        //    }, JsonRequestBehavior.AllowGet);
        //    jsonResult.MaxJsonLength = Int32.MaxValue;
        //    return jsonResult;
        //}


        //public JsonResult Get_UserGroup_List()
        //{
        //    var user_Group = _ICommon_Repository.Get_UserGroup_List();

        //    var jsonResult = Json(new
        //    {
        //        aaData = user_Group
        //    }, JsonRequestBehavior.AllowGet);
        //    jsonResult.MaxJsonLength = Int32.MaxValue;
        //    return jsonResult;
        //}
       
	}
}