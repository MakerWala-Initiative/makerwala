
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
namespace WebApp.Areas.Admin.Controllers
{
    public partial class SchoolController : AdminSessionController
    {
        IMaster_Repository _IMaster_Repository = new Master_Repository(new dataEntities());

        // GET: /Admin/Customer/
        public ActionResult Index()
        {
            CommonMethods.activitylogs("web", "school list", "Information", SessionFacade.AdminUserSession.teacherid);
           
            return View();
        }
        public ActionResult SchoolForm(string id = "0")
        {
            ViewBag.Block_Id = id;
            return View();
        }


        public JsonResult Get_School_List(String Search="")
        {
            if (SessionFacade.AdminUserSession.userroleid != 1)
            {
                Search += " and s.blockid=" + SessionFacade.AdminUserSession.blockid;
            }

            int numberOfObjectsPerPage = 0;

            var SchoolList = _IMaster_Repository.Get_School_List(Search, 0, 0, 0, out numberOfObjectsPerPage).ToList();

            var jsonResult = Json(new
            {
                aaData = SchoolList
            }, JsonRequestBehavior.AllowGet);
            jsonResult.MaxJsonLength = Int32.MaxValue;
            return jsonResult;
        }


        public JsonResult SaveUpdate_Delete_School(Get_School_List_Result sc, bool isDelete = false)
        {
           

           // sc.blockid = SessionFacade.AdminUserSession.blockid;
            sc.createddate = DateTime.Now;
            sc.createdby = SessionFacade.AdminUserSession.teacherid;
            sc.updateddate = DateTime.Now;
            sc.updatedby = SessionFacade.AdminUserSession.teacherid;

           
            int RetrunValue = 0;
            string xmlStr = string.Empty;

            XmlDocument xmlProduct = CommonFunction.ConvertToXml(sc);
            xmlStr += "<school>" + xmlProduct.DocumentElement.InnerXml + "</school>";
            xmlStr = "<DocumentElement>" + xmlStr + "</DocumentElement>";

            string OutIdentity = "0";
            RetrunValue = _IMaster_Repository.SaveUpdateDelete_School(xmlStr, isDelete, out OutIdentity);

            if (RetrunValue > 0)
            {
                if (isDelete)
                {
                    CommonMethods.activitylogs("web", "school-delete", "Information", SessionFacade.AdminUserSession.teacherid);
                }
                else
                {
                    if (sc.schoolid > 0)
                    {
                        CommonMethods.activitylogs("web", "school entry-edit", "Information", SessionFacade.AdminUserSession.teacherid);
                    }
                    else
                    {
                        CommonMethods.activitylogs("web", "school entry-add", "Information", SessionFacade.AdminUserSession.teacherid);
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