
using Data.Repository;
using Data.Utility;
using Data.Repository.Services;
using Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using System.Xml;
using WebApp.Areas.Admin.Filters;
using WebApp.Utility;
using Data.Services;
namespace WebApp.Areas.Admin.Controllers
{
    public partial class TeacherController : AdminSessionController
    {
        IMaster_Repository _IMaster_Repository = new Master_Repository(new dataEntities());
        ICommon_Repository _ICommon_Repository = new Common_Repository(new dataEntities());

        // GET: /Admin/Customer/
        public ActionResult Index()
        {
            CommonMethods.activitylogs("web", "teacher list", "Information", SessionFacade.AdminUserSession.teacherid);
           
            return View();
        }
        public ActionResult TeacherForm(string id = "0")
        {
            ViewBag.Customer_Id = id;
            return View();
        }




        public JsonResult Get_User_Role_List()
        {
            var TeacherList = _ICommon_Repository.getuserrole().ToList();
            var jsonResult = Json(new
            {
                aaData = TeacherList
            }, JsonRequestBehavior.AllowGet);
            jsonResult.MaxJsonLength = Int32.MaxValue;
            return jsonResult;
        }


        public JsonResult Get_Teacher_List(String Search)
        {
            int numberOfObjectsPerPage = 0;

            if (SessionFacade.AdminUserSession.userroleid != 1)
            {
                Search += " and t.blockid=" + SessionFacade.AdminUserSession.blockid ;

            }
            var TeacherList = _IMaster_Repository.Get_Teacher_List(Search, 0, 0, 0, out numberOfObjectsPerPage).ToList();

            var jsonResult = Json(new
            {
                aaData = TeacherList
            }, JsonRequestBehavior.AllowGet);
            jsonResult.MaxJsonLength = Int32.MaxValue;
            return jsonResult;
        }

        public JsonResult Get_Teachers_List(String Search)
        {
            var TeacherList = _IMaster_Repository.Get_Teachers_List().ToList();
            var jsonResult = Json(new
            {
                aaData = TeacherList
            }, JsonRequestBehavior.AllowGet);
            jsonResult.MaxJsonLength = Int32.MaxValue;
            return jsonResult;
        }


        public JsonResult SaveUpdate_Delete_Teacher(Get_Teacher_List_Result tch, bool isDelete = false)
        {

            //tch.blockid = SessionFacade.AdminUserSession.blockid;
            tch.createddate = DateTime.Now;
            tch.createdby = SessionFacade.AdminUserSession.teacherid;
            tch.updateddate = DateTime.Now;
            tch.updatedby = SessionFacade.AdminUserSession.teacherid;

            tch.imgprofile = CommonFunction.SaveStringByte(tch.imgprofile, "ProfileImage", "");

            int RetrunValue = 0;
            string xmlStr = string.Empty;

            XmlDocument xmlProduct =  CommonFunction.ConvertToXml(tch);
            xmlStr += "<teacher>" + xmlProduct.DocumentElement.InnerXml + "</teacher>";
            xmlStr = "<DocumentElement>" + xmlStr + "</DocumentElement>";

            string OutIdentity = "0";
            RetrunValue = _IMaster_Repository.SaveUpdateDelete_teacher(xmlStr, isDelete, out OutIdentity);

            if (RetrunValue > 0)
            {
                if (isDelete)
                {
                    CommonMethods.activitylogs("web", "teacher-delete", "Information", SessionFacade.AdminUserSession.teacherid);
                }
                else
                {
                    if (tch.teacherid > 0)
                    {
                        CommonMethods.activitylogs("web", "teacher entry-edit", "Information", SessionFacade.AdminUserSession.teacherid);
                    }
                    else
                    {
                        CommonMethods.activitylogs("web", "teacher entry-add", "Information", SessionFacade.AdminUserSession.teacherid);

                    }
                }
            }

            var jsonResult = Json(new
            {
                aaData = RetrunValue
            }, JsonRequestBehavior.AllowGet);
            jsonResult.MaxJsonLength = Int32.MaxValue;
            return jsonResult;

        }


      
	}
}